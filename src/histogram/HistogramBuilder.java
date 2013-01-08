package histogram;

public class HistogramBuilder {
    public final int NUM_THREADS = 10;
    
    public int[] buildHistogram(String string) {
        int[] result = new int[128];
        
        for (int i=0; i<NUM_THREADS; ++i) {
            PartialHistogramBuilder partial = new PartialHistogramBuilder(string, result, i);
            Thread thread = new Thread(partial);
            thread.start();
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
          int start = string.length() / NUM_THREADS * blockNumber;
          int end = string.length() / NUM_THREADS * (blockNumber + 1);
          for (int i=start; i<end; ++i) {
              ++result[string.charAt(i)];
          }
        }
    }
}
