package as.examples;

import java.util.Arrays;
import java.util.Random;

import as.conbench.Benchmark;
import as.conbench.Threads;


//http://stackoverflow.com/questions/11227809/why-is-processing-a-sorted-array-faster-than-an-unsorted-array
final public class BranchPrediction {
    static final int N_TIMES = 100_000;

    static int[] setup() {
        int arraySize = 32768;
        int[] data = new int[arraySize];
        Random rnd = new Random(0);
        for (int c = 0; c < arraySize; c++) {
            data[c] = rnd.nextInt() % 256;
        }
        return data;
    }

    static long sumElems(int[] data) {
        long sum = 0;
        for (int c = 0; c < data.length; ++c) {
            if (data[c] >= 128) {
                sum += data[c];
            }
        }
        return sum;
    }

    @Benchmark(N_TIMES)
    public static class UnsortedArray {
        int[] data = null;

        public UnsortedArray() {
            data = setup();
        }

        @Threads({1})
        public void accessA(int nTimes, int nThreads) {
            for (int i = 0; i < nTimes; i++) {
                sumElems(data);
            }
        }
    }

    @Benchmark(N_TIMES)
    public static class SortedArray {
        int[] data = null;

        public SortedArray() {
            data = setup();
            Arrays.sort(data);
        }

        @Threads({1})
        public void accessA(int nTimes, int nThreads) {
            for (int i = 0; i < nTimes; i++) {
                sumElems(data);
            }
        }
    }
}
