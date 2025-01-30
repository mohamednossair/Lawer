package com.lawyer.domain;

import static com.lawyer.domain.CaseStatusTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.lawyer.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CaseStatusTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CaseStatus.class);
        CaseStatus caseStatus1 = getCaseStatusSample1();
        CaseStatus caseStatus2 = new CaseStatus();
        assertThat(caseStatus1).isNotEqualTo(caseStatus2);

        caseStatus2.setId(caseStatus1.getId());
        assertThat(caseStatus1).isEqualTo(caseStatus2);

        caseStatus2 = getCaseStatusSample2();
        assertThat(caseStatus1).isNotEqualTo(caseStatus2);
    }
}
