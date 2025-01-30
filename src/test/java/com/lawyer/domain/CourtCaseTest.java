package com.lawyer.domain;

import static com.lawyer.domain.CaseStatusTestSamples.*;
import static com.lawyer.domain.ClientTestSamples.*;
import static com.lawyer.domain.CourtCaseTestSamples.*;
import static com.lawyer.domain.CourtCaseTypeTestSamples.*;
import static com.lawyer.domain.CourtTestSamples.*;
import static com.lawyer.domain.LawyerTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.lawyer.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CourtCaseTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CourtCase.class);
        CourtCase courtCase1 = getCourtCaseSample1();
        CourtCase courtCase2 = new CourtCase();
        assertThat(courtCase1).isNotEqualTo(courtCase2);

        courtCase2.setId(courtCase1.getId());
        assertThat(courtCase1).isEqualTo(courtCase2);

        courtCase2 = getCourtCaseSample2();
        assertThat(courtCase1).isNotEqualTo(courtCase2);
    }

    @Test
    void courtTest() {
        CourtCase courtCase = getCourtCaseRandomSampleGenerator();
        Court courtBack = getCourtRandomSampleGenerator();

        courtCase.setCourt(courtBack);
        assertThat(courtCase.getCourt()).isEqualTo(courtBack);

        courtCase.court(null);
        assertThat(courtCase.getCourt()).isNull();
    }

    @Test
    void clientTest() {
        CourtCase courtCase = getCourtCaseRandomSampleGenerator();
        Client clientBack = getClientRandomSampleGenerator();

        courtCase.setClient(clientBack);
        assertThat(courtCase.getClient()).isEqualTo(clientBack);

        courtCase.client(null);
        assertThat(courtCase.getClient()).isNull();
    }

    @Test
    void courtCaseTypeTest() {
        CourtCase courtCase = getCourtCaseRandomSampleGenerator();
        CourtCaseType courtCaseTypeBack = getCourtCaseTypeRandomSampleGenerator();

        courtCase.setCourtCaseType(courtCaseTypeBack);
        assertThat(courtCase.getCourtCaseType()).isEqualTo(courtCaseTypeBack);

        courtCase.courtCaseType(null);
        assertThat(courtCase.getCourtCaseType()).isNull();
    }

    @Test
    void caseStatusTest() {
        CourtCase courtCase = getCourtCaseRandomSampleGenerator();
        CaseStatus caseStatusBack = getCaseStatusRandomSampleGenerator();

        courtCase.setCaseStatus(caseStatusBack);
        assertThat(courtCase.getCaseStatus()).isEqualTo(caseStatusBack);

        courtCase.caseStatus(null);
        assertThat(courtCase.getCaseStatus()).isNull();
    }

    @Test
    void opponentLawyerIdTest() {
        CourtCase courtCase = getCourtCaseRandomSampleGenerator();
        Lawyer lawyerBack = getLawyerRandomSampleGenerator();

        courtCase.setOpponentLawyerId(lawyerBack);
        assertThat(courtCase.getOpponentLawyerId()).isEqualTo(lawyerBack);

        courtCase.opponentLawyerId(null);
        assertThat(courtCase.getOpponentLawyerId()).isNull();
    }
}
