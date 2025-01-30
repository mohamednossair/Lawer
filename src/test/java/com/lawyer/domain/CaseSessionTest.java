package com.lawyer.domain;

import static com.lawyer.domain.CaseSessionTestSamples.*;
import static com.lawyer.domain.CourtCaseTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.lawyer.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CaseSessionTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CaseSession.class);
        CaseSession caseSession1 = getCaseSessionSample1();
        CaseSession caseSession2 = new CaseSession();
        assertThat(caseSession1).isNotEqualTo(caseSession2);

        caseSession2.setId(caseSession1.getId());
        assertThat(caseSession1).isEqualTo(caseSession2);

        caseSession2 = getCaseSessionSample2();
        assertThat(caseSession1).isNotEqualTo(caseSession2);
    }

    @Test
    void courtCaseTest() {
        CaseSession caseSession = getCaseSessionRandomSampleGenerator();
        CourtCase courtCaseBack = getCourtCaseRandomSampleGenerator();

        caseSession.setCourtCase(courtCaseBack);
        assertThat(caseSession.getCourtCase()).isEqualTo(courtCaseBack);

        caseSession.courtCase(null);
        assertThat(caseSession.getCourtCase()).isNull();
    }
}
