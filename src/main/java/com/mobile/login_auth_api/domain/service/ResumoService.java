package com.mobile.login_auth_api.domain.service;

import com.mobile.login_auth_api.domain.despesa.Despesa;
import com.mobile.login_auth_api.domain.limite.Limite;
import com.mobile.login_auth_api.domain.user.User;
import com.mobile.login_auth_api.dto.ResumoDTO;
import com.mobile.login_auth_api.repositories.DespesaRepository;
import com.mobile.login_auth_api.repositories.LimiteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.YearMonth;
import java.util.List;

@Service
public class ResumoService {

    @Autowired
    private DespesaRepository despesaRepository;

    @Autowired
    private LimiteRepository limiteRepository;

    public ResumoDTO obterResumoFinanceiro(User user) {
        YearMonth mesAtual = YearMonth.now().minusMonths(1);

        // Obtendo o limite do mês
        Limite limite = limiteRepository.findByUserAndMesReferencia(user, mesAtual).orElse(null);
        BigDecimal valorLimite = limite != null ? limite.getLimite() : BigDecimal.ZERO;

        List<Despesa> despesas = despesaRepository.findByUserAndMesReferencia(user, mesAtual);
        BigDecimal totalDespesas = despesas.stream()
                .map(Despesa::getValor)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal economizado = valorLimite.subtract(totalDespesas);


        String categoriaEconomia;
        BigDecimal percentualEconomizado = economizado.divide(valorLimite, BigDecimal.ROUND_HALF_UP).multiply(BigDecimal.valueOf(100));

        if (percentualEconomizado.compareTo(BigDecimal.valueOf(30)) > 0) {
            categoriaEconomia = "Economizou muito";
        } else if (percentualEconomizado.compareTo(BigDecimal.ZERO) > 0) {
            categoriaEconomia = "Economizou";
        } else {
            categoriaEconomia = "Não economizou";
        }
        ResumoDTO resumo = new ResumoDTO(valorLimite, totalDespesas, economizado, categoriaEconomia);

        return resumo;
    }
}
