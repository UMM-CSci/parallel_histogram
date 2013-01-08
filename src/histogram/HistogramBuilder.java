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

}
