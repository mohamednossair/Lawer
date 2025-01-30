package com.lawyer.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class CaseStatusTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static CaseStatus getCaseStatusSample1() {
        return new CaseStatus().id(1L).caseStatusName("caseStatusName1");
    }

    public static CaseStatus getCaseStatusSample2() {
        return new CaseStatus().id(2L).caseStatusName("caseStatusName2");
    }

    public static CaseStatus getCaseStatusRandomSampleGenerator() {
        return new CaseStatus().id(longCount.incrementAndGet()).caseStatusName(UUID.randomUUID().toString());
    }
}
