package com.lawyer.domain;

import static com.lawyer.domain.LawyerTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.lawyer.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class LawyerTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Lawyer.class);
        Lawyer lawyer1 = getLawyerSample1();
        Lawyer lawyer2 = new Lawyer();
        assertThat(lawyer1).isNotEqualTo(lawyer2);

        lawyer2.setId(lawyer1.getId());
        assertThat(lawyer1).isEqualTo(lawyer2);

        lawyer2 = getLawyerSample2();
        assertThat(lawyer1).isNotEqualTo(lawyer2);
    }
}
