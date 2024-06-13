package com.mobile.login_auth_api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record RegisterRequestDTO(
        @NotBlank @NotNull
        String name,
        @NotBlank @NotNull
        String email,
        @NotBlank @NotNull
        String password,
        @NotBlank @NotNull
        String confirmPassword,
        @NotBlank @NotNull
        LocalDate birthday) {
}
