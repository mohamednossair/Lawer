package com.lawyer.service.mapper;

import static com.lawyer.domain.CourtCaseAsserts.*;
import static com.lawyer.domain.CourtCaseTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CourtCaseMapperTest {

    private CourtCaseMapper courtCaseMapper;

    @BeforeEach
    void setUp() {
        courtCaseMapper = new CourtCaseMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getCourtCaseSample1();
        var actual = courtCaseMapper.toEntity(courtCaseMapper.toDto(expected));
        assertCourtCaseAllPropertiesEquals(expected, actual);
    }
}
