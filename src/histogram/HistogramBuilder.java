package histogram;

public class HistogramBuilder {
	public final int NUM_THREADS = 10; // This is also the # of blocks
    
    public int[] buildHistogram(String string) {
        int[] result = new int[128];
        Thread[] threads = new Thread[NUM_THREADS];
        
        for (int i=0; i<NUM_THREADS; ++i) {
        	PartialHistogramBuilder builder = new PartialHistogramBuilder(string, i, result);
        	threads[i] = new Thread(builder);
        	threads[i].start();
        }
        for (int i=0; i<NUM_THREADS; ++i) {
        	try {
				threads[i].join();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }
        return result;
    }
    
    class PartialHistogramBuilder implements Runnable {
    	
    	private String string;
		private int[] result;
		private int blockNumber;

		public PartialHistogramBuilder(String string, int blockNumber, int[] result) {
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
    
    public static void main(String[] args) {
        final int NUM_Xs = 10000000;
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
        System.out.println("Generating the histograms took " + (duration / 1000000000.0) + " seconds.");
    }

    private static void printHistCount(int[] histogram) {
        int count = 0;
        for (int i=0; i<histogram.length; ++i) {
            count += histogram[i];
        }
        System.out.println("The total number of counts was " + count + ".");
    }
}
