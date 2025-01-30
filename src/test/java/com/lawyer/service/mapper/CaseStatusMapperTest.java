package com.lawyer.service.mapper;

import static com.lawyer.domain.CaseStatusAsserts.*;
import static com.lawyer.domain.CaseStatusTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CaseStatusMapperTest {

    private CaseStatusMapper caseStatusMapper;

    @BeforeEach
    void setUp() {
        caseStatusMapper = new CaseStatusMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getCaseStatusSample1();
        var actual = caseStatusMapper.toEntity(caseStatusMapper.toDto(expected));
        assertCaseStatusAllPropertiesEquals(expected, actual);
    }
}
