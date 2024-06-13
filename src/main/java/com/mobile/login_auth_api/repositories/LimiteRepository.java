package com.mobile.login_auth_api.repositories;

import com.mobile.login_auth_api.domain.limite.Limite;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface LimiteRepository extends JpaRepository<Limite, String> {

    @Query("SELECT e FROM Limite e WHERE e.user.email = :email AND e.dataExclusao IS NOT NULL")
    List<Limite> findByEmailAndNotNullDataExclusao(@Param("email") String email);

    @Modifying
    @Transactional
    @Query("UPDATE Limite l SET l.dataExclusao = :dataExclusao WHERE l.id = :id")
    int deleteLimite(@Param("dataExclusao") LocalDateTime dataExclusao, @Param("id") String id);

}
