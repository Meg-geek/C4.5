package lab1.neural.nsu.well.calculators;

import lab1.neural.nsu.well.WellParameters;
import lab1.neural.nsu.well.reader.ExcelReader;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class GainRatioCalculatorTest {
    private List<WellParameters> examples;
    private GainRatioCalculator gainRatioCalculator;

    @Before
    public void setUp() throws Exception {
        ExcelReader excelReader = new ExcelReader();
        examples = excelReader.getParametersFromFile("dataset.xlsx");
        gainRatioCalculator = new GainRatioCalculator();
    }

    @Test
    public void calculateGainRatio() {
        gainRatioCalculator.calculateGainRatio(examples);
    }
}