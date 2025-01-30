package com.lawyer.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.lawyer.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CaseDocumentDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(CaseDocumentDTO.class);
        CaseDocumentDTO caseDocumentDTO1 = new CaseDocumentDTO();
        caseDocumentDTO1.setId(1L);
        CaseDocumentDTO caseDocumentDTO2 = new CaseDocumentDTO();
        assertThat(caseDocumentDTO1).isNotEqualTo(caseDocumentDTO2);
        caseDocumentDTO2.setId(caseDocumentDTO1.getId());
        assertThat(caseDocumentDTO1).isEqualTo(caseDocumentDTO2);
        caseDocumentDTO2.setId(2L);
        assertThat(caseDocumentDTO1).isNotEqualTo(caseDocumentDTO2);
        caseDocumentDTO1.setId(null);
        assertThat(caseDocumentDTO1).isNotEqualTo(caseDocumentDTO2);
    }
}
