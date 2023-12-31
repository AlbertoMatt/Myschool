package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.repository.ClasseRepository;
import com.mycompany.myapp.service.ClasseService;
import com.mycompany.myapp.service.dto.ClasseDTO;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.Classe}.
 */
@RestController
@RequestMapping("/api/classes")
public class ClasseResource {

    private final Logger log = LoggerFactory.getLogger(ClasseResource.class);

    private static final String ENTITY_NAME = "classe";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ClasseService classeService;

    private final ClasseRepository classeRepository;

    public ClasseResource(ClasseService classeService, ClasseRepository classeRepository) {
        this.classeService = classeService;
        this.classeRepository = classeRepository;
    }

    /**
     * {@code POST  /classes} : Create a new classe.
     *
     * @param classeDTO the classeDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new classeDTO, or with status {@code 400 (Bad Request)} if the classe has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<ClasseDTO> createClasse(@Valid @RequestBody ClasseDTO classeDTO) throws URISyntaxException {
        log.debug("REST request to save Classe : {}", classeDTO);
        if (classeDTO.getId() != null) {
            throw new BadRequestAlertException("A new classe cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ClasseDTO result = classeService.save(classeDTO);
        return ResponseEntity
            .created(new URI("/api/classes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /classes/:id} : Updates an existing classe.
     *
     * @param id the id of the classeDTO to save.
     * @param classeDTO the classeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated classeDTO,
     * or with status {@code 400 (Bad Request)} if the classeDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the classeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ClasseDTO> updateClasse(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ClasseDTO classeDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Classe : {}, {}", id, classeDTO);
        if (classeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, classeDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!classeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ClasseDTO result = classeService.update(classeDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, classeDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /classes/:id} : Partial updates given fields of an existing classe, field will ignore if it is null
     *
     * @param id the id of the classeDTO to save.
     * @param classeDTO the classeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated classeDTO,
     * or with status {@code 400 (Bad Request)} if the classeDTO is not valid,
     * or with status {@code 404 (Not Found)} if the classeDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the classeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ClasseDTO> partialUpdateClasse(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ClasseDTO classeDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Classe partially : {}, {}", id, classeDTO);
        if (classeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, classeDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!classeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ClasseDTO> result = classeService.partialUpdate(classeDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, classeDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /classes} : get all the classes.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of classes in body.
     */
    @GetMapping("")
    public ResponseEntity<List<ClasseDTO>> getAllClasses(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of Classes");
        Page<ClasseDTO> page = classeService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /classes/:id} : get the "id" classe.
     *
     * @param id the id of the classeDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the classeDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ClasseDTO> getClasse(@PathVariable Long id) {
        log.debug("REST request to get Classe : {}", id);
        Optional<ClasseDTO> classeDTO = classeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(classeDTO);
    }

    /**
     * {@code DELETE  /classes/:id} : delete the "id" classe.
     *
     * @param id the id of the classeDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteClasse(@PathVariable Long id) {
        log.debug("REST request to delete Classe : {}", id);
        classeService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
