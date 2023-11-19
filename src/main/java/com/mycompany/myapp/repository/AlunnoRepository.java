package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Alunno;
import java.util.List;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Alunno entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AlunnoRepository extends JpaRepository<Alunno, Long> {
    @Query("SELECT DISTINCT a FROM Alunno a LEFT JOIN FETCH a.compitiEseguitis c")
    List<Alunno> findAllWithCompiti();
}
