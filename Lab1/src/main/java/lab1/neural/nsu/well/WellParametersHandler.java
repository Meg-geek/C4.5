package lab1.neural.nsu.well;

import lab1.neural.nsu.well.calculators.GainRatioCalculator;
import lab1.neural.nsu.well.reader.ExcelReader;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class WellParametersHandler {
    public List<GainParameter> getParameterGainListFromFile(String fileName) throws IOException {
        ExcelReader reader = new ExcelReader();
        GainRatioCalculator gainRatioCalculator = new GainRatioCalculator();
        List<Float> gains = gainRatioCalculator
                .calculateGainRatio(reader.getParametersFromFile(fileName));
        List<String> parameterNames = reader.getParametersNamesFromFile(fileName);
        List<GainParameter> gainParameters = new ArrayList<>();
        for (int i = 0; i < parameterNames.size(); i++) {
            gainParameters.add(new GainParameter(parameterNames.get(i), gains.get(i)));
        }
        return gainParameters;
    }
}
