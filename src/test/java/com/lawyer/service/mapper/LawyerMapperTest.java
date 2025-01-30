package com.lawyer.service.mapper;

import static com.lawyer.domain.LawyerAsserts.*;
import static com.lawyer.domain.LawyerTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class LawyerMapperTest {

    private LawyerMapper lawyerMapper;

    @BeforeEach
    void setUp() {
        lawyerMapper = new LawyerMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getLawyerSample1();
        var actual = lawyerMapper.toEntity(lawyerMapper.toDto(expected));
        assertLawyerAllPropertiesEquals(expected, actual);
    }
}
