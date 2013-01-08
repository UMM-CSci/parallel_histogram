package histogram;

public class HistogramBuilder {

    public int[] buildHistogram(String string) {
        int[] result = new int[128];
        
        for (char c : string.toCharArray()) {
            ++result[c];
        }
        
        return result;
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
