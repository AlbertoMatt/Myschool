package com.mycompany.myapp.web.rest;

import static com.mycompany.myapp.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.CompitoInClasse;
import com.mycompany.myapp.domain.enumeration.Materia;
import com.mycompany.myapp.repository.CompitoInClasseRepository;
import com.mycompany.myapp.service.dto.CompitoInClasseDTO;
import com.mycompany.myapp.service.mapper.CompitoInClasseMapper;
import jakarta.persistence.EntityManager;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link CompitoInClasseResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class CompitoInClasseResourceIT {

    private static final Materia DEFAULT_MATERIA = Materia.STORIA;
    private static final Materia UPDATED_MATERIA = Materia.ITALIANO;

    private static final LocalDate DEFAULT_DATA = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATA = LocalDate.now(ZoneId.systemDefault());

    private static final ZonedDateTime DEFAULT_DATA_RESTITUIZIONE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_DATA_RESTITUIZIONE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final String ENTITY_API_URL = "/api/compito-in-classes";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private CompitoInClasseRepository compitoInClasseRepository;

    @Autowired
    private CompitoInClasseMapper compitoInClasseMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCompitoInClasseMockMvc;

    private CompitoInClasse compitoInClasse;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CompitoInClasse createEntity(EntityManager em) {
        CompitoInClasse compitoInClasse = new CompitoInClasse()
            .materia(DEFAULT_MATERIA)
            .data(DEFAULT_DATA)
            .dataRestituizione(DEFAULT_DATA_RESTITUIZIONE);
        return compitoInClasse;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CompitoInClasse createUpdatedEntity(EntityManager em) {
        CompitoInClasse compitoInClasse = new CompitoInClasse()
            .materia(UPDATED_MATERIA)
            .data(UPDATED_DATA)
            .dataRestituizione(UPDATED_DATA_RESTITUIZIONE);
        return compitoInClasse;
    }

    @BeforeEach
    public void initTest() {
        compitoInClasse = createEntity(em);
    }

    @Test
    @Transactional
    void createCompitoInClasse() throws Exception {
        int databaseSizeBeforeCreate = compitoInClasseRepository.findAll().size();
        // Create the CompitoInClasse
        CompitoInClasseDTO compitoInClasseDTO = compitoInClasseMapper.toDto(compitoInClasse);
        restCompitoInClasseMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(compitoInClasseDTO))
            )
            .andExpect(status().isCreated());

        // Validate the CompitoInClasse in the database
        List<CompitoInClasse> compitoInClasseList = compitoInClasseRepository.findAll();
        assertThat(compitoInClasseList).hasSize(databaseSizeBeforeCreate + 1);
        CompitoInClasse testCompitoInClasse = compitoInClasseList.get(compitoInClasseList.size() - 1);
        assertThat(testCompitoInClasse.getMateria()).isEqualTo(DEFAULT_MATERIA);
        assertThat(testCompitoInClasse.getData()).isEqualTo(DEFAULT_DATA);
        assertThat(testCompitoInClasse.getDataRestituizione()).isEqualTo(DEFAULT_DATA_RESTITUIZIONE);
    }

    @Test
    @Transactional
    void createCompitoInClasseWithExistingId() throws Exception {
        // Create the CompitoInClasse with an existing ID
        compitoInClasse.setId(1L);
        CompitoInClasseDTO compitoInClasseDTO = compitoInClasseMapper.toDto(compitoInClasse);

        int databaseSizeBeforeCreate = compitoInClasseRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCompitoInClasseMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(compitoInClasseDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CompitoInClasse in the database
        List<CompitoInClasse> compitoInClasseList = compitoInClasseRepository.findAll();
        assertThat(compitoInClasseList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkMateriaIsRequired() throws Exception {
        int databaseSizeBeforeTest = compitoInClasseRepository.findAll().size();
        // set the field null
        compitoInClasse.setMateria(null);

        // Create the CompitoInClasse, which fails.
        CompitoInClasseDTO compitoInClasseDTO = compitoInClasseMapper.toDto(compitoInClasse);

        restCompitoInClasseMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(compitoInClasseDTO))
            )
            .andExpect(status().isBadRequest());

        List<CompitoInClasse> compitoInClasseList = compitoInClasseRepository.findAll();
        assertThat(compitoInClasseList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDataIsRequired() throws Exception {
        int databaseSizeBeforeTest = compitoInClasseRepository.findAll().size();
        // set the field null
        compitoInClasse.setData(null);

        // Create the CompitoInClasse, which fails.
        CompitoInClasseDTO compitoInClasseDTO = compitoInClasseMapper.toDto(compitoInClasse);

        restCompitoInClasseMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(compitoInClasseDTO))
            )
            .andExpect(status().isBadRequest());

        List<CompitoInClasse> compitoInClasseList = compitoInClasseRepository.findAll();
        assertThat(compitoInClasseList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllCompitoInClasses() throws Exception {
        // Initialize the database
        compitoInClasseRepository.saveAndFlush(compitoInClasse);

        // Get all the compitoInClasseList
        restCompitoInClasseMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(compitoInClasse.getId().intValue())))
            .andExpect(jsonPath("$.[*].materia").value(hasItem(DEFAULT_MATERIA.toString())))
            .andExpect(jsonPath("$.[*].data").value(hasItem(DEFAULT_DATA.toString())))
            .andExpect(jsonPath("$.[*].dataRestituizione").value(hasItem(sameInstant(DEFAULT_DATA_RESTITUIZIONE))));
    }

    @Test
    @Transactional
    void getCompitoInClasse() throws Exception {
        // Initialize the database
        compitoInClasseRepository.saveAndFlush(compitoInClasse);

        // Get the compitoInClasse
        restCompitoInClasseMockMvc
            .perform(get(ENTITY_API_URL_ID, compitoInClasse.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(compitoInClasse.getId().intValue()))
            .andExpect(jsonPath("$.materia").value(DEFAULT_MATERIA.toString()))
            .andExpect(jsonPath("$.data").value(DEFAULT_DATA.toString()))
            .andExpect(jsonPath("$.dataRestituizione").value(sameInstant(DEFAULT_DATA_RESTITUIZIONE)));
    }

    @Test
    @Transactional
    void getNonExistingCompitoInClasse() throws Exception {
        // Get the compitoInClasse
        restCompitoInClasseMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingCompitoInClasse() throws Exception {
        // Initialize the database
        compitoInClasseRepository.saveAndFlush(compitoInClasse);

        int databaseSizeBeforeUpdate = compitoInClasseRepository.findAll().size();

        // Update the compitoInClasse
        CompitoInClasse updatedCompitoInClasse = compitoInClasseRepository.findById(compitoInClasse.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedCompitoInClasse are not directly saved in db
        em.detach(updatedCompitoInClasse);
        updatedCompitoInClasse.materia(UPDATED_MATERIA).data(UPDATED_DATA).dataRestituizione(UPDATED_DATA_RESTITUIZIONE);
        CompitoInClasseDTO compitoInClasseDTO = compitoInClasseMapper.toDto(updatedCompitoInClasse);

        restCompitoInClasseMockMvc
            .perform(
                put(ENTITY_API_URL_ID, compitoInClasseDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(compitoInClasseDTO))
            )
            .andExpect(status().isOk());

        // Validate the CompitoInClasse in the database
        List<CompitoInClasse> compitoInClasseList = compitoInClasseRepository.findAll();
        assertThat(compitoInClasseList).hasSize(databaseSizeBeforeUpdate);
        CompitoInClasse testCompitoInClasse = compitoInClasseList.get(compitoInClasseList.size() - 1);
        assertThat(testCompitoInClasse.getMateria()).isEqualTo(UPDATED_MATERIA);
        assertThat(testCompitoInClasse.getData()).isEqualTo(UPDATED_DATA);
        assertThat(testCompitoInClasse.getDataRestituizione()).isEqualTo(UPDATED_DATA_RESTITUIZIONE);
    }

    @Test
    @Transactional
    void putNonExistingCompitoInClasse() throws Exception {
        int databaseSizeBeforeUpdate = compitoInClasseRepository.findAll().size();
        compitoInClasse.setId(longCount.incrementAndGet());

        // Create the CompitoInClasse
        CompitoInClasseDTO compitoInClasseDTO = compitoInClasseMapper.toDto(compitoInClasse);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCompitoInClasseMockMvc
            .perform(
                put(ENTITY_API_URL_ID, compitoInClasseDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(compitoInClasseDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CompitoInClasse in the database
        List<CompitoInClasse> compitoInClasseList = compitoInClasseRepository.findAll();
        assertThat(compitoInClasseList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCompitoInClasse() throws Exception {
        int databaseSizeBeforeUpdate = compitoInClasseRepository.findAll().size();
        compitoInClasse.setId(longCount.incrementAndGet());

        // Create the CompitoInClasse
        CompitoInClasseDTO compitoInClasseDTO = compitoInClasseMapper.toDto(compitoInClasse);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCompitoInClasseMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(compitoInClasseDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CompitoInClasse in the database
        List<CompitoInClasse> compitoInClasseList = compitoInClasseRepository.findAll();
        assertThat(compitoInClasseList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCompitoInClasse() throws Exception {
        int databaseSizeBeforeUpdate = compitoInClasseRepository.findAll().size();
        compitoInClasse.setId(longCount.incrementAndGet());

        // Create the CompitoInClasse
        CompitoInClasseDTO compitoInClasseDTO = compitoInClasseMapper.toDto(compitoInClasse);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCompitoInClasseMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(compitoInClasseDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the CompitoInClasse in the database
        List<CompitoInClasse> compitoInClasseList = compitoInClasseRepository.findAll();
        assertThat(compitoInClasseList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCompitoInClasseWithPatch() throws Exception {
        // Initialize the database
        compitoInClasseRepository.saveAndFlush(compitoInClasse);

        int databaseSizeBeforeUpdate = compitoInClasseRepository.findAll().size();

        // Update the compitoInClasse using partial update
        CompitoInClasse partialUpdatedCompitoInClasse = new CompitoInClasse();
        partialUpdatedCompitoInClasse.setId(compitoInClasse.getId());

        partialUpdatedCompitoInClasse.dataRestituizione(UPDATED_DATA_RESTITUIZIONE);

        restCompitoInClasseMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCompitoInClasse.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCompitoInClasse))
            )
            .andExpect(status().isOk());

        // Validate the CompitoInClasse in the database
        List<CompitoInClasse> compitoInClasseList = compitoInClasseRepository.findAll();
        assertThat(compitoInClasseList).hasSize(databaseSizeBeforeUpdate);
        CompitoInClasse testCompitoInClasse = compitoInClasseList.get(compitoInClasseList.size() - 1);
        assertThat(testCompitoInClasse.getMateria()).isEqualTo(DEFAULT_MATERIA);
        assertThat(testCompitoInClasse.getData()).isEqualTo(DEFAULT_DATA);
        assertThat(testCompitoInClasse.getDataRestituizione()).isEqualTo(UPDATED_DATA_RESTITUIZIONE);
    }

    @Test
    @Transactional
    void fullUpdateCompitoInClasseWithPatch() throws Exception {
        // Initialize the database
        compitoInClasseRepository.saveAndFlush(compitoInClasse);

        int databaseSizeBeforeUpdate = compitoInClasseRepository.findAll().size();

        // Update the compitoInClasse using partial update
        CompitoInClasse partialUpdatedCompitoInClasse = new CompitoInClasse();
        partialUpdatedCompitoInClasse.setId(compitoInClasse.getId());

        partialUpdatedCompitoInClasse.materia(UPDATED_MATERIA).data(UPDATED_DATA).dataRestituizione(UPDATED_DATA_RESTITUIZIONE);

        restCompitoInClasseMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCompitoInClasse.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCompitoInClasse))
            )
            .andExpect(status().isOk());

        // Validate the CompitoInClasse in the database
        List<CompitoInClasse> compitoInClasseList = compitoInClasseRepository.findAll();
        assertThat(compitoInClasseList).hasSize(databaseSizeBeforeUpdate);
        CompitoInClasse testCompitoInClasse = compitoInClasseList.get(compitoInClasseList.size() - 1);
        assertThat(testCompitoInClasse.getMateria()).isEqualTo(UPDATED_MATERIA);
        assertThat(testCompitoInClasse.getData()).isEqualTo(UPDATED_DATA);
        assertThat(testCompitoInClasse.getDataRestituizione()).isEqualTo(UPDATED_DATA_RESTITUIZIONE);
    }

    @Test
    @Transactional
    void patchNonExistingCompitoInClasse() throws Exception {
        int databaseSizeBeforeUpdate = compitoInClasseRepository.findAll().size();
        compitoInClasse.setId(longCount.incrementAndGet());

        // Create the CompitoInClasse
        CompitoInClasseDTO compitoInClasseDTO = compitoInClasseMapper.toDto(compitoInClasse);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCompitoInClasseMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, compitoInClasseDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(compitoInClasseDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CompitoInClasse in the database
        List<CompitoInClasse> compitoInClasseList = compitoInClasseRepository.findAll();
        assertThat(compitoInClasseList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCompitoInClasse() throws Exception {
        int databaseSizeBeforeUpdate = compitoInClasseRepository.findAll().size();
        compitoInClasse.setId(longCount.incrementAndGet());

        // Create the CompitoInClasse
        CompitoInClasseDTO compitoInClasseDTO = compitoInClasseMapper.toDto(compitoInClasse);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCompitoInClasseMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(compitoInClasseDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CompitoInClasse in the database
        List<CompitoInClasse> compitoInClasseList = compitoInClasseRepository.findAll();
        assertThat(compitoInClasseList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCompitoInClasse() throws Exception {
        int databaseSizeBeforeUpdate = compitoInClasseRepository.findAll().size();
        compitoInClasse.setId(longCount.incrementAndGet());

        // Create the CompitoInClasse
        CompitoInClasseDTO compitoInClasseDTO = compitoInClasseMapper.toDto(compitoInClasse);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCompitoInClasseMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(compitoInClasseDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the CompitoInClasse in the database
        List<CompitoInClasse> compitoInClasseList = compitoInClasseRepository.findAll();
        assertThat(compitoInClasseList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCompitoInClasse() throws Exception {
        // Initialize the database
        compitoInClasseRepository.saveAndFlush(compitoInClasse);

        int databaseSizeBeforeDelete = compitoInClasseRepository.findAll().size();

        // Delete the compitoInClasse
        restCompitoInClasseMockMvc
            .perform(delete(ENTITY_API_URL_ID, compitoInClasse.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<CompitoInClasse> compitoInClasseList = compitoInClasseRepository.findAll();
        assertThat(compitoInClasseList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
