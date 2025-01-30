package com.lawyer.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.lawyer.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CaseSessionDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(CaseSessionDTO.class);
        CaseSessionDTO caseSessionDTO1 = new CaseSessionDTO();
        caseSessionDTO1.setId(1L);
        CaseSessionDTO caseSessionDTO2 = new CaseSessionDTO();
        assertThat(caseSessionDTO1).isNotEqualTo(caseSessionDTO2);
        caseSessionDTO2.setId(caseSessionDTO1.getId());
        assertThat(caseSessionDTO1).isEqualTo(caseSessionDTO2);
        caseSessionDTO2.setId(2L);
        assertThat(caseSessionDTO1).isNotEqualTo(caseSessionDTO2);
        caseSessionDTO1.setId(null);
        assertThat(caseSessionDTO1).isNotEqualTo(caseSessionDTO2);
    }
}
