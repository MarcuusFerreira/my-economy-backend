package com.mobile.login_auth_api.dto;

import com.mobile.login_auth_api.domain.limite.Limite;

import java.math.BigDecimal;
import java.time.Year;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public record ResumoDTO(
        String mesReferenciaText,
        YearMonth mesReferencia,
        BigDecimal limite,
        BigDecimal totalDespesa,
        BigDecimal economizado
) {

    public static ResumoDTO createResumoDTO(YearMonth mesReferencia, BigDecimal valorLimite, BigDecimal despesa, BigDecimal economizado) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM/yyyy", new Locale("pt", "BR"));
        String formattedMesAnoText = mesReferencia.format(formatter);
        String capitalizedMesAnoText = capitalizeFirstLetter(formattedMesAnoText);
        return new ResumoDTO(
                capitalizedMesAnoText,
                mesReferencia,
                valorLimite,
                despesa,
                economizado
        );
    }


    private static String capitalizeFirstLetter(String text) {
        if (text == null || text.isEmpty()) {
            return text;
        }
        return text.substring(0, 1).toUpperCase() + text.substring(1);
    }
}
