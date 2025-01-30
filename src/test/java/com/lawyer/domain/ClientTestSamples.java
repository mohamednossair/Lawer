package com.lawyer.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class ClientTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Client getClientSample1() {
        return new Client()
            .id(1L)
            .clientName("clientName1")
            .clientDescription("clientDescription1")
            .contactNumber("contactNumber1")
            .address("address1")
            .nationalId("nationalId1")
            .email("email1");
    }

    public static Client getClientSample2() {
        return new Client()
            .id(2L)
            .clientName("clientName2")
            .clientDescription("clientDescription2")
            .contactNumber("contactNumber2")
            .address("address2")
            .nationalId("nationalId2")
            .email("email2");
    }

    public static Client getClientRandomSampleGenerator() {
        return new Client()
            .id(longCount.incrementAndGet())
            .clientName(UUID.randomUUID().toString())
            .clientDescription(UUID.randomUUID().toString())
            .contactNumber(UUID.randomUUID().toString())
            .address(UUID.randomUUID().toString())
            .nationalId(UUID.randomUUID().toString())
            .email(UUID.randomUUID().toString());
    }
}
