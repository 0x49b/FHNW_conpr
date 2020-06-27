package as.examples;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicIntegerArray;

import as.conbench.Benchmark;
import as.conbench.Threads;

public class CacheEffects2 {

    static final int N_TIMES = 1000;
    static final int INCREMENTS = 100000;

    @Benchmark(N_TIMES)
    public static class FalseSharing {
        public final AtomicIntegerArray aia = new AtomicIntegerArray(2);

        @Threads({1})
        public void accessA(int nTimes, int nThreads) {
            for (int i = 0; i < nTimes; i++) {
                for (int j = 0; j < INCREMENTS; j++) {
                    aia.set(0, aia.get(0) + 1);
                }
            }
        }

        @Threads({1})
        public void accessB(int nTimes, int nThreads) {
            for (int i = 0; i < nTimes; i++) {
                for (int j = 0; j < INCREMENTS; j++) {
                    aia.set(1, aia.get(1) + 1);
                }
            }
        }
    }

    @Benchmark(N_TIMES)
    public static class PaddedVariables {
        public final AtomicInteger[] aia = new AtomicInteger[2];

        {
            aia[0] = new AtomicInteger(0);
            aia[1] = new AtomicInteger(0);
        }

        @Threads({1})
        public void accessA(int nTimes, int nThreads) {
            for (int i = 0; i < nTimes; i++) {
                for (int j = 0; j < INCREMENTS; j++) {
                    AtomicInteger ai = aia[0];
                    ai.set(ai.get() + 1);
                }
            }
        }

        @Threads({1})
        public void accessB(int nTimes, int nThreads) {
            for (int i = 0; i < nTimes; i++) {
                for (int j = 0; j < INCREMENTS; j++) {
                    AtomicInteger ai = aia[1];
                    ai.set(ai.get() + 1);
                }
            }
        }
    }
}
