package com.proyek.akhir.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.proyek.akhir.IntegrationTest;
import com.proyek.akhir.domain.History;
import com.proyek.akhir.repository.HistoryRepository;
import com.proyek.akhir.service.HistoryService;
import com.proyek.akhir.service.dto.HistoryDTO;
import com.proyek.akhir.service.mapper.HistoryMapper;
import jakarta.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link HistoryResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class HistoryResourceIT {

    private static final String DEFAULT_PREDICTION_INPUT = "AAAAAAAAAA";
    private static final String UPDATED_PREDICTION_INPUT = "BBBBBBBBBB";

    private static final String DEFAULT_PREDICTION_RESULT = "AAAAAAAAAA";
    private static final String UPDATED_PREDICTION_RESULT = "BBBBBBBBBB";

    private static final Integer DEFAULT_PREDICTION_DAY = 1;
    private static final Integer UPDATED_PREDICTION_DAY = 2;

    private static final Integer DEFAULT_WINDOW_SIZE = 1;
    private static final Integer UPDATED_WINDOW_SIZE = 2;

    private static final String ENTITY_API_URL = "/api/histories";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private HistoryRepository historyRepository;

    @Mock
    private HistoryRepository historyRepositoryMock;

    @Autowired
    private HistoryMapper historyMapper;

    @Mock
    private HistoryService historyServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restHistoryMockMvc;

    private History history;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static History createEntity(EntityManager em) {
        History history = new History()
            .predictionInput(DEFAULT_PREDICTION_INPUT)
            .predictionResult(DEFAULT_PREDICTION_RESULT)
            .predictionDay(DEFAULT_PREDICTION_DAY)
            .windowSize(DEFAULT_WINDOW_SIZE);
        return history;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static History createUpdatedEntity(EntityManager em) {
        History history = new History()
            .predictionInput(UPDATED_PREDICTION_INPUT)
            .predictionResult(UPDATED_PREDICTION_RESULT)
            .predictionDay(UPDATED_PREDICTION_DAY)
            .windowSize(UPDATED_WINDOW_SIZE);
        return history;
    }

    @BeforeEach
    public void initTest() {
        history = createEntity(em);
    }

    @Test
    @Transactional
    void createHistory() throws Exception {
        int databaseSizeBeforeCreate = historyRepository.findAll().size();
        // Create the History
        HistoryDTO historyDTO = historyMapper.toDto(history);
        restHistoryMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(historyDTO)))
            .andExpect(status().isCreated());

        // Validate the History in the database
        List<History> historyList = historyRepository.findAll();
        assertThat(historyList).hasSize(databaseSizeBeforeCreate + 1);
        History testHistory = historyList.get(historyList.size() - 1);
        assertThat(testHistory.getPredictionInput()).isEqualTo(DEFAULT_PREDICTION_INPUT);
        assertThat(testHistory.getPredictionResult()).isEqualTo(DEFAULT_PREDICTION_RESULT);
        assertThat(testHistory.getPredictionDay()).isEqualTo(DEFAULT_PREDICTION_DAY);
        assertThat(testHistory.getWindowSize()).isEqualTo(DEFAULT_WINDOW_SIZE);
    }

    @Test
    @Transactional
    void createHistoryWithExistingId() throws Exception {
        // Create the History with an existing ID
        history.setId(1L);
        HistoryDTO historyDTO = historyMapper.toDto(history);

        int databaseSizeBeforeCreate = historyRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restHistoryMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(historyDTO)))
            .andExpect(status().isBadRequest());

        // Validate the History in the database
        List<History> historyList = historyRepository.findAll();
        assertThat(historyList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkPredictionInputIsRequired() throws Exception {
        int databaseSizeBeforeTest = historyRepository.findAll().size();
        // set the field null
        history.setPredictionInput(null);

        // Create the History, which fails.
        HistoryDTO historyDTO = historyMapper.toDto(history);

        restHistoryMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(historyDTO)))
            .andExpect(status().isBadRequest());

        List<History> historyList = historyRepository.findAll();
        assertThat(historyList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPredictionResultIsRequired() throws Exception {
        int databaseSizeBeforeTest = historyRepository.findAll().size();
        // set the field null
        history.setPredictionResult(null);

        // Create the History, which fails.
        HistoryDTO historyDTO = historyMapper.toDto(history);

        restHistoryMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(historyDTO)))
            .andExpect(status().isBadRequest());

        List<History> historyList = historyRepository.findAll();
        assertThat(historyList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPredictionDayIsRequired() throws Exception {
        int databaseSizeBeforeTest = historyRepository.findAll().size();
        // set the field null
        history.setPredictionDay(null);

        // Create the History, which fails.
        HistoryDTO historyDTO = historyMapper.toDto(history);

        restHistoryMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(historyDTO)))
            .andExpect(status().isBadRequest());

        List<History> historyList = historyRepository.findAll();
        assertThat(historyList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkWindowSizeIsRequired() throws Exception {
        int databaseSizeBeforeTest = historyRepository.findAll().size();
        // set the field null
        history.setWindowSize(null);

        // Create the History, which fails.
        HistoryDTO historyDTO = historyMapper.toDto(history);

        restHistoryMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(historyDTO)))
            .andExpect(status().isBadRequest());

        List<History> historyList = historyRepository.findAll();
        assertThat(historyList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllHistories() throws Exception {
        // Initialize the database
        historyRepository.saveAndFlush(history);

        // Get all the historyList
        restHistoryMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(history.getId().intValue())))
            .andExpect(jsonPath("$.[*].predictionInput").value(hasItem(DEFAULT_PREDICTION_INPUT)))
            .andExpect(jsonPath("$.[*].predictionResult").value(hasItem(DEFAULT_PREDICTION_RESULT)))
            .andExpect(jsonPath("$.[*].predictionDay").value(hasItem(DEFAULT_PREDICTION_DAY)))
            .andExpect(jsonPath("$.[*].windowSize").value(hasItem(DEFAULT_WINDOW_SIZE)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllHistoriesWithEagerRelationshipsIsEnabled() throws Exception {
        when(historyServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restHistoryMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(historyServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllHistoriesWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(historyServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restHistoryMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(historyRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getHistory() throws Exception {
        // Initialize the database
        historyRepository.saveAndFlush(history);

        // Get the history
        restHistoryMockMvc
            .perform(get(ENTITY_API_URL_ID, history.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(history.getId().intValue()))
            .andExpect(jsonPath("$.predictionInput").value(DEFAULT_PREDICTION_INPUT))
            .andExpect(jsonPath("$.predictionResult").value(DEFAULT_PREDICTION_RESULT))
            .andExpect(jsonPath("$.predictionDay").value(DEFAULT_PREDICTION_DAY))
            .andExpect(jsonPath("$.windowSize").value(DEFAULT_WINDOW_SIZE));
    }

    @Test
    @Transactional
    void getNonExistingHistory() throws Exception {
        // Get the history
        restHistoryMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingHistory() throws Exception {
        // Initialize the database
        historyRepository.saveAndFlush(history);

        int databaseSizeBeforeUpdate = historyRepository.findAll().size();

        // Update the history
        History updatedHistory = historyRepository.findById(history.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedHistory are not directly saved in db
        em.detach(updatedHistory);
        updatedHistory
            .predictionInput(UPDATED_PREDICTION_INPUT)
            .predictionResult(UPDATED_PREDICTION_RESULT)
            .predictionDay(UPDATED_PREDICTION_DAY)
            .windowSize(UPDATED_WINDOW_SIZE);
        HistoryDTO historyDTO = historyMapper.toDto(updatedHistory);

        restHistoryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, historyDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(historyDTO))
            )
            .andExpect(status().isOk());

        // Validate the History in the database
        List<History> historyList = historyRepository.findAll();
        assertThat(historyList).hasSize(databaseSizeBeforeUpdate);
        History testHistory = historyList.get(historyList.size() - 1);
        assertThat(testHistory.getPredictionInput()).isEqualTo(UPDATED_PREDICTION_INPUT);
        assertThat(testHistory.getPredictionResult()).isEqualTo(UPDATED_PREDICTION_RESULT);
        assertThat(testHistory.getPredictionDay()).isEqualTo(UPDATED_PREDICTION_DAY);
        assertThat(testHistory.getWindowSize()).isEqualTo(UPDATED_WINDOW_SIZE);
    }

    @Test
    @Transactional
    void putNonExistingHistory() throws Exception {
        int databaseSizeBeforeUpdate = historyRepository.findAll().size();
        history.setId(longCount.incrementAndGet());

        // Create the History
        HistoryDTO historyDTO = historyMapper.toDto(history);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restHistoryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, historyDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(historyDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the History in the database
        List<History> historyList = historyRepository.findAll();
        assertThat(historyList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchHistory() throws Exception {
        int databaseSizeBeforeUpdate = historyRepository.findAll().size();
        history.setId(longCount.incrementAndGet());

        // Create the History
        HistoryDTO historyDTO = historyMapper.toDto(history);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHistoryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(historyDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the History in the database
        List<History> historyList = historyRepository.findAll();
        assertThat(historyList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamHistory() throws Exception {
        int databaseSizeBeforeUpdate = historyRepository.findAll().size();
        history.setId(longCount.incrementAndGet());

        // Create the History
        HistoryDTO historyDTO = historyMapper.toDto(history);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHistoryMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(historyDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the History in the database
        List<History> historyList = historyRepository.findAll();
        assertThat(historyList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateHistoryWithPatch() throws Exception {
        // Initialize the database
        historyRepository.saveAndFlush(history);

        int databaseSizeBeforeUpdate = historyRepository.findAll().size();

        // Update the history using partial update
        History partialUpdatedHistory = new History();
        partialUpdatedHistory.setId(history.getId());

        partialUpdatedHistory
            .predictionInput(UPDATED_PREDICTION_INPUT)
            .predictionDay(UPDATED_PREDICTION_DAY)
            .windowSize(UPDATED_WINDOW_SIZE);

        restHistoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedHistory.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedHistory))
            )
            .andExpect(status().isOk());

        // Validate the History in the database
        List<History> historyList = historyRepository.findAll();
        assertThat(historyList).hasSize(databaseSizeBeforeUpdate);
        History testHistory = historyList.get(historyList.size() - 1);
        assertThat(testHistory.getPredictionInput()).isEqualTo(UPDATED_PREDICTION_INPUT);
        assertThat(testHistory.getPredictionResult()).isEqualTo(DEFAULT_PREDICTION_RESULT);
        assertThat(testHistory.getPredictionDay()).isEqualTo(UPDATED_PREDICTION_DAY);
        assertThat(testHistory.getWindowSize()).isEqualTo(UPDATED_WINDOW_SIZE);
    }

    @Test
    @Transactional
    void fullUpdateHistoryWithPatch() throws Exception {
        // Initialize the database
        historyRepository.saveAndFlush(history);

        int databaseSizeBeforeUpdate = historyRepository.findAll().size();

        // Update the history using partial update
        History partialUpdatedHistory = new History();
        partialUpdatedHistory.setId(history.getId());

        partialUpdatedHistory
            .predictionInput(UPDATED_PREDICTION_INPUT)
            .predictionResult(UPDATED_PREDICTION_RESULT)
            .predictionDay(UPDATED_PREDICTION_DAY)
            .windowSize(UPDATED_WINDOW_SIZE);

        restHistoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedHistory.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedHistory))
            )
            .andExpect(status().isOk());

        // Validate the History in the database
        List<History> historyList = historyRepository.findAll();
        assertThat(historyList).hasSize(databaseSizeBeforeUpdate);
        History testHistory = historyList.get(historyList.size() - 1);
        assertThat(testHistory.getPredictionInput()).isEqualTo(UPDATED_PREDICTION_INPUT);
        assertThat(testHistory.getPredictionResult()).isEqualTo(UPDATED_PREDICTION_RESULT);
        assertThat(testHistory.getPredictionDay()).isEqualTo(UPDATED_PREDICTION_DAY);
        assertThat(testHistory.getWindowSize()).isEqualTo(UPDATED_WINDOW_SIZE);
    }

    @Test
    @Transactional
    void patchNonExistingHistory() throws Exception {
        int databaseSizeBeforeUpdate = historyRepository.findAll().size();
        history.setId(longCount.incrementAndGet());

        // Create the History
        HistoryDTO historyDTO = historyMapper.toDto(history);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restHistoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, historyDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(historyDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the History in the database
        List<History> historyList = historyRepository.findAll();
        assertThat(historyList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchHistory() throws Exception {
        int databaseSizeBeforeUpdate = historyRepository.findAll().size();
        history.setId(longCount.incrementAndGet());

        // Create the History
        HistoryDTO historyDTO = historyMapper.toDto(history);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHistoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(historyDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the History in the database
        List<History> historyList = historyRepository.findAll();
        assertThat(historyList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamHistory() throws Exception {
        int databaseSizeBeforeUpdate = historyRepository.findAll().size();
        history.setId(longCount.incrementAndGet());

        // Create the History
        HistoryDTO historyDTO = historyMapper.toDto(history);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHistoryMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(historyDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the History in the database
        List<History> historyList = historyRepository.findAll();
        assertThat(historyList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteHistory() throws Exception {
        // Initialize the database
        historyRepository.saveAndFlush(history);

        int databaseSizeBeforeDelete = historyRepository.findAll().size();

        // Delete the history
        restHistoryMockMvc
            .perform(delete(ENTITY_API_URL_ID, history.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<History> historyList = historyRepository.findAll();
        assertThat(historyList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
