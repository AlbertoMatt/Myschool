package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.Classe;
import com.mycompany.myapp.repository.ClasseRepository;
import com.mycompany.myapp.service.dto.ClasseDTO;
import com.mycompany.myapp.service.mapper.ClasseMapper;
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
 * Integration tests for the {@link ClasseResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ClasseResourceIT {

    private static final Integer DEFAULT_INDICAZIONE_NUMERICA = 1;
    private static final Integer UPDATED_INDICAZIONE_NUMERICA = 2;

    private static final String DEFAULT_SEZIONE = "A";
    private static final String UPDATED_SEZIONE = "B";

    private static final String DEFAULT_NOTE = "AAAAAAAAAA";
    private static final String UPDATED_NOTE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/classes";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ClasseRepository classeRepository;

    @Autowired
    private ClasseMapper classeMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restClasseMockMvc;

    private Classe classe;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Classe createEntity(EntityManager em) {
        Classe classe = new Classe().indicazioneNumerica(DEFAULT_INDICAZIONE_NUMERICA).sezione(DEFAULT_SEZIONE).note(DEFAULT_NOTE);
        return classe;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Classe createUpdatedEntity(EntityManager em) {
        Classe classe = new Classe().indicazioneNumerica(UPDATED_INDICAZIONE_NUMERICA).sezione(UPDATED_SEZIONE).note(UPDATED_NOTE);
        return classe;
    }

    @BeforeEach
    public void initTest() {
        classe = createEntity(em);
    }

    @Test
    @Transactional
    void createClasse() throws Exception {
        int databaseSizeBeforeCreate = classeRepository.findAll().size();
        // Create the Classe
        ClasseDTO classeDTO = classeMapper.toDto(classe);
        restClasseMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(classeDTO)))
            .andExpect(status().isCreated());

        // Validate the Classe in the database
        List<Classe> classeList = classeRepository.findAll();
        assertThat(classeList).hasSize(databaseSizeBeforeCreate + 1);
        Classe testClasse = classeList.get(classeList.size() - 1);
        assertThat(testClasse.getIndicazioneNumerica()).isEqualTo(DEFAULT_INDICAZIONE_NUMERICA);
        assertThat(testClasse.getSezione()).isEqualTo(DEFAULT_SEZIONE);
        assertThat(testClasse.getNote()).isEqualTo(DEFAULT_NOTE);
    }

    @Test
    @Transactional
    void createClasseWithExistingId() throws Exception {
        // Create the Classe with an existing ID
        classe.setId(1L);
        ClasseDTO classeDTO = classeMapper.toDto(classe);

        int databaseSizeBeforeCreate = classeRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restClasseMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(classeDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Classe in the database
        List<Classe> classeList = classeRepository.findAll();
        assertThat(classeList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkIndicazioneNumericaIsRequired() throws Exception {
        int databaseSizeBeforeTest = classeRepository.findAll().size();
        // set the field null
        classe.setIndicazioneNumerica(null);

        // Create the Classe, which fails.
        ClasseDTO classeDTO = classeMapper.toDto(classe);

        restClasseMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(classeDTO)))
            .andExpect(status().isBadRequest());

        List<Classe> classeList = classeRepository.findAll();
        assertThat(classeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkSezioneIsRequired() throws Exception {
        int databaseSizeBeforeTest = classeRepository.findAll().size();
        // set the field null
        classe.setSezione(null);

        // Create the Classe, which fails.
        ClasseDTO classeDTO = classeMapper.toDto(classe);

        restClasseMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(classeDTO)))
            .andExpect(status().isBadRequest());

        List<Classe> classeList = classeRepository.findAll();
        assertThat(classeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllClasses() throws Exception {
        // Initialize the database
        classeRepository.saveAndFlush(classe);

        // Get all the classeList
        restClasseMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(classe.getId().intValue())))
            .andExpect(jsonPath("$.[*].indicazioneNumerica").value(hasItem(DEFAULT_INDICAZIONE_NUMERICA)))
            .andExpect(jsonPath("$.[*].sezione").value(hasItem(DEFAULT_SEZIONE)))
            .andExpect(jsonPath("$.[*].note").value(hasItem(DEFAULT_NOTE)));
    }

    @Test
    @Transactional
    void getClasse() throws Exception {
        // Initialize the database
        classeRepository.saveAndFlush(classe);

        // Get the classe
        restClasseMockMvc
            .perform(get(ENTITY_API_URL_ID, classe.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(classe.getId().intValue()))
            .andExpect(jsonPath("$.indicazioneNumerica").value(DEFAULT_INDICAZIONE_NUMERICA))
            .andExpect(jsonPath("$.sezione").value(DEFAULT_SEZIONE))
            .andExpect(jsonPath("$.note").value(DEFAULT_NOTE));
    }

    @Test
    @Transactional
    void getNonExistingClasse() throws Exception {
        // Get the classe
        restClasseMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingClasse() throws Exception {
        // Initialize the database
        classeRepository.saveAndFlush(classe);

        int databaseSizeBeforeUpdate = classeRepository.findAll().size();

        // Update the classe
        Classe updatedClasse = classeRepository.findById(classe.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedClasse are not directly saved in db
        em.detach(updatedClasse);
        updatedClasse.indicazioneNumerica(UPDATED_INDICAZIONE_NUMERICA).sezione(UPDATED_SEZIONE).note(UPDATED_NOTE);
        ClasseDTO classeDTO = classeMapper.toDto(updatedClasse);

        restClasseMockMvc
            .perform(
                put(ENTITY_API_URL_ID, classeDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(classeDTO))
            )
            .andExpect(status().isOk());

        // Validate the Classe in the database
        List<Classe> classeList = classeRepository.findAll();
        assertThat(classeList).hasSize(databaseSizeBeforeUpdate);
        Classe testClasse = classeList.get(classeList.size() - 1);
        assertThat(testClasse.getIndicazioneNumerica()).isEqualTo(UPDATED_INDICAZIONE_NUMERICA);
        assertThat(testClasse.getSezione()).isEqualTo(UPDATED_SEZIONE);
        assertThat(testClasse.getNote()).isEqualTo(UPDATED_NOTE);
    }

    @Test
    @Transactional
    void putNonExistingClasse() throws Exception {
        int databaseSizeBeforeUpdate = classeRepository.findAll().size();
        classe.setId(longCount.incrementAndGet());

        // Create the Classe
        ClasseDTO classeDTO = classeMapper.toDto(classe);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restClasseMockMvc
            .perform(
                put(ENTITY_API_URL_ID, classeDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(classeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Classe in the database
        List<Classe> classeList = classeRepository.findAll();
        assertThat(classeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchClasse() throws Exception {
        int databaseSizeBeforeUpdate = classeRepository.findAll().size();
        classe.setId(longCount.incrementAndGet());

        // Create the Classe
        ClasseDTO classeDTO = classeMapper.toDto(classe);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restClasseMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(classeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Classe in the database
        List<Classe> classeList = classeRepository.findAll();
        assertThat(classeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamClasse() throws Exception {
        int databaseSizeBeforeUpdate = classeRepository.findAll().size();
        classe.setId(longCount.incrementAndGet());

        // Create the Classe
        ClasseDTO classeDTO = classeMapper.toDto(classe);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restClasseMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(classeDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Classe in the database
        List<Classe> classeList = classeRepository.findAll();
        assertThat(classeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateClasseWithPatch() throws Exception {
        // Initialize the database
        classeRepository.saveAndFlush(classe);

        int databaseSizeBeforeUpdate = classeRepository.findAll().size();

        // Update the classe using partial update
        Classe partialUpdatedClasse = new Classe();
        partialUpdatedClasse.setId(classe.getId());

        partialUpdatedClasse.indicazioneNumerica(UPDATED_INDICAZIONE_NUMERICA).sezione(UPDATED_SEZIONE).note(UPDATED_NOTE);

        restClasseMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedClasse.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedClasse))
            )
            .andExpect(status().isOk());

        // Validate the Classe in the database
        List<Classe> classeList = classeRepository.findAll();
        assertThat(classeList).hasSize(databaseSizeBeforeUpdate);
        Classe testClasse = classeList.get(classeList.size() - 1);
        assertThat(testClasse.getIndicazioneNumerica()).isEqualTo(UPDATED_INDICAZIONE_NUMERICA);
        assertThat(testClasse.getSezione()).isEqualTo(UPDATED_SEZIONE);
        assertThat(testClasse.getNote()).isEqualTo(UPDATED_NOTE);
    }

    @Test
    @Transactional
    void fullUpdateClasseWithPatch() throws Exception {
        // Initialize the database
        classeRepository.saveAndFlush(classe);

        int databaseSizeBeforeUpdate = classeRepository.findAll().size();

        // Update the classe using partial update
        Classe partialUpdatedClasse = new Classe();
        partialUpdatedClasse.setId(classe.getId());

        partialUpdatedClasse.indicazioneNumerica(UPDATED_INDICAZIONE_NUMERICA).sezione(UPDATED_SEZIONE).note(UPDATED_NOTE);

        restClasseMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedClasse.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedClasse))
            )
            .andExpect(status().isOk());

        // Validate the Classe in the database
        List<Classe> classeList = classeRepository.findAll();
        assertThat(classeList).hasSize(databaseSizeBeforeUpdate);
        Classe testClasse = classeList.get(classeList.size() - 1);
        assertThat(testClasse.getIndicazioneNumerica()).isEqualTo(UPDATED_INDICAZIONE_NUMERICA);
        assertThat(testClasse.getSezione()).isEqualTo(UPDATED_SEZIONE);
        assertThat(testClasse.getNote()).isEqualTo(UPDATED_NOTE);
    }

    @Test
    @Transactional
    void patchNonExistingClasse() throws Exception {
        int databaseSizeBeforeUpdate = classeRepository.findAll().size();
        classe.setId(longCount.incrementAndGet());

        // Create the Classe
        ClasseDTO classeDTO = classeMapper.toDto(classe);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restClasseMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, classeDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(classeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Classe in the database
        List<Classe> classeList = classeRepository.findAll();
        assertThat(classeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchClasse() throws Exception {
        int databaseSizeBeforeUpdate = classeRepository.findAll().size();
        classe.setId(longCount.incrementAndGet());

        // Create the Classe
        ClasseDTO classeDTO = classeMapper.toDto(classe);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restClasseMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(classeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Classe in the database
        List<Classe> classeList = classeRepository.findAll();
        assertThat(classeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamClasse() throws Exception {
        int databaseSizeBeforeUpdate = classeRepository.findAll().size();
        classe.setId(longCount.incrementAndGet());

        // Create the Classe
        ClasseDTO classeDTO = classeMapper.toDto(classe);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restClasseMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(classeDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Classe in the database
        List<Classe> classeList = classeRepository.findAll();
        assertThat(classeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteClasse() throws Exception {
        // Initialize the database
        classeRepository.saveAndFlush(classe);

        int databaseSizeBeforeDelete = classeRepository.findAll().size();

        // Delete the classe
        restClasseMockMvc
            .perform(delete(ENTITY_API_URL_ID, classe.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Classe> classeList = classeRepository.findAll();
        assertThat(classeList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
