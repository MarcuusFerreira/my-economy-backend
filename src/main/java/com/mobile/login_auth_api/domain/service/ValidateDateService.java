package com.mobile.login_auth_api.domain.service;

import org.springframework.stereotype.Service;

import java.time.YearMonth;

@Service
public class ValidateDateService {

    public boolean validateYearMonth(YearMonth mesReferencia) {
        return mesReferencia.isBefore(YearMonth.now());
    }
}
