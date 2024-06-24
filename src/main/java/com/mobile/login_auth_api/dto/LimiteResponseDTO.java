package com.mobile.login_auth_api.dto;

import com.mobile.login_auth_api.domain.limite.Limite;

import java.math.BigDecimal;
import java.time.YearMonth;

public record LimiteResponseDTO (
        String id,
        BigDecimal limite,
        YearMonth mesReferencia) {

    public static LimiteResponseDTO response(Limite limite) {
        return new LimiteResponseDTO(
                limite.getId(),
                limite.getLimite(),
                limite.getMesReferencia()
        );
    }
}
