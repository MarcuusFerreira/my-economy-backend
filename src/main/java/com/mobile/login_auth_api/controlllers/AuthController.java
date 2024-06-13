package com.mobile.login_auth_api.controlllers;

import com.mobile.login_auth_api.domain.user.User;
import com.mobile.login_auth_api.dto.LoginRequestDTO;
import com.mobile.login_auth_api.dto.LoginResponseDTO;
import com.mobile.login_auth_api.dto.RegisterRequestDTO;
import com.mobile.login_auth_api.infra.security.TokenService;
import com.mobile.login_auth_api.repositories.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class AuthController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenService tokenService;

    @PostMapping("/singin")
    public ResponseEntity<LoginResponseDTO> singin(@RequestBody LoginRequestDTO body) {
        System.out.println(body.email());
        User user = this.userRepository.findByEmail(body.email()).orElseThrow(() -> new RuntimeException("User not found"));
        if(passwordEncoder.matches(body.password(), user.getPassword())) {
            String token = this.tokenService.generateToken(user);
            LoginResponseDTO loginResponseDTO = new LoginResponseDTO(user.getEmail(), user.getName(), user.getBirthday(), token);
            return ResponseEntity.ok(loginResponseDTO);
        }
        return ResponseEntity.badRequest().build();
    }

    @PostMapping("/singup")
    public ResponseEntity<LoginResponseDTO> singup(@RequestBody RegisterRequestDTO body) {
        Optional<User> user = this.userRepository.findByEmail(body.email());
        if (user.isEmpty()) {
            if(body.password().equals(body.confirmPassword())) {
                User newUser = new User(body.email(), body.name(), passwordEncoder.encode(body.password()), body.birthday());
                newUser = this.userRepository.save(newUser);
                String token = this.tokenService.generateToken(newUser);
                return ResponseEntity.ok(new LoginResponseDTO(newUser.getEmail(), newUser.getName(), newUser.getBirthday(), token));
            }
        }
        return ResponseEntity.badRequest().build();
    }

    @PostMapping("/singout")
    public ResponseEntity<?> singout(HttpServletRequest request) {
        var token = request.getHeader("Authorization").replace("Bearer ", "");
        tokenService.addInvalidToken(token);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/tokens")
    public List<String> getToken() {
        return tokenService.getInvalidToken();
    }
}
