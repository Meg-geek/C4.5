package lab1.neural.nsu.well.calculators;

import lab1.neural.nsu.well.ResultClass;
import lab1.neural.nsu.well.ValueHolder;
import lab1.neural.nsu.well.WellParameters;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.lang.Math.log;


public class GainRatioCalculator {
    private final int LOG_BASE = 2;
    private int notEmptyResultClassesSize = 0;

    public List<Float> calculateGainRatio(List<WellParameters> examples) {
        List<Float> gainRatios = new ArrayList<>();
        setNotEmptyResultClassesSize(examples);
        for (int i = 0; i < WellParameters.PARAMETERS_AMOUNT; i++) {
            gainRatios.add(calculateGainRatioForAttribute(i, examples));
        }
        return gainRatios;
    }

    private void setNotEmptyResultClassesSize(List<WellParameters> examples){
        int emptyAmount = 0;
        for(WellParameters wellParameters : examples){
            if(wellParameters.getResultClass().isEmpty()){
                emptyAmount++;
            }
        }
        this.notEmptyResultClassesSize = examples.size() - emptyAmount;
    }

    private float calculateGainRatioForAttribute(int attributeIndex, List<WellParameters> examples) {
        Map<ValueHolder, List<WellParameters>> valuesExamples = groupByAttribute(attributeIndex, examples);
        float gain = calculateGain(examples, valuesExamples);
        float splitInfo = calculateSplitInfo(valuesExamples, examples.size());
        return (splitInfo == 0) ? 0 : gain / splitInfo;
    }

    private float calculateSplitInfo(Map<ValueHolder, List<WellParameters>> valueExamples,
                                     int allExamplesAmount) {
        float sum = 0f;
        for (Map.Entry<ValueHolder, List<WellParameters>> valueExampleEntry : valueExamples.entrySet()) {
            int examplesAmount = valueExampleEntry.getValue().size();
            sum += (examplesAmount / (float) allExamplesAmount) * Math.log(examplesAmount) / Math.log(LOG_BASE);
        }
        return sum;
    }


    private float calculateGain(List<WellParameters> examples,
                                Map<ValueHolder, List<WellParameters>> valuesExamples) {
        float calcInfo = calculateInfo(examples);
        float attrInfo = calculateAttributesInfo(valuesExamples);
        return (notEmptyResultClassesSize/(float)examples.size()) * (calcInfo - attrInfo);
    }

    private float calculateAttributesInfo(Map<ValueHolder, List<WellParameters>> valuesExamples) {
        float sum = 0f;
        for (List<WellParameters> examples : valuesExamples.values()) {
            float calcInfo = calculateInfo(examples);
            sum += examples.size() / (float) notEmptyResultClassesSize * calcInfo;
        }
        return sum;
    }

    private float calculateInfo(List<WellParameters> examples) {
        Map<ResultClass, Integer> resultClassAmount = getResultClasses(examples);
        int emptyResultClassesAmount = resultClassAmount.get(ResultClass.getEmptyResultClass()) != null ?
                resultClassAmount.get(ResultClass.getEmptyResultClass()) : 0;
        int notEmptyExamplesAmount = examples.size() - emptyResultClassesAmount;
        float sum = 0f;
        for(Map.Entry<ResultClass, Integer> classIntegerEntry : resultClassAmount.entrySet()){
            if(classIntegerEntry.getKey().isEmpty()){
                continue;
            }
            float entropy = classIntegerEntry.getValue()/(float)notEmptyExamplesAmount;
            entropy = entropy * ((float)log(entropy)/(float)log(LOG_BASE));
            sum+=entropy;
        }
        return (-sum);
    }


    private Map<ValueHolder, List<WellParameters>> groupByAttribute(int attributeIndex, List<WellParameters> examples) {
        Map<ValueHolder, List<WellParameters>> valuesExamples = new HashMap<>();
        for (WellParameters wellParameters : examples) {
            ValueHolder attributeValue = wellParameters.getParameters().get(attributeIndex);
            if (valuesExamples.containsKey(attributeValue)) {
                valuesExamples.get(attributeValue).add(wellParameters);
            } else {
                valuesExamples.put(attributeValue, new ArrayList<>() {{
                    add(wellParameters);
                }});
            }
        }
        return valuesExamples;
    }

    private Map<ResultClass, Integer> getResultClasses(List<WellParameters> examples) {
        Map<ResultClass, Integer> classAmount = new HashMap<>();
        for (WellParameters wellParameters : examples) {
            ResultClass resultClass = wellParameters.getResultClass();
            classAmount.merge(resultClass, 1, Integer::sum);
        }
        return classAmount;
    }

}
