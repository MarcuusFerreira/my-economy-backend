package com.mobile.login_auth_api.domain.service;

import com.mobile.login_auth_api.domain.despesa.Despesa;
import com.mobile.login_auth_api.repositories.DespesaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.YearMonth;
import java.util.List;
import java.util.Objects;

@Service
public class DespesaService {

    @Autowired
    private DespesaRepository despesaRepository;

    public List<Despesa> getDespesasForCurrentMonth() {
        YearMonth currentMonth = YearMonth.now();
        return despesaRepository.findByMesReferenciaAndDataExclusaoIsNull(currentMonth);
    }

    public BigDecimal getSomaDespesasForCurrentMonth() {
        YearMonth currentMonth = YearMonth.now();
        List<Despesa> despesas = despesaRepository.findByMesReferenciaAndDataExclusaoIsNull(currentMonth);
        return despesas.stream()
                .map(Despesa::getValor)
                .filter(Objects::nonNull) // Filtra valores nulos
                .reduce(BigDecimal.ZERO, BigDecimal::add); // Soma todos os valores
    }
}
