package com.mycompany.myapp.service;

import com.mycompany.myapp.service.dto.AlunnoDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.mycompany.myapp.domain.Alunno}.
 */
public interface AlunnoService {
    /**
     * Save a alunno.
     *
     * @param alunnoDTO the entity to save.
     * @return the persisted entity.
     */
    AlunnoDTO save(AlunnoDTO alunnoDTO);

    /**
     * Updates a alunno.
     *
     * @param alunnoDTO the entity to update.
     * @return the persisted entity.
     */
    AlunnoDTO update(AlunnoDTO alunnoDTO);

    /**
     * Partially updates a alunno.
     *
     * @param alunnoDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<AlunnoDTO> partialUpdate(AlunnoDTO alunnoDTO);

    /**
     * Get all the alunnos.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<AlunnoDTO> findAll(Pageable pageable);

    /**
     * Get the "id" alunno.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<AlunnoDTO> findOne(Long id);

    /**
     * Delete the "id" alunno.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
