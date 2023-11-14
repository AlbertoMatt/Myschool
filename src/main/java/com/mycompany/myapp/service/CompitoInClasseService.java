package com.mycompany.myapp.service;

import com.mycompany.myapp.service.dto.CompitoInClasseDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.mycompany.myapp.domain.CompitoInClasse}.
 */
public interface CompitoInClasseService {
    /**
     * Save a compitoInClasse.
     *
     * @param compitoInClasseDTO the entity to save.
     * @return the persisted entity.
     */
    CompitoInClasseDTO save(CompitoInClasseDTO compitoInClasseDTO);

    /**
     * Updates a compitoInClasse.
     *
     * @param compitoInClasseDTO the entity to update.
     * @return the persisted entity.
     */
    CompitoInClasseDTO update(CompitoInClasseDTO compitoInClasseDTO);

    /**
     * Partially updates a compitoInClasse.
     *
     * @param compitoInClasseDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<CompitoInClasseDTO> partialUpdate(CompitoInClasseDTO compitoInClasseDTO);

    /**
     * Get all the compitoInClasses.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<CompitoInClasseDTO> findAll(Pageable pageable);

    /**
     * Get the "id" compitoInClasse.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<CompitoInClasseDTO> findOne(Long id);

    /**
     * Delete the "id" compitoInClasse.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
