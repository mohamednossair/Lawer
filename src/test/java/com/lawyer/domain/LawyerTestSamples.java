package com.lawyer.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class LawyerTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Lawyer getLawyerSample1() {
        return new Lawyer()
            .id(1L)
            .lawyerName("lawyerName1")
            .address("address1")
            .contactNumber("contactNumber1")
            .specialization("specialization1")
            .email("email1")
            .registrationNumber("registrationNumber1");
    }

    public static Lawyer getLawyerSample2() {
        return new Lawyer()
            .id(2L)
            .lawyerName("lawyerName2")
            .address("address2")
            .contactNumber("contactNumber2")
            .specialization("specialization2")
            .email("email2")
            .registrationNumber("registrationNumber2");
    }

    public static Lawyer getLawyerRandomSampleGenerator() {
        return new Lawyer()
            .id(longCount.incrementAndGet())
            .lawyerName(UUID.randomUUID().toString())
            .address(UUID.randomUUID().toString())
            .contactNumber(UUID.randomUUID().toString())
            .specialization(UUID.randomUUID().toString())
            .email(UUID.randomUUID().toString())
            .registrationNumber(UUID.randomUUID().toString());
    }
}
