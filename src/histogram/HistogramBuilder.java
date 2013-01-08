package histogram;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class HistogramBuilder {
    public final int NUM_THREADS = 10;
    
    public int[] buildHistogram(String string) throws InterruptedException {
        int[] result = new int[128];
        Thread[] threads = new Thread[NUM_THREADS];
        Lock resultLock = new ReentrantLock();
        
        for (int i=0; i<NUM_THREADS; ++i) {
            PartialHistogramBuilder partial = new PartialHistogramBuilder(string, i, result, resultLock);
            threads[i] = new Thread(partial);
            threads[i].start();
        }
        
        for (int i=0; i<NUM_THREADS; ++i) {
            threads[i].join();
        }
        
        return result;
    }

    class PartialHistogramBuilder implements Runnable {
        private String string;
        private int blockNumber;
        private Lock resultLock;
        private int[] result;
		private int[] partialResults;

        public PartialHistogramBuilder(String string, int blockNumber, int[] result, Lock resultLock) {
            this.string = string;
            this.blockNumber = blockNumber;
            this.resultLock = resultLock;
            this.result = result;
            partialResults = new int[result.length];
        }

        @Override
        public void run() {
          int start = (int) ((blockNumber * (long) string.length()) / NUM_THREADS);
          int end = (int) (((blockNumber + 1) * (long) string.length()) / NUM_THREADS);
          for (int i=start; i<end; ++i) {
              ++partialResults[string.charAt(i)];
          }
          resultLock.lock();  // block until condition holds
          try {
            for (int i=0; i<result.length; ++i) {
            	result[i] += partialResults[i];
            }
          } finally {
        	  resultLock.unlock();
          }
        }
    }
    
    public static void main(String[] args) throws InterruptedException {
        final int NUM_Xs = 50000000;
        final int NUM_WARMUPS = 10;
        final int NUM_RUNS_TO_COUNT = 100;
        
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < NUM_Xs; ++i) {
            builder.append('x');
        }
        int[] histogram = null;

        for (int i=0; i<NUM_WARMUPS; ++i) {
            HistogramBuilder histogramBuilder = new HistogramBuilder();
            histogram = histogramBuilder.buildHistogram(builder.toString());
            printHistCount(histogram);
        }

        long startTime = System.nanoTime();
        for (int i=0; i<NUM_RUNS_TO_COUNT; ++i) {
            HistogramBuilder histogramBuilder = new HistogramBuilder();
            histogram = histogramBuilder.buildHistogram(builder.toString());
            printHistCount(histogram);
        }
        long endTime = System.nanoTime();
        long duration = endTime - startTime;
        System.out.println("\nGenerating the histograms took " + (duration / 1000000000.0) + " seconds.");
    }

    private static void printHistCount(int[] histogram) {
        int count = 0;
        for (int i=0; i<histogram.length; ++i) {
            count += histogram[i];
        }
        System.out.print('.');
//        System.out.println("The total number of counts was " + count + ".");
    }
}
