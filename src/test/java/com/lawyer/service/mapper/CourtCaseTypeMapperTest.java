package com.lawyer.service.mapper;

import static com.lawyer.domain.CourtCaseTypeAsserts.*;
import static com.lawyer.domain.CourtCaseTypeTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CourtCaseTypeMapperTest {

    private CourtCaseTypeMapper courtCaseTypeMapper;

    @BeforeEach
    void setUp() {
        courtCaseTypeMapper = new CourtCaseTypeMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getCourtCaseTypeSample1();
        var actual = courtCaseTypeMapper.toEntity(courtCaseTypeMapper.toDto(expected));
        assertCourtCaseTypeAllPropertiesEquals(expected, actual);
    }
}
