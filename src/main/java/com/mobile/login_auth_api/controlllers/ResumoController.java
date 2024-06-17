package com.mobile.login_auth_api.controlllers;

import com.mobile.login_auth_api.domain.service.ResumoService;
import com.mobile.login_auth_api.domain.user.User;
import com.mobile.login_auth_api.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ResumoController {

    @Autowired
    private ResumoService resumoService;

    @Autowired
    UserRepository usuarioRepository;
    @Autowired
    private UserRepository userRepository;

    @GetMapping("/resumo")
    public ResponseEntity<?> obterResumoFinanceiro(@RequestParam("userId") Long userId) {
        try {
            User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
            System.out.println("Vou retornar ");
            return ResponseEntity.ok(resumoService.obterResumoFinanceiro(user));
        } catch (RuntimeException exception) {
            exception.printStackTrace();
            return ResponseEntity.badRequest().body(exception);
        }
    }
}
