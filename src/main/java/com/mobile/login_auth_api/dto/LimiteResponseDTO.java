package com.mobile.login_auth_api.dto;

import java.math.BigDecimal;
import java.time.YearMonth;

public record LimiteResponseDTO (
        String id,
        BigDecimal limite,
        YearMonth mesReferencia) {
}
