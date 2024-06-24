package com.mobile.login_auth_api.controlllers;

import com.mobile.login_auth_api.domain.limite.Limite;
import com.mobile.login_auth_api.domain.service.LimiteService;
import com.mobile.login_auth_api.domain.service.UserService;
import com.mobile.login_auth_api.domain.service.ValidateDateService;
import com.mobile.login_auth_api.domain.user.User;
import com.mobile.login_auth_api.dto.*;
import com.mobile.login_auth_api.repositories.LimiteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/limite")
@RequiredArgsConstructor
public class LimiteController {

    private final UserService userService;
    private final ValidateDateService validateDateService;
    private final LimiteRepository limiteRepository;
    private final LimiteService limiteService;

    @PostMapping("/register")
    public ResponseEntity<?> cadastrarDespesa(@RequestBody LimiteRequestDTO body) {
        try {
            User user = userService.findUser(body.userId());
            if (user == null) {
                throw new RuntimeException("Usuário não encontrado!");
            }
            if (body.limite() == null || body.limite().compareTo(BigDecimal.ZERO) <= 0) {
                throw new RuntimeException("Limite deve ser maior que zero!");
            }
            if (validateDateService.validateYearMonth(body.mesReferencia())) {
                throw new RuntimeException("Não é possivel criar limite de meses anteriores ao atual!");
            }
            if (limiteRepository.existsByMesReferenciaAndUserIdAndDataExclusaoIsNull(body.mesReferencia(), body.userId())) {
                throw new RuntimeException("Já existe limite cadastrado para esse mes!");
            }
            Limite limite = new Limite(body.limite(), body.mesReferencia(), user);
            limite = limiteRepository.save(limite);
            return ResponseEntity.ok(new LimiteResponseDTO(limite.getId(), limite.getLimite(), limite.getMesReferencia()));
        } catch (RuntimeException exception) {
            return ResponseEntity.badRequest().body(new ApiResponse(exception.getMessage()));
        }
    }

    @PutMapping("/update")
    public ResponseEntity<?> atualizarLimite(@RequestBody LimiteUpdateRequestDTO body) {
        try {
            if (validateDateService.validateYearMonth(body.mesReferencia())) {
                throw new RuntimeException("Mês informado é anterior ao mês atual!");
            }
            Limite limite = limiteRepository.findById(body.id()).orElseThrow(() -> new RuntimeException("Limite Não encontrado"));
            limite.setLimite(body.limite());
            limite.setMesReferencia(body.mesReferencia());
            limite = limiteRepository.save(limite);
            return ResponseEntity.ok(new ApiResponse("Limite atualizado com sucesso!"));
        } catch (RuntimeException exception) {
            return ResponseEntity.badRequest().body(new ApiResponse(exception.getMessage()));
        }
    }

    @DeleteMapping("/remove/{id}")
    public ResponseEntity<?> removerLimite(@PathVariable String id) {
        try {
            Limite limite = limiteRepository.findById(id).orElseThrow(() -> new RuntimeException("Limite não existe"));
            if(validateDateService.validateYearMonth(limite.getMesReferencia())) {
                throw new RuntimeException("Não é possivel excluir limites de meses anteriores ao atual!");
            }
            int update = limiteRepository.deleteLimite(LocalDateTime.now(), id);
            System.out.println("atualizou " + update);
            if (update == 1) {
                return ResponseEntity.ok("Limite removido com sucesso!");
            }
        } catch (RuntimeException exception) {
            return ResponseEntity.badRequest().body(new ApiResponse(exception.getMessage()));
        }
        return ResponseEntity.internalServerError().body("Erro ao remover limite!");
    }

    @GetMapping("/get")
    public ResponseEntity<?> buscarLimitePorId(@RequestParam("userId") Long userId) {
        User user = userService.findUser(userId);
        List<LimiteResponseDTO> dados = limiteRepository.findByUserAndDataExclusaoIsNullOrderByMesReferencia (user).stream().map(LimiteResponseDTO::response).toList();
        return ResponseEntity.ok(dados);
    }

    @GetMapping("/lista-filtro")
    public ResponseEntity<?> listarFiltros(@RequestParam("userId") Long userId) {
        List<FiltroDTO> filtros = limiteRepository.findByUserIdAndDataExclusaoIsNullOrderByMesReferenciaAsc(userId).stream().map(FiltroDTO::createFiltroDTO).toList();
        return ResponseEntity.ok(filtros);
    }

    @GetMapping("/get-limite")
    public ResponseEntity<?> buscarLimite(@RequestParam("userId") Long userId, @RequestParam("mesReferencia") YearMonth mesReferencia) {
        List<LimiteResponseDTO> limites = limiteRepository.findByUserIdAndMesReferenciaAndDataExclusaoIsNull(userId, mesReferencia).stream().map(LimiteResponseDTO::response).toList();
        return ResponseEntity.ok(limites);

    }
}
