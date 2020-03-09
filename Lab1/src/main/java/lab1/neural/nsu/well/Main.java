package lab1.neural.nsu.well;

import lab1.neural.nsu.well.reader.ExcelReader;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        ExcelReader reader = new ExcelReader();
        reader.getParametersFromFile("dataset.xlsx");
    }
}
