package lab1.neural.nsu.well;

import lab1.neural.nsu.well.calculators.GainRatioCalculator;
import lab1.neural.nsu.well.reader.ExcelReader;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static java.lang.Math.abs;
import static java.lang.Math.sqrt;
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

    public float[][] getAttributesCorrelations(List<WellParameters> examples) {
        int parametersAmount = examples.get(0).getParameters().size();
        float[][] correlationsArray = new float[parametersAmount][parametersAmount];
        for (int i = 0; i < parametersAmount; i++) {
            for (int j = 0; j < parametersAmount; j++) {
                int notEmptyValuesAmount = parametersAmount;
                float sumX = 0f, sumY = 0f, sumXSq = 0f, sumXY = 0f, sumYSq = 0f;
                for (WellParameters wellParameters : examples) {
                    if (wellParameters.getParameters().get(i).isEmptyValueHolder() &&
                            wellParameters.getParameters().get(j).isEmptyValueHolder()) {
                        notEmptyValuesAmount--;
                    }
                    float Xi = wellParameters.getParameters().get(i).getValue();
                    float Yi = wellParameters.getParameters().get(j).getValue();
                    sumX += Xi;
                    sumY += Yi;
                    sumXSq += Xi * Xi;
                    sumYSq += Yi * Yi;
                    sumXY += Xi * Yi;
                }
                float denominator = (float) sqrt(abs(notEmptyValuesAmount * sumXSq - sumX * sumX))
                        * (float) sqrt(abs(notEmptyValuesAmount * sumYSq - sumY * sumY));
                correlationsArray[i][j] = (notEmptyValuesAmount * sumXY - sumX * sumY) / denominator;
            }
        }
        return correlationsArray;
    }
}
