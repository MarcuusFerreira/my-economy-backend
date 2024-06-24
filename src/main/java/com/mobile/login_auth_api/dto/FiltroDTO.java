package com.mobile.login_auth_api.dto;

import com.mobile.login_auth_api.domain.limite.Limite;

import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public record FiltroDTO(
        String mesAnoText,
        YearMonth mesReferencia
) {
    public static FiltroDTO createFiltroDTO(Limite limite) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM/yyyy", new Locale("pt", "BR"));
        String formattedMesAnoText = limite.getMesReferencia().format(formatter);
        String capitalizedMesAnoText = capitalizeFirstLetter(formattedMesAnoText);

        return new FiltroDTO(
                capitalizedMesAnoText,
                limite.getMesReferencia()
        );
    }

    private static String capitalizeFirstLetter(String text) {
        if (text == null || text.isEmpty()) {
            return text;
        }
        return text.substring(0, 1).toUpperCase() + text.substring(1);
    }
}
