package com.proyek.akhir.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class HistoryTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static History getHistorySample1() {
        return new History().id(1L).predictionInput("predictionInput1").predictionResult("predictionResult1");
    }

    public static History getHistorySample2() {
        return new History().id(2L).predictionInput("predictionInput2").predictionResult("predictionResult2");
    }

    public static History getHistoryRandomSampleGenerator() {
        return new History()
            .id(longCount.incrementAndGet())
            .predictionInput(UUID.randomUUID().toString())
            .predictionResult(UUID.randomUUID().toString());
    }
}
