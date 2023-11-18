package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Alunno;
import com.mycompany.myapp.domain.CompitoInClasse;
import java.util.List;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the CompitoInClasse entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CompitoInClasseRepository extends JpaRepository<CompitoInClasse, Long> {
    @Query("SELECT ac.alunno FROM AlunnoCompito ac " + "WHERE ac.compito.id = :compitoId AND ac.risultatoNumerico > :valoreMinimo")
    List<Alunno> findAlunniByCompitoAndRisultatoSuperiore(@Param("compitoId") Long compitoId, @Param("valoreMinimo") Double valoreMinimo);
}
