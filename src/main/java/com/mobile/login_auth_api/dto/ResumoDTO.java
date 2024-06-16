package com.mobile.login_auth_api.dto;

import java.math.BigDecimal;

public record ResumoDTO(
        BigDecimal limite,
        BigDecimal totalDespesa,
        BigDecimal economizou,
        String categoria
) {
}
