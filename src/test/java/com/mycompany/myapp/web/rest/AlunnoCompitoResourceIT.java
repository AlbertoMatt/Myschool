package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.Alunno;
import com.mycompany.myapp.domain.AlunnoCompito;
import com.mycompany.myapp.domain.CompitoInClasse;
import com.mycompany.myapp.repository.AlunnoCompitoRepository;
import com.mycompany.myapp.service.dto.AlunnoCompitoDTO;
import com.mycompany.myapp.service.mapper.AlunnoCompitoMapper;
import jakarta.persistence.EntityManager;
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
 * Integration tests for the {@link AlunnoCompitoResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class AlunnoCompitoResourceIT {

    private static final Double DEFAULT_RISULTATO_NUMERICO = 0D;
    private static final Double UPDATED_RISULTATO_NUMERICO = 1D;

    private static final String ENTITY_API_URL = "/api/alunno-compitos";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private AlunnoCompitoRepository alunnoCompitoRepository;

    @Autowired
    private AlunnoCompitoMapper alunnoCompitoMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAlunnoCompitoMockMvc;

    private AlunnoCompito alunnoCompito;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AlunnoCompito createEntity(EntityManager em) {
        AlunnoCompito alunnoCompito = new AlunnoCompito().risultatoNumerico(DEFAULT_RISULTATO_NUMERICO);
        // Add required entity
        Alunno alunno;
        if (TestUtil.findAll(em, Alunno.class).isEmpty()) {
            alunno = AlunnoResourceIT.createEntity(em);
            em.persist(alunno);
            em.flush();
        } else {
            alunno = TestUtil.findAll(em, Alunno.class).get(0);
        }
        alunnoCompito.setAlunno(alunno);
        // Add required entity
        CompitoInClasse compitoInClasse;
        if (TestUtil.findAll(em, CompitoInClasse.class).isEmpty()) {
            compitoInClasse = CompitoInClasseResourceIT.createEntity(em);
            em.persist(compitoInClasse);
            em.flush();
        } else {
            compitoInClasse = TestUtil.findAll(em, CompitoInClasse.class).get(0);
        }
        alunnoCompito.setCompito(compitoInClasse);
        return alunnoCompito;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AlunnoCompito createUpdatedEntity(EntityManager em) {
        AlunnoCompito alunnoCompito = new AlunnoCompito().risultatoNumerico(UPDATED_RISULTATO_NUMERICO);
        // Add required entity
        Alunno alunno;
        if (TestUtil.findAll(em, Alunno.class).isEmpty()) {
            alunno = AlunnoResourceIT.createUpdatedEntity(em);
            em.persist(alunno);
            em.flush();
        } else {
            alunno = TestUtil.findAll(em, Alunno.class).get(0);
        }
        alunnoCompito.setAlunno(alunno);
        // Add required entity
        CompitoInClasse compitoInClasse;
        if (TestUtil.findAll(em, CompitoInClasse.class).isEmpty()) {
            compitoInClasse = CompitoInClasseResourceIT.createUpdatedEntity(em);
            em.persist(compitoInClasse);
            em.flush();
        } else {
            compitoInClasse = TestUtil.findAll(em, CompitoInClasse.class).get(0);
        }
        alunnoCompito.setCompito(compitoInClasse);
        return alunnoCompito;
    }

    @BeforeEach
    public void initTest() {
        alunnoCompito = createEntity(em);
    }

    @Test
    @Transactional
    void createAlunnoCompito() throws Exception {
        int databaseSizeBeforeCreate = alunnoCompitoRepository.findAll().size();
        // Create the AlunnoCompito
        AlunnoCompitoDTO alunnoCompitoDTO = alunnoCompitoMapper.toDto(alunnoCompito);
        restAlunnoCompitoMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(alunnoCompitoDTO))
            )
            .andExpect(status().isCreated());

        // Validate the AlunnoCompito in the database
        List<AlunnoCompito> alunnoCompitoList = alunnoCompitoRepository.findAll();
        assertThat(alunnoCompitoList).hasSize(databaseSizeBeforeCreate + 1);
        AlunnoCompito testAlunnoCompito = alunnoCompitoList.get(alunnoCompitoList.size() - 1);
        assertThat(testAlunnoCompito.getRisultatoNumerico()).isEqualTo(DEFAULT_RISULTATO_NUMERICO);
    }

    @Test
    @Transactional
    void createAlunnoCompitoWithExistingId() throws Exception {
        // Create the AlunnoCompito with an existing ID
        alunnoCompito.setId(1L);
        AlunnoCompitoDTO alunnoCompitoDTO = alunnoCompitoMapper.toDto(alunnoCompito);

        int databaseSizeBeforeCreate = alunnoCompitoRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restAlunnoCompitoMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(alunnoCompitoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AlunnoCompito in the database
        List<AlunnoCompito> alunnoCompitoList = alunnoCompitoRepository.findAll();
        assertThat(alunnoCompitoList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkRisultatoNumericoIsRequired() throws Exception {
        int databaseSizeBeforeTest = alunnoCompitoRepository.findAll().size();
        // set the field null
        alunnoCompito.setRisultatoNumerico(null);

        // Create the AlunnoCompito, which fails.
        AlunnoCompitoDTO alunnoCompitoDTO = alunnoCompitoMapper.toDto(alunnoCompito);

        restAlunnoCompitoMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(alunnoCompitoDTO))
            )
            .andExpect(status().isBadRequest());

        List<AlunnoCompito> alunnoCompitoList = alunnoCompitoRepository.findAll();
        assertThat(alunnoCompitoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllAlunnoCompitos() throws Exception {
        // Initialize the database
        alunnoCompitoRepository.saveAndFlush(alunnoCompito);

        // Get all the alunnoCompitoList
        restAlunnoCompitoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(alunnoCompito.getId().intValue())))
            .andExpect(jsonPath("$.[*].risultatoNumerico").value(hasItem(DEFAULT_RISULTATO_NUMERICO.doubleValue())));
    }

    @Test
    @Transactional
    void getAlunnoCompito() throws Exception {
        // Initialize the database
        alunnoCompitoRepository.saveAndFlush(alunnoCompito);

        // Get the alunnoCompito
        restAlunnoCompitoMockMvc
            .perform(get(ENTITY_API_URL_ID, alunnoCompito.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(alunnoCompito.getId().intValue()))
            .andExpect(jsonPath("$.risultatoNumerico").value(DEFAULT_RISULTATO_NUMERICO.doubleValue()));
    }

    @Test
    @Transactional
    void getNonExistingAlunnoCompito() throws Exception {
        // Get the alunnoCompito
        restAlunnoCompitoMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingAlunnoCompito() throws Exception {
        // Initialize the database
        alunnoCompitoRepository.saveAndFlush(alunnoCompito);

        int databaseSizeBeforeUpdate = alunnoCompitoRepository.findAll().size();

        // Update the alunnoCompito
        AlunnoCompito updatedAlunnoCompito = alunnoCompitoRepository.findById(alunnoCompito.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedAlunnoCompito are not directly saved in db
        em.detach(updatedAlunnoCompito);
        updatedAlunnoCompito.risultatoNumerico(UPDATED_RISULTATO_NUMERICO);
        AlunnoCompitoDTO alunnoCompitoDTO = alunnoCompitoMapper.toDto(updatedAlunnoCompito);

        restAlunnoCompitoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, alunnoCompitoDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(alunnoCompitoDTO))
            )
            .andExpect(status().isOk());

        // Validate the AlunnoCompito in the database
        List<AlunnoCompito> alunnoCompitoList = alunnoCompitoRepository.findAll();
        assertThat(alunnoCompitoList).hasSize(databaseSizeBeforeUpdate);
        AlunnoCompito testAlunnoCompito = alunnoCompitoList.get(alunnoCompitoList.size() - 1);
        assertThat(testAlunnoCompito.getRisultatoNumerico()).isEqualTo(UPDATED_RISULTATO_NUMERICO);
    }

    @Test
    @Transactional
    void putNonExistingAlunnoCompito() throws Exception {
        int databaseSizeBeforeUpdate = alunnoCompitoRepository.findAll().size();
        alunnoCompito.setId(longCount.incrementAndGet());

        // Create the AlunnoCompito
        AlunnoCompitoDTO alunnoCompitoDTO = alunnoCompitoMapper.toDto(alunnoCompito);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAlunnoCompitoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, alunnoCompitoDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(alunnoCompitoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AlunnoCompito in the database
        List<AlunnoCompito> alunnoCompitoList = alunnoCompitoRepository.findAll();
        assertThat(alunnoCompitoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchAlunnoCompito() throws Exception {
        int databaseSizeBeforeUpdate = alunnoCompitoRepository.findAll().size();
        alunnoCompito.setId(longCount.incrementAndGet());

        // Create the AlunnoCompito
        AlunnoCompitoDTO alunnoCompitoDTO = alunnoCompitoMapper.toDto(alunnoCompito);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAlunnoCompitoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(alunnoCompitoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AlunnoCompito in the database
        List<AlunnoCompito> alunnoCompitoList = alunnoCompitoRepository.findAll();
        assertThat(alunnoCompitoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAlunnoCompito() throws Exception {
        int databaseSizeBeforeUpdate = alunnoCompitoRepository.findAll().size();
        alunnoCompito.setId(longCount.incrementAndGet());

        // Create the AlunnoCompito
        AlunnoCompitoDTO alunnoCompitoDTO = alunnoCompitoMapper.toDto(alunnoCompito);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAlunnoCompitoMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(alunnoCompitoDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the AlunnoCompito in the database
        List<AlunnoCompito> alunnoCompitoList = alunnoCompitoRepository.findAll();
        assertThat(alunnoCompitoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateAlunnoCompitoWithPatch() throws Exception {
        // Initialize the database
        alunnoCompitoRepository.saveAndFlush(alunnoCompito);

        int databaseSizeBeforeUpdate = alunnoCompitoRepository.findAll().size();

        // Update the alunnoCompito using partial update
        AlunnoCompito partialUpdatedAlunnoCompito = new AlunnoCompito();
        partialUpdatedAlunnoCompito.setId(alunnoCompito.getId());

        restAlunnoCompitoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAlunnoCompito.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAlunnoCompito))
            )
            .andExpect(status().isOk());

        // Validate the AlunnoCompito in the database
        List<AlunnoCompito> alunnoCompitoList = alunnoCompitoRepository.findAll();
        assertThat(alunnoCompitoList).hasSize(databaseSizeBeforeUpdate);
        AlunnoCompito testAlunnoCompito = alunnoCompitoList.get(alunnoCompitoList.size() - 1);
        assertThat(testAlunnoCompito.getRisultatoNumerico()).isEqualTo(DEFAULT_RISULTATO_NUMERICO);
    }

    @Test
    @Transactional
    void fullUpdateAlunnoCompitoWithPatch() throws Exception {
        // Initialize the database
        alunnoCompitoRepository.saveAndFlush(alunnoCompito);

        int databaseSizeBeforeUpdate = alunnoCompitoRepository.findAll().size();

        // Update the alunnoCompito using partial update
        AlunnoCompito partialUpdatedAlunnoCompito = new AlunnoCompito();
        partialUpdatedAlunnoCompito.setId(alunnoCompito.getId());

        partialUpdatedAlunnoCompito.risultatoNumerico(UPDATED_RISULTATO_NUMERICO);

        restAlunnoCompitoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAlunnoCompito.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAlunnoCompito))
            )
            .andExpect(status().isOk());

        // Validate the AlunnoCompito in the database
        List<AlunnoCompito> alunnoCompitoList = alunnoCompitoRepository.findAll();
        assertThat(alunnoCompitoList).hasSize(databaseSizeBeforeUpdate);
        AlunnoCompito testAlunnoCompito = alunnoCompitoList.get(alunnoCompitoList.size() - 1);
        assertThat(testAlunnoCompito.getRisultatoNumerico()).isEqualTo(UPDATED_RISULTATO_NUMERICO);
    }

    @Test
    @Transactional
    void patchNonExistingAlunnoCompito() throws Exception {
        int databaseSizeBeforeUpdate = alunnoCompitoRepository.findAll().size();
        alunnoCompito.setId(longCount.incrementAndGet());

        // Create the AlunnoCompito
        AlunnoCompitoDTO alunnoCompitoDTO = alunnoCompitoMapper.toDto(alunnoCompito);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAlunnoCompitoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, alunnoCompitoDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(alunnoCompitoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AlunnoCompito in the database
        List<AlunnoCompito> alunnoCompitoList = alunnoCompitoRepository.findAll();
        assertThat(alunnoCompitoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchAlunnoCompito() throws Exception {
        int databaseSizeBeforeUpdate = alunnoCompitoRepository.findAll().size();
        alunnoCompito.setId(longCount.incrementAndGet());

        // Create the AlunnoCompito
        AlunnoCompitoDTO alunnoCompitoDTO = alunnoCompitoMapper.toDto(alunnoCompito);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAlunnoCompitoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(alunnoCompitoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AlunnoCompito in the database
        List<AlunnoCompito> alunnoCompitoList = alunnoCompitoRepository.findAll();
        assertThat(alunnoCompitoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAlunnoCompito() throws Exception {
        int databaseSizeBeforeUpdate = alunnoCompitoRepository.findAll().size();
        alunnoCompito.setId(longCount.incrementAndGet());

        // Create the AlunnoCompito
        AlunnoCompitoDTO alunnoCompitoDTO = alunnoCompitoMapper.toDto(alunnoCompito);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAlunnoCompitoMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(alunnoCompitoDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the AlunnoCompito in the database
        List<AlunnoCompito> alunnoCompitoList = alunnoCompitoRepository.findAll();
        assertThat(alunnoCompitoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteAlunnoCompito() throws Exception {
        // Initialize the database
        alunnoCompitoRepository.saveAndFlush(alunnoCompito);

        int databaseSizeBeforeDelete = alunnoCompitoRepository.findAll().size();

        // Delete the alunnoCompito
        restAlunnoCompitoMockMvc
            .perform(delete(ENTITY_API_URL_ID, alunnoCompito.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<AlunnoCompito> alunnoCompitoList = alunnoCompitoRepository.findAll();
        assertThat(alunnoCompitoList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
