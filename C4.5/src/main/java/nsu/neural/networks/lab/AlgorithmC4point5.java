package nsu.neural.networks.lab;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AlgorithmC4point5 {
    private final int LOG_BASE = 2;
    private final int MIN_EXAMPLES_AMOUNT = 1;

    public TreeNode getSolutionTree(List<Example> examples) {
        return getSolutionTree(examples, null);
    }


    private TreeNode getSolutionTree(List<Example> examples, TreeNode parentNode) {
        if (examples == null) {
            return null;
        }
        if (examples.size() <= MIN_EXAMPLES_AMOUNT) {
            return new ClassNode(parentNode, getMostFrequentlyClass(examples, parentNode));
        }
        List<Pair<Float, Map<String, List<Example>>>> gainAttribute = new ArrayList<>();
        Attribute[] attributes = Attribute.values();
        for (Attribute attribute : Attribute.values()) {
            Pair<Float, Map<String, List<Example>>> gain = calculateGainRatio(attribute, examples);
            gainAttribute.add(gain);
        }
        int bestAttributeIndex = 0;
        float maxGainRatio = 0f;
        boolean allZeroes = true;
        for (int i = 0; i < attributes.length; i++) {
            float gainRatio = gainAttribute.get(i).getFirstEl();
            if (gainRatio > maxGainRatio) {
                maxGainRatio = gainAttribute.get(i).getFirstEl();
                bestAttributeIndex = i;
            }
            if (gainRatio != 0) {
                allZeroes = false;
            }
        }
        AttributeNode attributeNode;
        if (allZeroes) {
            return new ClassNode(parentNode, examples.get(0).getPlayClass());
        }
        if (parentNode == null) {
            attributeNode = new AttributeNode(attributes[bestAttributeIndex],
                    getMostFrequentlyClass(examples, parentNode));
        } else {
            attributeNode = new AttributeNode(attributes[bestAttributeIndex],
                    getMostFrequentlyClass(examples, parentNode), parentNode);
        }
        for (Map.Entry<String, List<Example>> attributeExamples :
                gainAttribute.get(bestAttributeIndex).getSecondEl().entrySet()) {
            TreeNode nodeToAdd = getSolutionTree(attributeExamples.getValue(), attributeNode);
            attributeNode.addNode(nodeToAdd,
                    attributeExamples.getKey());
        }
        return attributeNode;
    }


    private PlayClass getMostFrequentlyClass(List<Example> examples, TreeNode parentNode) {
        int yesAmount = 0;
        for (Example example : examples) {
            if (example.getPlayClass() == PlayClass.YES) {
                yesAmount++;
            }
        }
        if (yesAmount == examples.size() - yesAmount && parentNode != null) {
            return parentNode.getMostFrequentlyClass();
        }
        if (yesAmount > examples.size() - yesAmount) {
            return PlayClass.YES;
        }
        return PlayClass.NO;
    }

    private Pair<Float, Map<String, List<Example>>> calculateGainRatio(Attribute attribute, List<Example> examples) {
        Map<String, List<Example>> valuesExamples = groupByAttribute(attribute, examples);
        float gain = calculateGain(examples, valuesExamples);
        float splitInfo = calculateSplitInfo(valuesExamples, examples.size());
        float gainRatio = (splitInfo == 0) ? 0 : gain / splitInfo;
        return new Pair<>(gainRatio, valuesExamples);
    }

    private float calculateGain(List<Example> examples, Map<String, List<Example>> valuesExamples) {
        float calcInfo = calculateInfo(examples);
        float attrInfo = calculateAttributesInfo(valuesExamples, examples.size());
        return calcInfo - attrInfo;
    }

    private float calculateInfo(List<Example> examples) {
        float sum = 0f;
        int yesAmount = 0, allAmount = examples.size();
        for (Example example : examples) {
            if (example.getPlayClass() == PlayClass.YES) {
                yesAmount++;
            }
        }
        float yesCoef = yesAmount / (float) allAmount, noCoef = (allAmount - yesAmount) / (float) allAmount;
        if (yesCoef == 0 || noCoef == 0) {
            return 0;
        }
        sum += yesCoef * (Math.log(yesCoef) / Math.log(LOG_BASE));
        sum += noCoef * (Math.log(noCoef) / Math.log(LOG_BASE));
        return (-sum);
    }

    private float calculateAttributesInfo(Map<String, List<Example>> valuesExamples, int allSize) {
        float sum = 0f;
        for (List<Example> examples : valuesExamples.values()) {
            float calcInfo = calculateInfo(examples);
            sum += examples.size() / (float) allSize * calcInfo;
        }
        return sum;
    }

    private Map<String, List<Example>> groupByAttribute(Attribute attribute, List<Example> examples) {
        Map<String, List<Example>> valueExamplesMap = new HashMap<>();
        for (Example example : examples) {
            String attributeValue = example.getAttributeValue(attribute);
            if (attributeValue != null) {
                if (valueExamplesMap.containsKey(attributeValue)) {
                    valueExamplesMap.get(attributeValue).add(example);
                } else {
                    valueExamplesMap.put(attributeValue, new ArrayList<>() {{
                        add(example);
                    }});
                }
            }
        }
        return valueExamplesMap;
    }

    private float calculateSplitInfo(Map<String, List<Example>> valueExamplesMap,
                                     int allExamplesAmount) {
        float sum = 0f;
        for (Map.Entry<String, List<Example>> valueExamples : valueExamplesMap.entrySet()) {
            int examplesAmount = valueExamples.getValue().size();
            sum += (examplesAmount / (float) allExamplesAmount) * Math.log(examplesAmount) / Math.log(LOG_BASE);
        }
        return sum;
    }
}

class Pair<F, S> {
    private F firstEl;
    private S secondEl;

    Pair(F first, S second) {
        this.firstEl = first;
        this.secondEl = second;
    }

    F getFirstEl() {
        return firstEl;
    }

    S getSecondEl() {
        return secondEl;
    }
}