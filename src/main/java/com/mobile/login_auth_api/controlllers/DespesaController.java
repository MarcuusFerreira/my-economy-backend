package com.mobile.login_auth_api.controlllers;

import com.mobile.login_auth_api.domain.despesa.Despesa;
import com.mobile.login_auth_api.domain.service.UserService;
import com.mobile.login_auth_api.domain.service.ValidateDateService;
import com.mobile.login_auth_api.domain.user.User;
import com.mobile.login_auth_api.dto.DespesaRequestDTO;
import com.mobile.login_auth_api.dto.DespesaResponseDTO;
import com.mobile.login_auth_api.dto.DespesaUpdateRequestDTO;
import com.mobile.login_auth_api.repositories.DespesaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.YearMonth;

@RestController
@RequestMapping("/despesa")
@RequiredArgsConstructor
public class DespesaController {

    private final UserService userService;
    private final DespesaRepository despesaRepository;
    private final ValidateDateService validateDateService;

    @PostMapping("/register")
    public ResponseEntity<?> cadastrarDespesa(@RequestBody DespesaRequestDTO body) {
        try {
            User user = userService.findUser(body.email());
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
            User user = userService.findUser(body.email());
            Despesa despesa = new Despesa(body.id(), user, body.descricao(), body.valor(), body.mesReferencia());
            despesa = despesaRepository.save(despesa);
            DespesaResponseDTO responseDTO = new DespesaResponseDTO(despesa.getId(), despesa.getDescricao(), despesa.getValor(), despesa.getMesReferencia());
            return ResponseEntity.ok(responseDTO);
        } catch (RuntimeException exception) {
            return ResponseEntity.badRequest().body(exception.getMessage());
        }
    }

    @DeleteMapping("/remove/{id}")
    public ResponseEntity<?> removerDespesa(@PathVariable String id) {
        int update = despesaRepository.deleteDespesa(LocalDateTime.now(), id);
        System.out.println("atualizou " + update);
        if (update == 1) {
            return ResponseEntity.ok("Despesa removida com sucesso!");
        }
        return ResponseEntity.badRequest().build();
    }

    @GetMapping("/{email}")
    public ResponseEntity<?> getDespesas(@PathVariable String email) {
        return ResponseEntity.ok().body(despesaRepository.findByEmailAndNotNullDataExclusao(email));
    }

    @GetMapping("/{email}/{mesAno}")
    public ResponseEntity<?> getDespesas(@PathVariable String email, @PathVariable YearMonth mesAno) {
        return ResponseEntity.ok().body(despesaRepository.findByEmailAndMesAndNotNullDataExclusao(email, mesAno));
    }
}
