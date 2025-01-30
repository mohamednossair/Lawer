package com.lawyer.domain;

import static com.lawyer.domain.CourtCaseTypeTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.lawyer.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CourtCaseTypeTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CourtCaseType.class);
        CourtCaseType courtCaseType1 = getCourtCaseTypeSample1();
        CourtCaseType courtCaseType2 = new CourtCaseType();
        assertThat(courtCaseType1).isNotEqualTo(courtCaseType2);

        courtCaseType2.setId(courtCaseType1.getId());
        assertThat(courtCaseType1).isEqualTo(courtCaseType2);

        courtCaseType2 = getCourtCaseTypeSample2();
        assertThat(courtCaseType1).isNotEqualTo(courtCaseType2);
    }
}
