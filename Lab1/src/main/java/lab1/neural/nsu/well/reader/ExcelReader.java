package lab1.neural.nsu.well.reader;


import lab1.neural.nsu.well.ResultClass;
import lab1.neural.nsu.well.ValueHolder;
import lab1.neural.nsu.well.ValueType;
import lab1.neural.nsu.well.WellParameters;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ExcelReader {
    private final int HEADER_LINES_AMOUNT = 3;
    private final int VU_SHEET_INDEX = 1;
    private final int FIRST_RESULT_CLASS_INDEX = 29;

    public List<WellParameters> getParametersFromFile(String filePath) throws IOException {
        FileInputStream fileInputStream = new FileInputStream(new File(filePath));
        XSSFWorkbook workbook = new XSSFWorkbook(fileInputStream);
        XSSFSheet sheet = workbook.getSheetAt(VU_SHEET_INDEX);
        Iterator<Row> rowIterator = sheet.iterator();
        List<WellParameters> parameters = new ArrayList<>();
        skipHeaders(rowIterator);
        while (rowIterator.hasNext()) {
            parameters.add(getWellParameters(rowIterator.next().cellIterator()));
        }
        return parameters;
    }

    private void skipHeaders(Iterator<Row> rowIterator) {
        for (int i = 0; i < HEADER_LINES_AMOUNT; i++) {
            rowIterator.next();
        }
    }

    private WellParameters getWellParameters(Iterator<Cell> cellIterator) {
        WellParameters wellParameters = new WellParameters();
        if (cellIterator.hasNext()) {
            Cell cell = cellIterator.next();
            setWellNumber(wellParameters, cell);
        }
        if (cellIterator.hasNext()) {
            Cell cell = cellIterator.next();
            setWellDate(wellParameters, cell);
        }
        for (int i = 0; i < FIRST_RESULT_CLASS_INDEX && cellIterator.hasNext(); i++) {
            Cell cell = cellIterator.next();
            wellParameters.addParameter(getValueHolderFromCell(cell));
        }
        wellParameters.setResultClass(getResultClass(cellIterator));
        return wellParameters;
    }

    private ResultClass getResultClass(Iterator<Cell> cellIterator) {
        List<ValueHolder> resultHolders = new ArrayList<>();
        while(cellIterator.hasNext()){
            resultHolders.add(getValueHolderFromCell(cellIterator.next()));
        }
        return ResultClass.getResultClassFromHolders(resultHolders);
    }

    private void setWellNumber(WellParameters wellParameters, Cell cell) {
        if (wellParameters != null &&
                cell != null &&
                cell.getCellType() == CellType.NUMERIC) {
            wellParameters.setNumber((int) cell.getNumericCellValue());
        }
    }

    private void setWellDate(WellParameters wellParameters, Cell cell) {
        if (wellParameters != null &&
                cell != null &&
                cell.getCellType() != CellType.STRING) {
            wellParameters.setDate(cell.getDateCellValue());
        }
    }

    private ValueHolder getValueHolderFromCell(Cell cell) {
        if (cell.getCellType() == CellType.NUMERIC) {
            return new ValueHolder((float) cell.getNumericCellValue(),
                    ValueType.FLOAT);
        }
        if (cell.getCellType() == CellType.STRING &&
                isFloat(cell.getStringCellValue())) {
            float value = Float.parseFloat(cell.getStringCellValue());
            return new ValueHolder(value, ValueType.FLOAT);
        }
        if (cell.getCellType() == CellType.FORMULA) {
            return getCellFormulaValueHolder(cell);
        }
        return ValueHolder.getEmptyValueHolder();
    }

    private ValueHolder getCellFormulaValueHolder(Cell cell) {
        if (cell.getCachedFormulaResultType() == CellType.NUMERIC) {
            return new ValueHolder((float) cell.getNumericCellValue(),
                    ValueType.FLOAT);
        }
        return ValueHolder.getEmptyValueHolder();
    }

    private boolean isFloat(String line) {
        try {
            Float.parseFloat(line);
        } catch (NumberFormatException ex) {
            return false;
        }
        return true;
    }
}

