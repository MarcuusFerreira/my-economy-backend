package com.mobile.login_auth_api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record LoginRequestDTO(
        @NotBlank @NotNull
        String email,
        @NotBlank @NotNull
        String password) {
}
