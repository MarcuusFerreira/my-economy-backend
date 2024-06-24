package com.mobile.login_auth_api.dto;

import com.mobile.login_auth_api.domain.despesa.Despesa;

import java.math.BigDecimal;
import java.time.YearMonth;

public record DespesaResponseDTO(
        String id,
        String descricao,
        BigDecimal valor,
        YearMonth mesReferencia
) {

    public static DespesaResponseDTO response(Despesa despesa) {
        return new DespesaResponseDTO(
                despesa.getId(),
                despesa.getDescricao(),
                despesa.getValor(),
                despesa.getMesReferencia()
        );
    }
}
