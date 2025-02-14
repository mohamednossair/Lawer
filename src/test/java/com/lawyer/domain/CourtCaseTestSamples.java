package com.lawyer.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class CourtCaseTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static CourtCase getCourtCaseSample1() {
        return new CourtCase()
            .id(1L)
            .caseNumber("caseNumber1")
            .caseYear("caseYear1")
            .courtCircuit("courtCircuit1")
            .attorneyNumber("attorneyNumber1")
            .attorneyYear(1)
            .attorneyAuthentication("attorneyAuthentication1")
            .opponentName("opponentName1")
            .opponentDescription("opponentDescription1")
            .opponentAddress("opponentAddress1")
            .subject("subject1")
            .notes("notes1");
    }

    public static CourtCase getCourtCaseSample2() {
        return new CourtCase()
            .id(2L)
            .caseNumber("caseNumber2")
            .caseYear("caseYear2")
            .courtCircuit("courtCircuit2")
            .attorneyNumber("attorneyNumber2")
            .attorneyYear(2)
            .attorneyAuthentication("attorneyAuthentication2")
            .opponentName("opponentName2")
            .opponentDescription("opponentDescription2")
            .opponentAddress("opponentAddress2")
            .subject("subject2")
            .notes("notes2");
    }

    public static CourtCase getCourtCaseRandomSampleGenerator() {
        return new CourtCase()
            .id(longCount.incrementAndGet())
            .caseNumber(UUID.randomUUID().toString())
            .caseYear(UUID.randomUUID().toString())
            .courtCircuit(UUID.randomUUID().toString())
            .attorneyNumber(UUID.randomUUID().toString())
            .attorneyYear(intCount.incrementAndGet())
            .attorneyAuthentication(UUID.randomUUID().toString())
            .opponentName(UUID.randomUUID().toString())
            .opponentDescription(UUID.randomUUID().toString())
            .opponentAddress(UUID.randomUUID().toString())
            .subject(UUID.randomUUID().toString())
            .notes(UUID.randomUUID().toString());
    }
}
