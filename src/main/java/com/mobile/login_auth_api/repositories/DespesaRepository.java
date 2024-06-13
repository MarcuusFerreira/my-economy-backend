package com.mobile.login_auth_api.repositories;

import com.mobile.login_auth_api.domain.despesa.Despesa;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.List;
import java.util.Optional;

@Repository
public interface DespesaRepository extends JpaRepository<Despesa, String> {

    @Query("SELECT e FROM Despesa e WHERE e.user.email = :email AND e.dataExclusao IS NOT NULL")
    List<Despesa> findByEmailAndNotNullDataExclusao(@Param("email") String email);

    @Query("SELECT e from Despesa e where e.user.email = :email AND e.mesReferencia = :mesAno AND e.dataExclusao IS NOT NULL")
    List<Despesa> findByEmailAndMesAndNotNullDataExclusao(@Param("email")String email, @Param("mesAno") YearMonth mesAno);

    @Query("SELECT e FROM Despesa e WHERE e.id = :id AND e.dataExclusao IS NOT NULL")
    Optional<Despesa> findByIdAndNotNullDataExclusao(@Param("id") String id);

    @Modifying
    @Transactional
    @Query("UPDATE Despesa d SET d.dataExclusao = :dataExclusao WHERE d.id = :id")
    int deleteDespesa(@Param("dataExclusao") LocalDateTime dataExclusao, @Param("id") String id);

}