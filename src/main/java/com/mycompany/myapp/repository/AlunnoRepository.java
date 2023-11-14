package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Alunno;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Alunno entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AlunnoRepository extends JpaRepository<Alunno, Long> {}
