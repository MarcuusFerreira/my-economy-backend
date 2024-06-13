package com.mobile.login_auth_api.dto;

import java.time.LocalDate;

public record LoginResponseDTO(String email, String name, LocalDate birthday, String token) {
}
