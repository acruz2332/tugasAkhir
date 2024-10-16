package com.proyek.akhir.domain;

import static com.proyek.akhir.domain.HistoryTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.proyek.akhir.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class HistoryTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(History.class);
        History history1 = getHistorySample1();
        History history2 = new History();
        assertThat(history1).isNotEqualTo(history2);

        history2.setId(history1.getId());
        assertThat(history1).isEqualTo(history2);

        history2 = getHistorySample2();
        assertThat(history1).isNotEqualTo(history2);
    }
}
