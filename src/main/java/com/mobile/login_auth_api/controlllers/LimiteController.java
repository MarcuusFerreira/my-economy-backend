package com.mobile.login_auth_api.controlllers;

import com.mobile.login_auth_api.domain.limite.Limite;
import com.mobile.login_auth_api.domain.service.LimiteService;
import com.mobile.login_auth_api.domain.service.UserService;
import com.mobile.login_auth_api.domain.service.ValidateDateService;
import com.mobile.login_auth_api.domain.user.User;
import com.mobile.login_auth_api.dto.LimiteRequestDTO;
import com.mobile.login_auth_api.dto.LimiteResponseDTO;
import com.mobile.login_auth_api.dto.LimiteUpdateRequestDTO;
import com.mobile.login_auth_api.repositories.LimiteRepository;
import com.mobile.login_auth_api.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
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
            // Verifica se o usuário existe
            User user = userService.findUser(body.userId());
            if (user == null) {
                throw new RuntimeException("Usuário não encontrado!");
            }

            // Valida o limite
            if (body.limite() == null || body.limite().compareTo(BigDecimal.ZERO) <= 0) {
                throw new RuntimeException("Limite deve ser maior que zero!");
            }

            // Cria e salva o novo limite
            Limite limite = new Limite(body.limite(), body.mesReferencia(), user);
            limite = limiteRepository.save(limite);

            // Retorna a resposta de sucesso
            return ResponseEntity.ok(new LimiteResponseDTO(limite.getId(), limite.getLimite(), limite.getMesReferencia()));
        } catch (RuntimeException exception) {

            return ResponseEntity.badRequest().body(exception.getMessage());
        }
    }

    @PostMapping("/update")
    public ResponseEntity<?> atualizarLimite(@RequestBody LimiteUpdateRequestDTO body) {
        try {
            if (validateDateService.validateYearMonth(body.mesReferencia())) {
                throw new RuntimeException("Mês informado é anterior ao mês atual!");
            }
            Limite limite = new Limite(body.id(), body.limite(), body.mesReferencia());
            limite = limiteRepository.save(limite);
            return ResponseEntity.ok(new LimiteResponseDTO(limite.getId(), limite.getLimite(), limite.getMesReferencia()));
        } catch (RuntimeException exception) {
            return ResponseEntity.badRequest().body(exception.getMessage());
        }
    }

    @DeleteMapping("/remove/{id}")
    public ResponseEntity<?> removerLimite(@PathVariable String id) {
        int update = limiteRepository.deleteLimite(LocalDateTime.now(), id);
        System.out.println("atualizou " + update);
        if (update == 1) {
            return ResponseEntity.ok("Limite removido com sucesso!");
        }
        return ResponseEntity.badRequest().build();
    }

    @GetMapping("/get")
    public ResponseEntity<?> buscarLimitePorEmail(@RequestParam("userId") Long userId) {
        User user = userService.findUser(userId);
        List<LimiteResponseDTO> lista = new ArrayList<>();
        List<Limite> dados = limiteRepository.findByUserAndDataExclusaoIsNull(user);
        for (Limite limite : dados) {
            LimiteResponseDTO dto = new LimiteResponseDTO(limite.getId(), limite.getLimite(), limite.getMesReferencia());
            lista.add(dto);
        }
        return ResponseEntity.ok(lista);
    }

    @GetMapping("/current-month")
    public ResponseEntity<?> getCurrentMonthLimite() {
        Optional<Limite> limite = limiteService.getLimiteForCurrentMonth();
        if (limite.isPresent()) {
            return ResponseEntity.ok(limite.get().getLimite());
        }
        return ResponseEntity.badRequest().build();
    }
}
