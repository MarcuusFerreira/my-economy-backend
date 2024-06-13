package com.mobile.login_auth_api.dto;

import java.math.BigDecimal;
import java.time.YearMonth;

public record DespesaResponseDTO(
        String id,
        String descricao,
        BigDecimal valor,
        YearMonth mesReferencia
) {
}
