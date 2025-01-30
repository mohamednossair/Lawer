package com.lawyer.domain;

import static com.lawyer.domain.CourtTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.lawyer.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CourtTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Court.class);
        Court court1 = getCourtSample1();
        Court court2 = new Court();
        assertThat(court1).isNotEqualTo(court2);

        court2.setId(court1.getId());
        assertThat(court1).isEqualTo(court2);

        court2 = getCourtSample2();
        assertThat(court1).isNotEqualTo(court2);
    }
}
