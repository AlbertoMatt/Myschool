package com.mycompany.myapp.service;

import com.mycompany.myapp.service.dto.AlunnoCompitoDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.mycompany.myapp.domain.AlunnoCompito}.
 */
public interface AlunnoCompitoService {
    /**
     * Save a alunnoCompito.
     *
     * @param alunnoCompitoDTO the entity to save.
     * @return the persisted entity.
     */
    AlunnoCompitoDTO save(AlunnoCompitoDTO alunnoCompitoDTO);

    /**
     * Updates a alunnoCompito.
     *
     * @param alunnoCompitoDTO the entity to update.
     * @return the persisted entity.
     */
    AlunnoCompitoDTO update(AlunnoCompitoDTO alunnoCompitoDTO);

    /**
     * Partially updates a alunnoCompito.
     *
     * @param alunnoCompitoDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<AlunnoCompitoDTO> partialUpdate(AlunnoCompitoDTO alunnoCompitoDTO);

    /**
     * Get all the alunnoCompitos.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<AlunnoCompitoDTO> findAll(Pageable pageable);

    /**
     * Get the "id" alunnoCompito.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<AlunnoCompitoDTO> findOne(Long id);

    /**
     * Delete the "id" alunnoCompito.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
