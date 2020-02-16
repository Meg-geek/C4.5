package nsu.neural.networks.lab;

import org.junit.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.Assert.*;

public class ExcelReaderTest {

    @Test
    public void getExamplesFromFile() throws IOException {
        ExcelReader excelReader = new ExcelReader();
        assertEquals(14, excelReader.getExamplesFromFile("Test.xlsx").size());
    }
}