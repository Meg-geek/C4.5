package lab1.neural.nsu.well;

import lab1.neural.nsu.well.calculators.GainRatioCalculator;
import lab1.neural.nsu.well.reader.ExcelReader;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static java.util.Collections.emptyList;

public class WellParametersHandler {
    private String fileName;
    private ExcelReader excelReader = new ExcelReader();

    public WellParametersHandler(String fileName) {
        this.fileName = fileName;
    }

    public List<GainParameter> getParameterGainListFromFile() throws IOException {
        if (fileName == null) {
            return emptyList();
        }
        GainRatioCalculator gainRatioCalculator = new GainRatioCalculator();
        List<Float> gains = gainRatioCalculator
                .calculateGainRatio(excelReader.getParametersFromFile(fileName));
        List<String> parameterNames = excelReader.getParametersNamesFromFile(fileName);
        List<GainParameter> gainParameters = new ArrayList<>();
        for (int i = 0; i < parameterNames.size(); i++) {
            gainParameters.add(new GainParameter(parameterNames.get(i), gains.get(i)));
        }
        return gainParameters;
    }

    public List<WellParameters> getExamplesFromFile() throws IOException {
        if (fileName == null) {
            return emptyList();
        }
        return excelReader.getParametersFromFile(fileName);
    }

    public List<String> getParametersNamesFromFile() throws IOException {
        if (fileName == null) {
            return emptyList();
        }
        return excelReader.getParametersNamesFromFile(fileName);
    }

}
