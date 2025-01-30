package com.lawyer.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.lawyer.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CourtCaseTypeDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(CourtCaseTypeDTO.class);
        CourtCaseTypeDTO courtCaseTypeDTO1 = new CourtCaseTypeDTO();
        courtCaseTypeDTO1.setId(1L);
        CourtCaseTypeDTO courtCaseTypeDTO2 = new CourtCaseTypeDTO();
        assertThat(courtCaseTypeDTO1).isNotEqualTo(courtCaseTypeDTO2);
        courtCaseTypeDTO2.setId(courtCaseTypeDTO1.getId());
        assertThat(courtCaseTypeDTO1).isEqualTo(courtCaseTypeDTO2);
        courtCaseTypeDTO2.setId(2L);
        assertThat(courtCaseTypeDTO1).isNotEqualTo(courtCaseTypeDTO2);
        courtCaseTypeDTO1.setId(null);
        assertThat(courtCaseTypeDTO1).isNotEqualTo(courtCaseTypeDTO2);
    }
}
