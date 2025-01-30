package com.lawyer.service.mapper;

import static com.lawyer.domain.CaseDocumentAsserts.*;
import static com.lawyer.domain.CaseDocumentTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CaseDocumentMapperTest {

    private CaseDocumentMapper caseDocumentMapper;

    @BeforeEach
    void setUp() {
        caseDocumentMapper = new CaseDocumentMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getCaseDocumentSample1();
        var actual = caseDocumentMapper.toEntity(caseDocumentMapper.toDto(expected));
        assertCaseDocumentAllPropertiesEquals(expected, actual);
    }
}
