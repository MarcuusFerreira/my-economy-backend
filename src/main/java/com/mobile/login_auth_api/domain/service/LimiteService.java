package com.mobile.login_auth_api.domain.service;

import com.mobile.login_auth_api.domain.limite.Limite;
import com.mobile.login_auth_api.repositories.LimiteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.YearMonth;
import java.util.Optional;

@Service
public class LimiteService {

    @Autowired
    private LimiteRepository limiteRepository;

    public Optional<Limite> getLimiteForCurrentMonth() {
        YearMonth currentMonth = YearMonth.now();
        return limiteRepository.findByMesReferencia(currentMonth);
    }
}
