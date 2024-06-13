package com.mobile.login_auth_api.domain.user;

import com.mobile.login_auth_api.domain.despesa.Despesa;
import com.mobile.login_auth_api.domain.limite.Limite;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "users")
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    private String email;
    private String name;
    private String password;
    private LocalDate birthday;
    @OneToMany
    private List<Despesa> despesas;
    @OneToMany
    private List<Limite> listaLimites;

    public User(String email, String name, String password, LocalDate birthday) {}

}
