package com.lawyer.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.lawyer.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CaseStatusDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(CaseStatusDTO.class);
        CaseStatusDTO caseStatusDTO1 = new CaseStatusDTO();
        caseStatusDTO1.setId(1L);
        CaseStatusDTO caseStatusDTO2 = new CaseStatusDTO();
        assertThat(caseStatusDTO1).isNotEqualTo(caseStatusDTO2);
        caseStatusDTO2.setId(caseStatusDTO1.getId());
        assertThat(caseStatusDTO1).isEqualTo(caseStatusDTO2);
        caseStatusDTO2.setId(2L);
        assertThat(caseStatusDTO1).isNotEqualTo(caseStatusDTO2);
        caseStatusDTO1.setId(null);
        assertThat(caseStatusDTO1).isNotEqualTo(caseStatusDTO2);
    }
}
