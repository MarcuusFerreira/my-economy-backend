package com.mobile.login_auth_api.controlllers;

import com.mobile.login_auth_api.domain.despesa.Despesa;
import com.mobile.login_auth_api.domain.limite.Limite;
import com.mobile.login_auth_api.dto.ResumoDTO;
import com.mobile.login_auth_api.repositories.DespesaRepository;
import com.mobile.login_auth_api.repositories.LimiteRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/resumo")
public class ResumoController {

    private final DespesaRepository despesaRepository;
    private final LimiteRepository limiteRepository;

    public ResumoController(DespesaRepository despesaRepository, LimiteRepository limiteRepository) {
        this.despesaRepository = despesaRepository;
        this.limiteRepository = limiteRepository;
    }

    @GetMapping("/consulta")
    public ResponseEntity<?> consultaDados(@RequestParam("userId") Long userId) {
        List<Limite> limites = limiteRepository.findByUserIdAndDataExclusaoIsNullOrderByMesReferenciaAsc(userId);
        List<Despesa> despesas = despesaRepository.findByUserIdAndDataExclusaoIsNull(userId);

        Map<YearMonth, List<Despesa>> despesasPorMes = despesas.stream()
                .collect(Collectors.groupingBy(Despesa::getMesReferencia));
        Map<YearMonth, Limite> limitesPorMes =  limites.stream()
                .collect(Collectors.toMap(Limite::getMesReferencia, limite -> limite));

        List<ResumoDTO> resumoDtos = new ArrayList<>();
        for (YearMonth mesReferencia : despesasPorMes.keySet()) {
            BigDecimal totalDespesas = despesasPorMes.get(mesReferencia).stream()
                    .map(Despesa::getValor)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            Limite limite = limitesPorMes.get(mesReferencia);
            BigDecimal valorLimite = (limite != null) ? limite.getLimite() : BigDecimal.ZERO;
            BigDecimal economizado = valorLimite.subtract(totalDespesas);

            resumoDtos.add(ResumoDTO.createResumoDTO(mesReferencia, valorLimite, totalDespesas, economizado));
        }

        for (YearMonth mesReferencia : limitesPorMes.keySet()) {
            if (!despesasPorMes.containsKey(mesReferencia)) {
                Limite limite = limitesPorMes.get(mesReferencia);
                BigDecimal valorLimite = limite.getLimite();
                BigDecimal economizado = valorLimite;

                resumoDtos.add(ResumoDTO.createResumoDTO(mesReferencia, valorLimite, BigDecimal.ZERO, economizado));
            }
        }
        resumoDtos.sort(Comparator.comparing(ResumoDTO::mesReferencia));
        return ResponseEntity.ok(resumoDtos);
    }
}
