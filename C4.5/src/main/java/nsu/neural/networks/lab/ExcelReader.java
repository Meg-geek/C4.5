package nsu.neural.networks.lab;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ExcelReader {
    private final int OUTLOOK_INDEX = 0;
    private final int TEMPERATURE_INDEX = 1;
    private final int HUMIDITY_INDEX = 2;
    private final int WIND_INDEX = 3;
    private final int PLAY_INDEX = 4;
    private final int NEED_VALUES_AMOUNT = 5;

    public List<Example> getExamplesFromFile(String filePath) throws IOException {
        FileInputStream fileInputStream = new FileInputStream(new File(filePath));
        XSSFWorkbook workbook = new XSSFWorkbook(fileInputStream);
        XSSFSheet sheet = workbook.getSheetAt(0);
        Iterator<Row> rowIterator = sheet.iterator();
        List<Example> examples = new ArrayList<>();
        //for column names
        if (rowIterator.hasNext()) {
            rowIterator.next();
        }
        while (rowIterator.hasNext()) {
            Row row = rowIterator.next();
            Example example = new Example();
            // For each row, iterate through each columns
            Iterator<Cell> cellIterator = row.cellIterator();
            List<String> cellValues = new ArrayList<>();
            while (cellIterator.hasNext()) {
                Cell cell = cellIterator.next();
                cellValues.add(cell.getStringCellValue());
            }
            if (cellValues.size() >= NEED_VALUES_AMOUNT) {
                example.addAttributeValue(Attribute.OUTLOOK, cellValues.get(OUTLOOK_INDEX));
                example.addAttributeValue(Attribute.HUMIDITY, cellValues.get(HUMIDITY_INDEX));
                example.addAttributeValue(Attribute.TEMPERATURE, cellValues.get(TEMPERATURE_INDEX));
                example.addAttributeValue(Attribute.WIND, cellValues.get(WIND_INDEX));
                if (cellValues.get(PLAY_INDEX).equals("Yes")) {
                    example.setResultClass(PlayClass.YES);
                } else {
                    example.setResultClass(PlayClass.NO);
                }
                examples.add(example);
            }
        }
        return examples;
    }
}
