package histogram;

import static org.junit.Assert.*;

import org.junit.Test;

public class HistogramTest {
    @Test
    public void testSmallString() {
        HistogramBuilder histogramBuilder = new HistogramBuilder();
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
        HistogramBuilder histogramBuilder = new HistogramBuilder();
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
}
