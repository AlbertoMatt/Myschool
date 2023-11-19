package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.Alunno;
import com.mycompany.myapp.domain.Classe;
import com.mycompany.myapp.repository.AlunnoRepository;
import com.mycompany.myapp.service.dto.AlunnoDTO;
import com.mycompany.myapp.service.mapper.AlunnoMapper;
import jakarta.persistence.EntityManager;
import java.time.LocalDate;
import java.time.ZoneId;
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
 * Integration tests for the {@link AlunnoResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class AlunnoResourceIT {

    private static final String DEFAULT_NOME = "AAAAAAAAAA";
    private static final String UPDATED_NOME = "BBBBBBBBBB";

    private static final String DEFAULT_COGNOME = "AAAAAAAAAA";
    private static final String UPDATED_COGNOME = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_DATA_NASCITA = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATA_NASCITA = LocalDate.now(ZoneId.systemDefault());

    private static final Double DEFAULT_MEDIA_VOTI = 1D;
    private static final Double UPDATED_MEDIA_VOTI = 2D;

    private static final String ENTITY_API_URL = "/api/alunnos";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private AlunnoRepository alunnoRepository;

    @Autowired
    private AlunnoMapper alunnoMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAlunnoMockMvc;

    private Alunno alunno;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Alunno createEntity(EntityManager em) {
        Alunno alunno = new Alunno()
            .nome(DEFAULT_NOME)
            .cognome(DEFAULT_COGNOME)
            .dataNascita(DEFAULT_DATA_NASCITA)
            .mediaVoti(DEFAULT_MEDIA_VOTI);
        // Add required entity
        Classe classe;
        if (TestUtil.findAll(em, Classe.class).isEmpty()) {
            classe = ClasseResourceIT.createEntity(em);
            em.persist(classe);
            em.flush();
        } else {
            classe = TestUtil.findAll(em, Classe.class).get(0);
        }
        alunno.setClasseDiAppartenenza(classe);
        return alunno;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Alunno createUpdatedEntity(EntityManager em) {
        Alunno alunno = new Alunno()
            .nome(UPDATED_NOME)
            .cognome(UPDATED_COGNOME)
            .dataNascita(UPDATED_DATA_NASCITA)
            .mediaVoti(UPDATED_MEDIA_VOTI);
        // Add required entity
        Classe classe;
        if (TestUtil.findAll(em, Classe.class).isEmpty()) {
            classe = ClasseResourceIT.createUpdatedEntity(em);
            em.persist(classe);
            em.flush();
        } else {
            classe = TestUtil.findAll(em, Classe.class).get(0);
        }
        alunno.setClasseDiAppartenenza(classe);
        return alunno;
    }

    @BeforeEach
    public void initTest() {
        alunno = createEntity(em);
    }

    @Test
    @Transactional
    void createAlunno() throws Exception {
        int databaseSizeBeforeCreate = alunnoRepository.findAll().size();
        // Create the Alunno
        AlunnoDTO alunnoDTO = alunnoMapper.toDto(alunno);
        restAlunnoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(alunnoDTO)))
            .andExpect(status().isCreated());

        // Validate the Alunno in the database
        List<Alunno> alunnoList = alunnoRepository.findAll();
        assertThat(alunnoList).hasSize(databaseSizeBeforeCreate + 1);
        Alunno testAlunno = alunnoList.get(alunnoList.size() - 1);
        assertThat(testAlunno.getNome()).isEqualTo(DEFAULT_NOME);
        assertThat(testAlunno.getCognome()).isEqualTo(DEFAULT_COGNOME);
        assertThat(testAlunno.getDataNascita()).isEqualTo(DEFAULT_DATA_NASCITA);
        assertThat(testAlunno.getMediaVoti()).isEqualTo(DEFAULT_MEDIA_VOTI);
    }

    @Test
    @Transactional
    void createAlunnoWithExistingId() throws Exception {
        // Create the Alunno with an existing ID
        alunno.setId(1L);
        AlunnoDTO alunnoDTO = alunnoMapper.toDto(alunno);

        int databaseSizeBeforeCreate = alunnoRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restAlunnoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(alunnoDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Alunno in the database
        List<Alunno> alunnoList = alunnoRepository.findAll();
        assertThat(alunnoList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNomeIsRequired() throws Exception {
        int databaseSizeBeforeTest = alunnoRepository.findAll().size();
        // set the field null
        alunno.setNome(null);

        // Create the Alunno, which fails.
        AlunnoDTO alunnoDTO = alunnoMapper.toDto(alunno);

        restAlunnoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(alunnoDTO)))
            .andExpect(status().isBadRequest());

        List<Alunno> alunnoList = alunnoRepository.findAll();
        assertThat(alunnoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCognomeIsRequired() throws Exception {
        int databaseSizeBeforeTest = alunnoRepository.findAll().size();
        // set the field null
        alunno.setCognome(null);

        // Create the Alunno, which fails.
        AlunnoDTO alunnoDTO = alunnoMapper.toDto(alunno);

        restAlunnoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(alunnoDTO)))
            .andExpect(status().isBadRequest());

        List<Alunno> alunnoList = alunnoRepository.findAll();
        assertThat(alunnoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDataNascitaIsRequired() throws Exception {
        int databaseSizeBeforeTest = alunnoRepository.findAll().size();
        // set the field null
        alunno.setDataNascita(null);

        // Create the Alunno, which fails.
        AlunnoDTO alunnoDTO = alunnoMapper.toDto(alunno);

        restAlunnoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(alunnoDTO)))
            .andExpect(status().isBadRequest());

        List<Alunno> alunnoList = alunnoRepository.findAll();
        assertThat(alunnoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllAlunnos() throws Exception {
        // Initialize the database
        alunnoRepository.saveAndFlush(alunno);

        // Get all the alunnoList
        restAlunnoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(alunno.getId().intValue())))
            .andExpect(jsonPath("$.[*].nome").value(hasItem(DEFAULT_NOME)))
            .andExpect(jsonPath("$.[*].cognome").value(hasItem(DEFAULT_COGNOME)))
            .andExpect(jsonPath("$.[*].dataNascita").value(hasItem(DEFAULT_DATA_NASCITA.toString())))
            .andExpect(jsonPath("$.[*].mediaVoti").value(hasItem(DEFAULT_MEDIA_VOTI.doubleValue())));
    }

    @Test
    @Transactional
    void getAlunno() throws Exception {
        // Initialize the database
        alunnoRepository.saveAndFlush(alunno);

        // Get the alunno
        restAlunnoMockMvc
            .perform(get(ENTITY_API_URL_ID, alunno.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(alunno.getId().intValue()))
            .andExpect(jsonPath("$.nome").value(DEFAULT_NOME))
            .andExpect(jsonPath("$.cognome").value(DEFAULT_COGNOME))
            .andExpect(jsonPath("$.dataNascita").value(DEFAULT_DATA_NASCITA.toString()))
            .andExpect(jsonPath("$.mediaVoti").value(DEFAULT_MEDIA_VOTI.doubleValue()));
    }

    @Test
    @Transactional
    void getNonExistingAlunno() throws Exception {
        // Get the alunno
        restAlunnoMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingAlunno() throws Exception {
        // Initialize the database
        alunnoRepository.saveAndFlush(alunno);

        int databaseSizeBeforeUpdate = alunnoRepository.findAll().size();

        // Update the alunno
        Alunno updatedAlunno = alunnoRepository.findById(alunno.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedAlunno are not directly saved in db
        em.detach(updatedAlunno);
        updatedAlunno.nome(UPDATED_NOME).cognome(UPDATED_COGNOME).dataNascita(UPDATED_DATA_NASCITA).mediaVoti(UPDATED_MEDIA_VOTI);
        AlunnoDTO alunnoDTO = alunnoMapper.toDto(updatedAlunno);

        restAlunnoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, alunnoDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(alunnoDTO))
            )
            .andExpect(status().isOk());

        // Validate the Alunno in the database
        List<Alunno> alunnoList = alunnoRepository.findAll();
        assertThat(alunnoList).hasSize(databaseSizeBeforeUpdate);
        Alunno testAlunno = alunnoList.get(alunnoList.size() - 1);
        assertThat(testAlunno.getNome()).isEqualTo(UPDATED_NOME);
        assertThat(testAlunno.getCognome()).isEqualTo(UPDATED_COGNOME);
        assertThat(testAlunno.getDataNascita()).isEqualTo(UPDATED_DATA_NASCITA);
        assertThat(testAlunno.getMediaVoti()).isEqualTo(UPDATED_MEDIA_VOTI);
    }

    @Test
    @Transactional
    void putNonExistingAlunno() throws Exception {
        int databaseSizeBeforeUpdate = alunnoRepository.findAll().size();
        alunno.setId(longCount.incrementAndGet());

        // Create the Alunno
        AlunnoDTO alunnoDTO = alunnoMapper.toDto(alunno);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAlunnoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, alunnoDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(alunnoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Alunno in the database
        List<Alunno> alunnoList = alunnoRepository.findAll();
        assertThat(alunnoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchAlunno() throws Exception {
        int databaseSizeBeforeUpdate = alunnoRepository.findAll().size();
        alunno.setId(longCount.incrementAndGet());

        // Create the Alunno
        AlunnoDTO alunnoDTO = alunnoMapper.toDto(alunno);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAlunnoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(alunnoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Alunno in the database
        List<Alunno> alunnoList = alunnoRepository.findAll();
        assertThat(alunnoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAlunno() throws Exception {
        int databaseSizeBeforeUpdate = alunnoRepository.findAll().size();
        alunno.setId(longCount.incrementAndGet());

        // Create the Alunno
        AlunnoDTO alunnoDTO = alunnoMapper.toDto(alunno);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAlunnoMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(alunnoDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Alunno in the database
        List<Alunno> alunnoList = alunnoRepository.findAll();
        assertThat(alunnoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateAlunnoWithPatch() throws Exception {
        // Initialize the database
        alunnoRepository.saveAndFlush(alunno);

        int databaseSizeBeforeUpdate = alunnoRepository.findAll().size();

        // Update the alunno using partial update
        Alunno partialUpdatedAlunno = new Alunno();
        partialUpdatedAlunno.setId(alunno.getId());

        partialUpdatedAlunno.mediaVoti(UPDATED_MEDIA_VOTI);

        restAlunnoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAlunno.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAlunno))
            )
            .andExpect(status().isOk());

        // Validate the Alunno in the database
        List<Alunno> alunnoList = alunnoRepository.findAll();
        assertThat(alunnoList).hasSize(databaseSizeBeforeUpdate);
        Alunno testAlunno = alunnoList.get(alunnoList.size() - 1);
        assertThat(testAlunno.getNome()).isEqualTo(DEFAULT_NOME);
        assertThat(testAlunno.getCognome()).isEqualTo(DEFAULT_COGNOME);
        assertThat(testAlunno.getDataNascita()).isEqualTo(DEFAULT_DATA_NASCITA);
        assertThat(testAlunno.getMediaVoti()).isEqualTo(UPDATED_MEDIA_VOTI);
    }

    @Test
    @Transactional
    void fullUpdateAlunnoWithPatch() throws Exception {
        // Initialize the database
        alunnoRepository.saveAndFlush(alunno);

        int databaseSizeBeforeUpdate = alunnoRepository.findAll().size();

        // Update the alunno using partial update
        Alunno partialUpdatedAlunno = new Alunno();
        partialUpdatedAlunno.setId(alunno.getId());

        partialUpdatedAlunno.nome(UPDATED_NOME).cognome(UPDATED_COGNOME).dataNascita(UPDATED_DATA_NASCITA).mediaVoti(UPDATED_MEDIA_VOTI);

        restAlunnoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAlunno.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAlunno))
            )
            .andExpect(status().isOk());

        // Validate the Alunno in the database
        List<Alunno> alunnoList = alunnoRepository.findAll();
        assertThat(alunnoList).hasSize(databaseSizeBeforeUpdate);
        Alunno testAlunno = alunnoList.get(alunnoList.size() - 1);
        assertThat(testAlunno.getNome()).isEqualTo(UPDATED_NOME);
        assertThat(testAlunno.getCognome()).isEqualTo(UPDATED_COGNOME);
        assertThat(testAlunno.getDataNascita()).isEqualTo(UPDATED_DATA_NASCITA);
        assertThat(testAlunno.getMediaVoti()).isEqualTo(UPDATED_MEDIA_VOTI);
    }

    @Test
    @Transactional
    void patchNonExistingAlunno() throws Exception {
        int databaseSizeBeforeUpdate = alunnoRepository.findAll().size();
        alunno.setId(longCount.incrementAndGet());

        // Create the Alunno
        AlunnoDTO alunnoDTO = alunnoMapper.toDto(alunno);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAlunnoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, alunnoDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(alunnoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Alunno in the database
        List<Alunno> alunnoList = alunnoRepository.findAll();
        assertThat(alunnoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchAlunno() throws Exception {
        int databaseSizeBeforeUpdate = alunnoRepository.findAll().size();
        alunno.setId(longCount.incrementAndGet());

        // Create the Alunno
        AlunnoDTO alunnoDTO = alunnoMapper.toDto(alunno);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAlunnoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(alunnoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Alunno in the database
        List<Alunno> alunnoList = alunnoRepository.findAll();
        assertThat(alunnoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAlunno() throws Exception {
        int databaseSizeBeforeUpdate = alunnoRepository.findAll().size();
        alunno.setId(longCount.incrementAndGet());

        // Create the Alunno
        AlunnoDTO alunnoDTO = alunnoMapper.toDto(alunno);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAlunnoMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(alunnoDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Alunno in the database
        List<Alunno> alunnoList = alunnoRepository.findAll();
        assertThat(alunnoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteAlunno() throws Exception {
        // Initialize the database
        alunnoRepository.saveAndFlush(alunno);

        int databaseSizeBeforeDelete = alunnoRepository.findAll().size();

        // Delete the alunno
        restAlunnoMockMvc
            .perform(delete(ENTITY_API_URL_ID, alunno.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Alunno> alunnoList = alunnoRepository.findAll();
        assertThat(alunnoList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
