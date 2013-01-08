package histogram;

public class HistogramBuilder {
    public final int NUM_THREADS = 10;
    
    public int[] buildHistogram(String string) throws InterruptedException {
        int[] result = new int[128];
        Thread[] threads = new Thread[NUM_THREADS];
        
        for (int i=0; i<NUM_THREADS; ++i) {
            PartialHistogramBuilder partial = new PartialHistogramBuilder(string, result, i);
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

        public PartialHistogramBuilder(String string, int[] result, int blockNumber) {
            this.string = string;
            this.result = result;
            this.blockNumber = blockNumber;
        }

        @Override
        public void run() {
          int start = (blockNumber * string.length()) / NUM_THREADS;
          int end = ((blockNumber + 1) * string.length()) / NUM_THREADS;
          for (int i=start; i<end; ++i) {
              ++result[string.charAt(i)];
          }
        }
    }
}
