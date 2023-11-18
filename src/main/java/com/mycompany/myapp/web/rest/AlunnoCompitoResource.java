package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.repository.AlunnoCompitoRepository;
import com.mycompany.myapp.service.AlunnoCompitoService;
import com.mycompany.myapp.service.dto.AlunnoCompitoDTO;
import com.mycompany.myapp.web.rest.errors.BadRequestAlertException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.mycompany.myapp.domain.AlunnoCompito}.
 */
@RestController
@RequestMapping("/api/alunno-compitos")
public class AlunnoCompitoResource {

    private final Logger log = LoggerFactory.getLogger(AlunnoCompitoResource.class);

    private static final String ENTITY_NAME = "alunnoCompito";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AlunnoCompitoService alunnoCompitoService;

    private final AlunnoCompitoRepository alunnoCompitoRepository;

    public AlunnoCompitoResource(AlunnoCompitoService alunnoCompitoService, AlunnoCompitoRepository alunnoCompitoRepository) {
        this.alunnoCompitoService = alunnoCompitoService;
        this.alunnoCompitoRepository = alunnoCompitoRepository;
    }

    /**
     * {@code POST  /alunno-compitos} : Create a new alunnoCompito.
     *
     * @param alunnoCompitoDTO the alunnoCompitoDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new alunnoCompitoDTO, or with status {@code 400 (Bad Request)} if the alunnoCompito has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<AlunnoCompitoDTO> createAlunnoCompito(@Valid @RequestBody AlunnoCompitoDTO alunnoCompitoDTO)
        throws URISyntaxException {
        log.debug("REST request to save AlunnoCompito : {}", alunnoCompitoDTO);
        if (alunnoCompitoDTO.getId() != null) {
            throw new BadRequestAlertException("A new alunnoCompito cannot already have an ID", ENTITY_NAME, "idexists");
        }
        AlunnoCompitoDTO result = alunnoCompitoService.save(alunnoCompitoDTO);
        return ResponseEntity
            .created(new URI("/api/alunno-compitos/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /alunno-compitos/:id} : Updates an existing alunnoCompito.
     *
     * @param id the id of the alunnoCompitoDTO to save.
     * @param alunnoCompitoDTO the alunnoCompitoDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated alunnoCompitoDTO,
     * or with status {@code 400 (Bad Request)} if the alunnoCompitoDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the alunnoCompitoDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<AlunnoCompitoDTO> updateAlunnoCompito(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody AlunnoCompitoDTO alunnoCompitoDTO
    ) throws URISyntaxException {
        log.debug("REST request to update AlunnoCompito : {}, {}", id, alunnoCompitoDTO);
        if (alunnoCompitoDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, alunnoCompitoDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!alunnoCompitoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        AlunnoCompitoDTO result = alunnoCompitoService.update(alunnoCompitoDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, alunnoCompitoDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /alunno-compitos/:id} : Partial updates given fields of an existing alunnoCompito, field will ignore if it is null
     *
     * @param id the id of the alunnoCompitoDTO to save.
     * @param alunnoCompitoDTO the alunnoCompitoDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated alunnoCompitoDTO,
     * or with status {@code 400 (Bad Request)} if the alunnoCompitoDTO is not valid,
     * or with status {@code 404 (Not Found)} if the alunnoCompitoDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the alunnoCompitoDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<AlunnoCompitoDTO> partialUpdateAlunnoCompito(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody AlunnoCompitoDTO alunnoCompitoDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update AlunnoCompito partially : {}, {}", id, alunnoCompitoDTO);
        if (alunnoCompitoDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, alunnoCompitoDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!alunnoCompitoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<AlunnoCompitoDTO> result = alunnoCompitoService.partialUpdate(alunnoCompitoDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, alunnoCompitoDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /alunno-compitos} : get all the alunnoCompitos.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of alunnoCompitos in body.
     */
    @GetMapping("")
    public ResponseEntity<List<AlunnoCompitoDTO>> getAllAlunnoCompitos(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of AlunnoCompitos");
        Page<AlunnoCompitoDTO> page = alunnoCompitoService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /alunno-compitos/:id} : get the "id" alunnoCompito.
     *
     * @param id the id of the alunnoCompitoDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the alunnoCompitoDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<AlunnoCompitoDTO> getAlunnoCompito(@PathVariable Long id) {
        log.debug("REST request to get AlunnoCompito : {}", id);
        Optional<AlunnoCompitoDTO> alunnoCompitoDTO = alunnoCompitoService.findOne(id);
        return ResponseUtil.wrapOrNotFound(alunnoCompitoDTO);
    }

    /**
     * {@code DELETE  /alunno-compitos/:id} : delete the "id" alunnoCompito.
     *
     * @param id the id of the alunnoCompitoDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAlunnoCompito(@PathVariable Long id) {
        log.debug("REST request to delete AlunnoCompito : {}", id);
        alunnoCompitoService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
