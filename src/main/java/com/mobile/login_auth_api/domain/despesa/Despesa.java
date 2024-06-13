package com.mobile.login_auth_api.domain.despesa;

import com.mobile.login_auth_api.domain.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.YearMonth;

@Getter
@Setter
@Table(name = "despesas")
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Despesa {

    @Id @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    private String descricao;
    private BigDecimal valor;
    private YearMonth mesReferencia;
    private LocalDateTime dataExclusao;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public Despesa(User user, String descricao, BigDecimal valor, YearMonth mesReferencia) {
        this.user = user;
        this.descricao = descricao;
        this.valor = valor;
        this.mesReferencia = mesReferencia;
    }

    public Despesa(String id, User user, String descricao, BigDecimal valor, YearMonth mesReferencia) {
        this.id = id;
        this.user = user;
        this.descricao = descricao;
        this.valor = valor;
        this.mesReferencia = mesReferencia;
    }
}
