package com.mobile.login_auth_api.domain.limite;

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
public class Limite {

    @Id @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    private BigDecimal limite;
    @Column(unique = true, nullable = false)
    private YearMonth mesReferencia;
    private LocalDateTime dataExclusao;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public Limite(BigDecimal limite, YearMonth mesReferencia, User user) {
        this.limite = limite;
        this.mesReferencia = mesReferencia;
        this.user = user;
    }
    public Limite(String id, BigDecimal limite, YearMonth mesReferencia) {
        this.id = id;
        this.limite = limite;
        this.mesReferencia = mesReferencia;
    }
}
