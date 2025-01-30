package com.lawyer.domain;

import static com.lawyer.domain.CaseDocumentTestSamples.*;
import static com.lawyer.domain.CourtCaseTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.lawyer.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CaseDocumentTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CaseDocument.class);
        CaseDocument caseDocument1 = getCaseDocumentSample1();
        CaseDocument caseDocument2 = new CaseDocument();
        assertThat(caseDocument1).isNotEqualTo(caseDocument2);

        caseDocument2.setId(caseDocument1.getId());
        assertThat(caseDocument1).isEqualTo(caseDocument2);

        caseDocument2 = getCaseDocumentSample2();
        assertThat(caseDocument1).isNotEqualTo(caseDocument2);
    }

    @Test
    void courtCaseTest() {
        CaseDocument caseDocument = getCaseDocumentRandomSampleGenerator();
        CourtCase courtCaseBack = getCourtCaseRandomSampleGenerator();

        caseDocument.setCourtCase(courtCaseBack);
        assertThat(caseDocument.getCourtCase()).isEqualTo(courtCaseBack);

        caseDocument.courtCase(null);
        assertThat(caseDocument.getCourtCase()).isNull();
    }
}
