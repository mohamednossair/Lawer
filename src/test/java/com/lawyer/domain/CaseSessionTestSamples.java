package com.lawyer.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class CaseSessionTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static CaseSession getCaseSessionSample1() {
        return new CaseSession().id(1L).description("description1").notes("notes1");
    }

    public static CaseSession getCaseSessionSample2() {
        return new CaseSession().id(2L).description("description2").notes("notes2");
    }

    public static CaseSession getCaseSessionRandomSampleGenerator() {
        return new CaseSession()
            .id(longCount.incrementAndGet())
            .description(UUID.randomUUID().toString())
            .notes(UUID.randomUUID().toString());
    }
}
