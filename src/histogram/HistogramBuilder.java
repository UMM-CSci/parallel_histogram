package histogram;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class HistogramBuilder {
    public final int NUM_THREADS = 10;
    
    public int[] buildHistogram(String string) throws InterruptedException {
        int[] result = new int[128];
        Lock[] locks = new ReentrantLock[128];
        for (int i=0; i<locks.length; ++i) {
        	locks[i] = new ReentrantLock();
        }
        Thread[] threads = new Thread[NUM_THREADS];
        
        for (int i=0; i<NUM_THREADS; ++i) {
            PartialHistogramBuilder partial = new PartialHistogramBuilder(string, result, i, locks);
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
        private int[] result;
        private int blockNumber;
		private Lock[] locks;

        public PartialHistogramBuilder(String string, int[] result, int blockNumber, Lock[] locks) {
            this.string = string;
            this.result = result;
            this.blockNumber = blockNumber;
            this.locks = locks;
        }

        @Override
        public void run() {
          int start = (blockNumber * string.length()) / NUM_THREADS;
          int end = ((blockNumber + 1) * string.length()) / NUM_THREADS;
          for (int i=start; i<end; ++i) {
        	  int c = string.charAt(i);
        	  locks[c].lock();  // block until condition holds
        	  try {
                  ++result[c];
        	  } finally {
        		  locks[c].unlock();
        	  }
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
