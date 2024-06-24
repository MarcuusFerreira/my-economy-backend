package com.mobile.login_auth_api.repositories;

import com.mobile.login_auth_api.domain.limite.Limite;
import com.mobile.login_auth_api.domain.user.User;
import com.mobile.login_auth_api.dto.FiltroDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.List;
import java.util.Optional;

public interface LimiteRepository extends JpaRepository<Limite, String> {

    List<Limite> findByUserAndDataExclusaoIsNull(User user);

    Optional<Limite> findByMesReferencia(YearMonth mesReferencia);

    @Modifying
    @Transactional
    @Query("UPDATE Limite l SET l.dataExclusao = :dataExclusao WHERE l.id = :id")
    int deleteLimite(@Param("dataExclusao") LocalDateTime dataExclusao, @Param("id") String id);

    Optional<Limite> findByUserAndMesReferencia(User user, YearMonth mesReferencia);

    boolean existsByMesReferenciaAndUserIdAndDataExclusaoIsNull(YearMonth mesReferencia, Long userId);

    List<Limite> findByUserIdAndDataExclusaoIsNull(Long userId);
}
