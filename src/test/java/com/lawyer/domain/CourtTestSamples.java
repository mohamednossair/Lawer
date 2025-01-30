package com.lawyer.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class CourtTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Court getCourtSample1() {
        return new Court().id(1L).courtName("courtName1");
    }

    public static Court getCourtSample2() {
        return new Court().id(2L).courtName("courtName2");
    }

    public static Court getCourtRandomSampleGenerator() {
        return new Court().id(longCount.incrementAndGet()).courtName(UUID.randomUUID().toString());
    }
}
