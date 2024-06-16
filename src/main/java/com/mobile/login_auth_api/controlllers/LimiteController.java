package com.mobile.login_auth_api.controlllers;

import com.mobile.login_auth_api.domain.limite.Limite;
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

import java.time.LocalDateTime;

@RestController
@RequestMapping("/limite")
@RequiredArgsConstructor
public class LimiteController {

    private final UserService userService;
    private final ValidateDateService validateDateService;
    private final LimiteRepository limiteRepository;

    @PostMapping("/register")
    public ResponseEntity<?> cadastrarDespesa(@RequestBody LimiteRequestDTO body) {
        try {
            User user = userService.findUser(body.userId());
            if (validateDateService.validateYearMonth(body.mesReferencia())) {
                throw new RuntimeException("Mês informado é anterior ao mês atual!");
            }
            Limite limite = new Limite(body.limite(), body.mesReferencia(), user);
            limite = limiteRepository.save(limite);
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

    @GetMapping("/{id}")
    public ResponseEntity<?> buscarLimitePorEmail(@PathVariable Long id) {
        return ResponseEntity.ok(limiteRepository.findByUserAndNotNullDataExclusao(id));
    }
}
