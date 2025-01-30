package com.lawyer.service.mapper;

import static com.lawyer.domain.CaseSessionAsserts.*;
import static com.lawyer.domain.CaseSessionTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CaseSessionMapperTest {

    private CaseSessionMapper caseSessionMapper;

    @BeforeEach
    void setUp() {
        caseSessionMapper = new CaseSessionMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getCaseSessionSample1();
        var actual = caseSessionMapper.toEntity(caseSessionMapper.toDto(expected));
        assertCaseSessionAllPropertiesEquals(expected, actual);
    }
}
