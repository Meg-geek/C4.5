package nsu.neural.networks.lab;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AlgorithmC4point5 {
    private TreeNode firstNode;
    private final int LOG_BASE = 2;


    private float calculateGainRatio(Attribute attribute, List<Example> examples){
        Map<String, List<Example>> valuesExamples = groupByAttribute(attribute, examples);
        return calculateGain(examples, valuesExamples)
                /calculateSplitInfo(valuesExamples, examples.size());
    }

    private float calculateGain(List<Example> examples, Map<String, List<Example>> valuesExamples){
        return calculateInfo(examples) - calculateAttributesInfo(valuesExamples, examples.size());
    }

    private float calculateInfo(List<Example> examples){
        float sum = 0f;
        int yesAmount = 0, allAmount = examples.size();
        for(Example example : examples){
            if(example.getPlayClass() == PlayClass.YES){
                yesAmount++;
            }
        }
        float yesCoef = yesAmount/(float)allAmount, noCoef = (allAmount - yesAmount)/(float)allAmount;
        sum += yesCoef * (Math.log(yesCoef) / Math.log(LOG_BASE));
        sum += noCoef * (Math.log(noCoef) / Math.log(LOG_BASE));
        return (-sum);
    }

    private float calculateAttributesInfo(Map<String, List<Example>> valuesExamples, int allSize){
        float sum = 0f;
        for(List<Example> examples : valuesExamples.values()){
            sum+=examples.size()/(float)allSize * calculateInfo(examples);
        }
        return sum;
    }

    private Map<String, List<Example>> groupByAttribute(Attribute attribute, List<Example> examples){
        Map<String, List<Example>> valueExamplesMap = new HashMap<>();
        for(Example example : examples){
            String attributeValue = example.getAttributeValues(attribute);
            if(attributeValue != null){
                if(valueExamplesMap.containsKey(attributeValue)){
                    valueExamplesMap.get(attributeValue).add(example);
                } else {
                    valueExamplesMap.put(attributeValue, new ArrayList<>(){{add(example);}});
                }
            }
        }
        return valueExamplesMap;
    }

    private float calculateSplitInfo(Map<String, List<Example>> valueExamplesMap,
                                     int allExamplesAmount){
        float sum = 0f;
        for( Map.Entry<String, List<Example>> valueExamples: valueExamplesMap.entrySet()){
            int examplesAmount = valueExamples.getValue().size();
            sum += (examplesAmount / (float)allExamplesAmount) * Math.log(examplesAmount)/ Math.log(LOG_BASE);
        }
        return sum;
    }
}
