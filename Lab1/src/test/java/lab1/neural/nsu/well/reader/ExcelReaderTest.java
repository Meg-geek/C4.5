package lab1.neural.nsu.well.reader;

import lab1.neural.nsu.well.WellParameters;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.Assert.*;

public class ExcelReaderTest {
    private ExcelReader excelReader;

    @Before
    public void setUp(){
        excelReader = new ExcelReader();
    }

    @Test
    public void getParametersFromFile() throws IOException {
        List<WellParameters> parameters = excelReader.getParametersFromFile("dataset.xlsx");
        for(WellParameters wellParameters : parameters){
            assertNotNull(wellParameters.getResultClass());
            assertEquals(WellParameters.PARAMETERS_AMOUNT, wellParameters.getParameters().size());
        }
    }
}