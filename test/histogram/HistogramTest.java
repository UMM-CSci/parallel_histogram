package histogram;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import org.junit.Before;
import org.junit.Test;

public class HistogramTest {
    private HistogramBuilder histogramBuilder;

    @Before
    public void createBuilder() {
        histogramBuilder = new HistogramBuilder();
    }
    
    @Test
    public void testSmallString() {
        int[] histogram = histogramBuilder.buildHistogram("Morris");
        assertEquals(1, histogram['M']);
        assertEquals(1, histogram['o']);
        assertEquals(2, histogram['r']);
        assertEquals(1, histogram['i']);
        assertEquals(1, histogram['s']);
        for (int i=0; i<128; ++i) {
            if (i != 'M' && i != 'o' && i != 'r' && i != 'i' && i != 's') {
                assertEquals("Count for '" + ((char) i) + "' wasn't zero as expected.", 0, histogram[i]);
            }
        }
    }

    @Test
    public void testAlphabet() {
        String charsToCount = "";
        for (char c = 'a'; c <= 'z'; ++c) {
            charsToCount += c;
        }
        for (char c = 'A'; c <= 'Z'; ++c) {
            charsToCount += c;
        }
        int[] histogram = histogramBuilder.buildHistogram(charsToCount);
        for (char c = 'a'; c <= 'z'; ++c) {
            assertEquals(1, histogram[c]);
        }
        for (char c = 'A'; c <= 'Z'; ++c) {
            assertEquals(1, histogram[c]);
        }
    }
    
    @Test
    public void testAsYouLikeIt() throws FileNotFoundException {
        // Tests that the contents of the play "As you like it" are processed correctly.
        String text = new Scanner( new File("test/histogram/AsYouLikeIt.txt"), "UTF-8" ).useDelimiter("\\A").next();
        int[] expectedCounts = new int[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 5397, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 30282, 130, 0, 0, 0, 0, 4, 494, 0, 0, 0, 0, 1944, 159, 962, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 374, 367, 0, 0, 0, 292, 0, 995, 165, 384, 524, 780, 111, 83, 245, 1474, 90, 97, 694, 129, 648, 916, 80, 73, 645, 675, 547, 299, 97, 259, 4, 113, 0, 9, 0, 9, 0, 0, 0, 6012, 1178, 1495, 3255, 10334, 1740, 1344, 5637, 4920, 43, 716, 3706, 2391, 5331, 7345, 1062, 72, 5114, 5129, 7482, 3024, 931, 1986, 107, 2349, 30, 0, 2, 0, 0, 0};
        int[] histogram = histogramBuilder.buildHistogram(text);
        
        for (int i = 0; i<128; ++i) {
            assertEquals(expectedCounts[i], histogram[i]);
        }
    }
}
