package lab1.neural.nsu.well.calculators;

import lab1.neural.nsu.well.ResultClass;
import lab1.neural.nsu.well.ValueHolder;
import lab1.neural.nsu.well.WellParameters;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.lang.Math.log;
import static java.util.Collections.singletonList;


public class GainRatioCalculator {
    private final int LOG_BASE = 2;

    public List<Float> calculateGainRatio(List<WellParameters> examples) {
        List<Float> gainRatios = new ArrayList<>();
        for (int i = 0; i < WellParameters.PARAMETERS_AMOUNT; i++) {
            gainRatios.add(calculateGainRatioForAttribute(i, examples));
        }
        gainRatios.add(calculateGainRatioForDate(examples));
        return gainRatios;
    }

    private float calculateGainRatioForDate(List<WellParameters> examples) {
        Map<Date, List<WellParameters>> datesExamples = groupByDate(examples);
        float gain = calculateGainDate(examples, datesExamples);
        float splitInfo = calculateSplitInfoDate(datesExamples, examples.size());
        return (splitInfo == 0) ? 0 : gain / splitInfo;
    }

    private float calculateGainDate(List<WellParameters> examples, Map<Date, List<WellParameters>> datesExamples) {
        float calcInfo = calculateInfoDate(examples);
        float attrInfo = calculateAttributeDateInfo(datesExamples, examples.size());
        return (calcInfo - attrInfo);
    }

    private float calculateSplitInfoDate(Map<Date, List<WellParameters>> datesExamples,
                                         int allExamplesSize) {
        float sum = 0f;
        for (Map.Entry<Date, List<WellParameters>> dateExamplesEntry : datesExamples.entrySet()) {
            int examplesAmount = dateExamplesEntry.getValue().size();
            sum += (examplesAmount / (float) allExamplesSize) * Math.log(examplesAmount) / Math.log(LOG_BASE);
        }
        return sum;
    }

    private float calculateAttributeDateInfo(Map<Date, List<WellParameters>> datesExamples,
                                             int allExamplesSize) {
        float sum = 0f;
        for (List<WellParameters> examples : datesExamples.values()) {
            float calcInfo = calculateInfoDate(examples);
            sum += (examples.size() / (float) allExamplesSize) * calcInfo;
        }
        return sum;
    }

    private float calculateInfoDate(List<WellParameters> examples) {
        Map<ResultClass, Integer> resultClassAmount = getResultClasses(examples);
        float sum = 0f;
        for (Map.Entry<ResultClass, Integer> classIntegerEntry : resultClassAmount.entrySet()) {
            float entropy = classIntegerEntry.getValue() / (float) examples.size();
            entropy = entropy * ((float) log(entropy) / (float) log(LOG_BASE));
            sum += entropy;
        }
        return (-sum);
    }

    private Map<Date, List<WellParameters>> groupByDate(List<WellParameters> examples) {
        Map<Date, List<WellParameters>> datesExamples = new HashMap<>();
        for (WellParameters wellParameters : examples) {
            Date date = wellParameters.getDate();
            datesExamples.merge(date,
                    new ArrayList<>(singletonList(wellParameters)),
                    (list1, list2) ->
                            Stream.of(list1, list2).flatMap(Collection::stream).collect(Collectors.toList()));
        }
        return datesExamples;
    }

    private float calculateGainRatioForAttribute(int attributeIndex, List<WellParameters> examples) {
        Map<ValueHolder, List<WellParameters>> valuesExamples = groupByAttribute(attributeIndex, examples);
        float gain = calculateGain(examples, valuesExamples, attributeIndex);
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
                                Map<ValueHolder, List<WellParameters>> valuesExamples, int attributeIndex) {
        float calcInfo = calculateInfo(examples, attributeIndex);
        float attrInfo = calculateAttributesInfo(examples, valuesExamples, attributeIndex);
        int notEmptyAttributesAmount = examples.size() - getEmptyAttributeAmount(examples, attributeIndex);
        return (notEmptyAttributesAmount / (float) examples.size()) * (calcInfo - attrInfo);
    }

    private float calculateAttributesInfo(List<WellParameters> allExamples,
                                          Map<ValueHolder, List<WellParameters>> valuesExamples,
                                          int attributeIndex) {
        float sum = 0f;
        int notEmptyAttributeValues = allExamples.size() - getEmptyAttributeAmount(allExamples, attributeIndex);
        for (List<WellParameters> examples : valuesExamples.values()) {
            float calcInfo = calculateInfo(examples, attributeIndex);
            sum += (examples.size() / (float) notEmptyAttributeValues) * calcInfo;
        }
        return sum;
    }

    private float calculateInfo(List<WellParameters> examples, int attributeIndex) {
        int notEmptyAttributeValuesAmount = examples.size() - getEmptyAttributeAmount(examples, attributeIndex);
        if (notEmptyAttributeValuesAmount == 0) {
            return 0f;
        }
        Map<ResultClass, Integer> resultClassAmount = getResultClasses(examples);
        float sum = 0f;
        for (Map.Entry<ResultClass, Integer> classIntegerEntry : resultClassAmount.entrySet()) {
            float entropy = classIntegerEntry.getValue() / (float) notEmptyAttributeValuesAmount;
            entropy = entropy * ((float) log(entropy) / (float) log(LOG_BASE));
            sum += entropy;
        }
        return (-sum);
    }

    private int getEmptyAttributeAmount(List<WellParameters> examples, int attributeIndex) {
        int amount = 0;
        for (WellParameters wellParameters : examples) {
            if (wellParameters.getParameters().get(attributeIndex).isEmptyValueHolder()) {
                amount++;
            }
        }
        return amount;
    }

    private Map<ValueHolder, List<WellParameters>> groupByAttribute(int attributeIndex, List<WellParameters> examples) {
        Map<ValueHolder, List<WellParameters>> valuesExamples = new HashMap<>();
        for (WellParameters wellParameters : examples) {
            ValueHolder attributeValue = wellParameters.getParameters().get(attributeIndex);
            if (valuesExamples.containsKey(attributeValue)) {
                valuesExamples.get(attributeValue).add(wellParameters);
            } else {
                valuesExamples.put(attributeValue, new ArrayList<>(singletonList(wellParameters)));
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
