package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.repository.CompitoInClasseRepository;
import com.mycompany.myapp.service.CompitoInClasseService;
import com.mycompany.myapp.service.dto.AlunnoDTO;
import com.mycompany.myapp.service.dto.CompitoInClasseDTO;
import com.mycompany.myapp.web.rest.errors.BadRequestAlertException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.mycompany.myapp.domain.CompitoInClasse}.
 */
@RestController
@RequestMapping("/api/compito-in-classes")
public class CompitoInClasseResource {

    private final Logger log = LoggerFactory.getLogger(CompitoInClasseResource.class);

    private static final String ENTITY_NAME = "compitoInClasse";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CompitoInClasseService compitoInClasseService;

    private final CompitoInClasseRepository compitoInClasseRepository;

    public CompitoInClasseResource(CompitoInClasseService compitoInClasseService, CompitoInClasseRepository compitoInClasseRepository) {
        this.compitoInClasseService = compitoInClasseService;
        this.compitoInClasseRepository = compitoInClasseRepository;
    }

    /**
     * {@code POST  /compito-in-classes} : Create a new compitoInClasse.
     *
     * @param compitoInClasseDTO the compitoInClasseDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new compitoInClasseDTO, or with status {@code 400 (Bad Request)} if the compitoInClasse has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<CompitoInClasseDTO> createCompitoInClasse(@Valid @RequestBody CompitoInClasseDTO compitoInClasseDTO)
        throws URISyntaxException {
        log.debug("REST request to save CompitoInClasse : {}", compitoInClasseDTO);
        if (compitoInClasseDTO.getId() != null) {
            throw new BadRequestAlertException("A new compitoInClasse cannot already have an ID", ENTITY_NAME, "idexists");
        }
        CompitoInClasseDTO result = compitoInClasseService.save(compitoInClasseDTO);
        return ResponseEntity
            .created(new URI("/api/compito-in-classes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /compito-in-classes/:id} : Updates an existing compitoInClasse.
     *
     * @param id the id of the compitoInClasseDTO to save.
     * @param compitoInClasseDTO the compitoInClasseDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated compitoInClasseDTO,
     * or with status {@code 400 (Bad Request)} if the compitoInClasseDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the compitoInClasseDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<CompitoInClasseDTO> updateCompitoInClasse(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody CompitoInClasseDTO compitoInClasseDTO
    ) throws URISyntaxException {
        log.debug("REST request to update CompitoInClasse : {}, {}", id, compitoInClasseDTO);
        if (compitoInClasseDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, compitoInClasseDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!compitoInClasseRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        CompitoInClasseDTO result = compitoInClasseService.update(compitoInClasseDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, compitoInClasseDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /compito-in-classes/:id} : Partial updates given fields of an existing compitoInClasse, field will ignore if it is null
     *
     * @param id the id of the compitoInClasseDTO to save.
     * @param compitoInClasseDTO the compitoInClasseDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated compitoInClasseDTO,
     * or with status {@code 400 (Bad Request)} if the compitoInClasseDTO is not valid,
     * or with status {@code 404 (Not Found)} if the compitoInClasseDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the compitoInClasseDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<CompitoInClasseDTO> partialUpdateCompitoInClasse(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody CompitoInClasseDTO compitoInClasseDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update CompitoInClasse partially : {}, {}", id, compitoInClasseDTO);
        if (compitoInClasseDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, compitoInClasseDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!compitoInClasseRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<CompitoInClasseDTO> result = compitoInClasseService.partialUpdate(compitoInClasseDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, compitoInClasseDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /compito-in-classes} : get all the compitoInClasses.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of compitoInClasses in body.
     */
    @GetMapping("")
    public ResponseEntity<List<CompitoInClasseDTO>> getAllCompitoInClasses(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get a page of CompitoInClasses");
        Page<CompitoInClasseDTO> page = compitoInClasseService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /compito-in-classes/:id} : get the "id" compitoInClasse.
     *
     * @param id the id of the compitoInClasseDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the compitoInClasseDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<CompitoInClasseDTO> getCompitoInClasse(@PathVariable Long id) {
        log.debug("REST request to get CompitoInClasse : {}", id);
        Optional<CompitoInClasseDTO> compitoInClasseDTO = compitoInClasseService.findOne(id);
        return ResponseUtil.wrapOrNotFound(compitoInClasseDTO);
    }

    /**
     * {@code DELETE  /compito-in-classes/:id} : delete the "id" compitoInClasse.
     *
     * @param id the id of the compitoInClasseDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCompitoInClasse(@PathVariable Long id) {
        log.debug("REST request to delete CompitoInClasse : {}", id);
        compitoInClasseService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }

    @PatchMapping("/{id}/valorizza-data-restituzione")
    public ResponseEntity<CompitoInClasseDTO> valorizzaDataRestituzione(
        @PathVariable Long id,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) ZonedDateTime dataRestituizione
    ) {
        Optional<CompitoInClasseDTO> optionalCompito = compitoInClasseService.findOne(id);

        if (optionalCompito.isPresent()) {
            CompitoInClasseDTO compitoInClasseDTO = optionalCompito.get();
            compitoInClasseDTO.setDataRestituizione(dataRestituizione);

            CompitoInClasseDTO updatedCompitoDTO = compitoInClasseService.save(compitoInClasseDTO);

            return ResponseEntity.ok().body(updatedCompitoDTO);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{id}/alunni-risultato-superiore")
    public ResponseEntity<List<AlunnoDTO>> getAlunniRisultatoSuperiore(@PathVariable Long id, @RequestParam Double valoreMinimo) {
        List<AlunnoDTO> alunni = compitoInClasseService.findAlunniByCompitoAndRisultatoSuperiore(id, valoreMinimo);
        return ResponseEntity.ok().body(alunni);
    }
}
