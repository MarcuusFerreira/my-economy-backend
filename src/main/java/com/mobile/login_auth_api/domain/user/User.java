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
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true, nullable = false)
    private String email;
    private String name;
    private String password;
    private LocalDate birthday;
    @OneToMany
    private List<Despesa> despesas;
    @OneToMany
    private List<Limite> listaLimites;

    public User(String email, String name, String password, LocalDate birthday) {
        this.email = email;
        this.name = name;
        this.password = password;
        this.birthday = birthday;
    }

    @Override
    public String toString() {
        return new StringBuilder()
                .append("User Details:\n")
                .append("ID: ").append(id).append("\n")
                .append("Email: ").append(email).append("\n")
                .append("Name: ").append(name).append("\n")
                .append("Password: ").append(password).append("\n")
                .append("Birthday: ").append(birthday).append("\n")
                .toString();
    }
}
