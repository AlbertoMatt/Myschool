package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.AlunnoCompito;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the AlunnoCompito entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AlunnoCompitoRepository extends JpaRepository<AlunnoCompito, Long> {}
