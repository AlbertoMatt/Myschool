package com.mycompany.myapp.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

public class AlunnoCompitoTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static AlunnoCompito getAlunnoCompitoSample1() {
        return new AlunnoCompito().id(1L);
    }

    public static AlunnoCompito getAlunnoCompitoSample2() {
        return new AlunnoCompito().id(2L);
    }

    public static AlunnoCompito getAlunnoCompitoRandomSampleGenerator() {
        return new AlunnoCompito().id(longCount.incrementAndGet());
    }
}
