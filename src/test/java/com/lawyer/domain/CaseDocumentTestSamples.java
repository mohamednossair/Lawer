package com.lawyer.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class CaseDocumentTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static CaseDocument getCaseDocumentSample1() {
        return new CaseDocument().id(1L).documentName("documentName1").documentType("documentType1").filePath("filePath1").uploadedBy(1);
    }

    public static CaseDocument getCaseDocumentSample2() {
        return new CaseDocument().id(2L).documentName("documentName2").documentType("documentType2").filePath("filePath2").uploadedBy(2);
    }

    public static CaseDocument getCaseDocumentRandomSampleGenerator() {
        return new CaseDocument()
            .id(longCount.incrementAndGet())
            .documentName(UUID.randomUUID().toString())
            .documentType(UUID.randomUUID().toString())
            .filePath(UUID.randomUUID().toString())
            .uploadedBy(intCount.incrementAndGet());
    }
}
