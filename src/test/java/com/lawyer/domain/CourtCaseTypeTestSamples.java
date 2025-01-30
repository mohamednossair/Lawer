package com.lawyer.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class CourtCaseTypeTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static CourtCaseType getCourtCaseTypeSample1() {
        return new CourtCaseType().id(1L).caseTypeName("caseTypeName1");
    }

    public static CourtCaseType getCourtCaseTypeSample2() {
        return new CourtCaseType().id(2L).caseTypeName("caseTypeName2");
    }

    public static CourtCaseType getCourtCaseTypeRandomSampleGenerator() {
        return new CourtCaseType().id(longCount.incrementAndGet()).caseTypeName(UUID.randomUUID().toString());
    }
}
