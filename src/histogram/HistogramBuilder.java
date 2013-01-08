package histogram;

public class HistogramBuilder {
    public final int NUM_THREADS = 10;
    
    public int[] buildHistogram(String string) throws InterruptedException {
        int[] result = new int[128];
        Thread[] threads = new Thread[NUM_THREADS];
        int[][] partialResults = new int[NUM_THREADS][128];
        
        for (int i=0; i<NUM_THREADS; ++i) {
            PartialHistogramBuilder partial = new PartialHistogramBuilder(string, i, partialResults[i]);
            threads[i] = new Thread(partial);
            threads[i].start();
        }
        
        for (int i=0; i<NUM_THREADS; ++i) {
            threads[i].join();
        }
        
        for (int i=0; i<NUM_THREADS; ++i) {
        	for (char c=0; c<128; ++c) {
        		result[c] += partialResults[i][c];
        	}
        }
        
        return result;
    }

    class PartialHistogramBuilder implements Runnable {
        private String string;
        private int blockNumber;
		private int[] partialResults;

        public PartialHistogramBuilder(String string, int blockNumber, int[] partialResults) {
            this.string = string;
            this.blockNumber = blockNumber;
            this.partialResults = partialResults;
        }

        @Override
        public void run() {
          int start = (int) ((blockNumber * (long) string.length()) / NUM_THREADS);
          int end = (int) (((blockNumber + 1) * (long) string.length()) / NUM_THREADS);
          for (int i=start; i<end; ++i) {
              ++partialResults[string.charAt(i)];
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
