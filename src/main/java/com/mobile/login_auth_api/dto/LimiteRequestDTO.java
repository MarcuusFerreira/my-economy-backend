package com.mobile.login_auth_api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.YearMonth;

public record LimiteRequestDTO(
        @NotBlank @NotNull
        String email,
        @NotBlank @NotNull
        BigDecimal limite,
        @NotBlank @NotNull
        YearMonth mesReferencia) {
}
