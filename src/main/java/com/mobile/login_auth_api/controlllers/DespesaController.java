package com.mobile.login_auth_api.controlllers;

import com.mobile.login_auth_api.domain.despesa.Despesa;
import com.mobile.login_auth_api.domain.service.DespesaService;
import com.mobile.login_auth_api.domain.service.UserService;
import com.mobile.login_auth_api.domain.service.ValidateDateService;
import com.mobile.login_auth_api.domain.user.User;
import com.mobile.login_auth_api.dto.DespesaRequestDTO;
import com.mobile.login_auth_api.dto.DespesaResponseDTO;
import com.mobile.login_auth_api.dto.DespesaUpdateRequestDTO;
import com.mobile.login_auth_api.repositories.DespesaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/despesa")
@RequiredArgsConstructor
public class DespesaController {

    private final UserService userService;
    private final DespesaRepository despesaRepository;
    private final ValidateDateService validateDateService;
    @Autowired
    private DespesaService despesaService;

    @PostMapping("/register")
    public ResponseEntity<?> cadastrarDespesa(@RequestBody DespesaRequestDTO body) {
        try {
            User user = userService.findUser(body.userId());
            if (validateDateService.validateYearMonth(body.mesReferencia())){
                throw new RuntimeException("Mês informado é anterior ao mês atual!");
            }
            Despesa despesa = new Despesa(user, body.descricao(), body.valor(), body.mesReferencia());
            despesa = despesaRepository.save(despesa);
            DespesaResponseDTO responseDTO = new DespesaResponseDTO(despesa.getId(), despesa.getDescricao(), despesa.getValor(), despesa.getMesReferencia());
            return ResponseEntity.ok(responseDTO);
        } catch (RuntimeException exception) {
            return ResponseEntity.badRequest().body(exception.getMessage());
        }
    }

    @PutMapping("/update")
    public ResponseEntity<?> atualizarDespesa(@RequestBody DespesaUpdateRequestDTO body) {
        try{
            despesaRepository.findByIdAndNotNullDataExclusao(body.id()).orElseThrow(() -> new RuntimeException("Despesa não encontrada"));
            if (validateDateService.validateYearMonth(body.mesReferencia())){
                throw new RuntimeException("Mês informado é anterior ao mês atual!");
            }
            User user = userService.findUser(body.userId());
            Despesa despesa = new Despesa(body.id(), user, body.descricao(), body.valor(), body.mesReferencia());
            despesa = despesaRepository.save(despesa);
            DespesaResponseDTO responseDTO = new DespesaResponseDTO(despesa.getId(), despesa.getDescricao(), despesa.getValor(), despesa.getMesReferencia());
            return ResponseEntity.ok(responseDTO);
        } catch (RuntimeException exception) {
            return ResponseEntity.badRequest().body(exception.getMessage());
        }
    }

    @DeleteMapping("/remove")
    public ResponseEntity<?> removerDespesa(@RequestParam("id") String id) {
        int update = despesaRepository.deleteDespesa(LocalDateTime.now(), id);
        System.out.println("atualizou " + update);
        if (update == 1) {
            return ResponseEntity.ok("Despesa removida com sucesso!");
        }
        return ResponseEntity.badRequest().build();
    }

    @GetMapping("/get")
    public ResponseEntity<?> getDespesas(@RequestParam("userId") Long userId) {
        User user = userService.findUser(userId);
        List<DespesaResponseDTO> lista = new ArrayList<>();
        List<Despesa> dados = despesaRepository.findByUserAndDataExclusaoIsNull(user);
        for (Despesa despesa : dados) {
            lista.add(new DespesaResponseDTO(despesa.getId(), despesa.getDescricao(), despesa.getValor(), despesa.getMesReferencia()));
        }
        return ResponseEntity.ok().body(lista);
    }

    @GetMapping("/{email}/{mesAno}")
    public ResponseEntity<?> getDespesas(@PathVariable String email, @PathVariable YearMonth mesAno) {
        return ResponseEntity.ok().body(despesaRepository.findByEmailAndMesAndNotNullDataExclusao(email, mesAno));
    }

    @GetMapping("/current-month")
    public ResponseEntity<BigDecimal> getSomaDespesasForCurrentMonth() {
        BigDecimal somaDespesas = despesaService.getSomaDespesasForCurrentMonth();
        return ResponseEntity.ok(somaDespesas);
    }
}
