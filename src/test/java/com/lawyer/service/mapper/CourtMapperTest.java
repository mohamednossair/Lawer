package com.lawyer.service.mapper;

import static com.lawyer.domain.CourtAsserts.*;
import static com.lawyer.domain.CourtTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CourtMapperTest {

    private CourtMapper courtMapper;

    @BeforeEach
    void setUp() {
        courtMapper = new CourtMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getCourtSample1();
        var actual = courtMapper.toEntity(courtMapper.toDto(expected));
        assertCourtAllPropertiesEquals(expected, actual);
    }
}
