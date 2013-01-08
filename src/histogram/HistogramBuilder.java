package histogram;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Scanner;

public class HistogramBuilder {

    public int[] buildHistogram(String string) {
        int[] result = new int[128];
        
        for (char c : string.toCharArray()) {
            ++result[c];
        }
        
        return result;
    }
    
    public static void main(String[] args) throws FileNotFoundException {
        HistogramBuilder builder = new HistogramBuilder();
        String text = new Scanner( new File("test/histogram/AsYouLikeIt.txt"), "UTF-8" ).useDelimiter("\\A").next();
        int[] histogram = builder.buildHistogram(text);
        System.out.println(Arrays.toString(histogram));
    }

}
