package lab1.neural.nsu.well;

import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertTrue;

public class WellParametersHandlerTest {

    @Test
    public void getAttributesCorrelations() throws IOException {
        WellParametersHandler wellParametersHandler = new WellParametersHandler("dataset.xlsx");
        float[][] correlations = wellParametersHandler
                .getAttributesCorrelations(wellParametersHandler.getExamplesFromFile());
        for (float[] correlation : correlations) {
            for (int j = 0; j < correlations.length; j++) {
                assertTrue(correlation[j] <= 1);
            }
        }
    }
}