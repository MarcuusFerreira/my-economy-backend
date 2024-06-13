package com.mobile.login_auth_api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.YearMonth;

public record DespesaRequestDTO(
        @NotBlank @NotNull
        String email,
        @NotBlank @NotNull
        String descricao,
        @NotBlank @NotNull
        BigDecimal valor,
        @NotBlank @NotNull
        YearMonth mesReferencia) {
}
