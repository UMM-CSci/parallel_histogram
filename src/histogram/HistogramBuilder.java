package histogram;


public class HistogramBuilder {

    public int[] buildHistogram(String string) {
        int[] result = new int[128];
        
        for (char c : string.toCharArray()) {
            ++result[c];
        }
        
        return result;
    }

}
