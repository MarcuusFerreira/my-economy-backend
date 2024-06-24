package com.mobile.login_auth_api.controlllers;

import com.mobile.login_auth_api.domain.despesa.Despesa;
import com.mobile.login_auth_api.domain.service.DespesaService;
import com.mobile.login_auth_api.domain.service.UserService;
import com.mobile.login_auth_api.domain.service.ValidateDateService;
import com.mobile.login_auth_api.domain.user.User;
import com.mobile.login_auth_api.dto.ApiResponse;
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
                throw new RuntimeException("Não é possivel criar despesa de meses anteriores ao atua!");
            }
            Despesa despesa = new Despesa(user, body.descricao(), body.valor(), body.mesReferencia());
            despesa = despesaRepository.save(despesa);
            DespesaResponseDTO responseDTO = DespesaResponseDTO.response(despesa);
            return ResponseEntity.ok(responseDTO);
        } catch (RuntimeException exception) {
            return ResponseEntity.badRequest().body(new ApiResponse(exception.getMessage()));
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
            return ResponseEntity.badRequest().body(new ApiResponse(exception.getMessage()));
        }
    }

    @DeleteMapping("/remove")
    public ResponseEntity<?> removerDespesa(@RequestParam("id") String id) {
        try {
            Despesa despesa = despesaRepository.findById(id).orElseThrow(() -> new RuntimeException("Despesa não encontrada"));
            if (validateDateService.validateYearMonth(despesa.getMesReferencia())) {
                throw new RuntimeException("Não é possivel excluir despesas de meses anteriores ao atual!");
            }
            int update = despesaRepository.deleteDespesa(LocalDateTime.now(), id);
            System.out.println("atualizou " + update);
            if (update == 1) {
                return ResponseEntity.ok(new ApiResponse("Despesa removida com sucesso!"));
            }
        } catch (RuntimeException exception) {
            return ResponseEntity.badRequest().body(new ApiResponse(exception.getMessage()));
        }
        return ResponseEntity.internalServerError().body(new ApiResponse("Erro ao remover despesa!"));
    }

    @GetMapping("/get")
    public ResponseEntity<?> getDespesas(@RequestParam("userId") Long userId) {
        User user = userService.findUser(userId);
        List<DespesaResponseDTO> dados = despesaRepository.findByUserAndDataExclusaoIsNullOrderByMesReferenciaAsc(user).stream().map(DespesaResponseDTO::response).toList();
        return ResponseEntity.ok().body(dados);
    }

    @GetMapping("/{email}/{mesAno}")
    public ResponseEntity<?> getDespesas(@PathVariable String id, @PathVariable YearMonth mesAno) {
        return ResponseEntity.ok().body(despesaRepository.findByIdAndMesAndNotNullDataExclusao(id, mesAno));
    }

    @GetMapping("/current-month")
    public ResponseEntity<BigDecimal> getSomaDespesasForCurrentMonth() {
        BigDecimal somaDespesas = despesaService.getSomaDespesasForCurrentMonth();
        return ResponseEntity.ok(somaDespesas);
    }

    @GetMapping("/get-despesa")
    public ResponseEntity<?> getDespesa(@RequestParam("userId") Long userId, @RequestParam("mesReferencia") YearMonth mesReferencia) {
        List<DespesaResponseDTO> responseDTOS = despesaRepository.findByUserIdAndMesReferenciaAndDataExclusaoIsNullOrderByMesReferenciaAsc(userId, mesReferencia).stream().map(DespesaResponseDTO::response).toList();
        return ResponseEntity.ok(responseDTOS);
    }
}
