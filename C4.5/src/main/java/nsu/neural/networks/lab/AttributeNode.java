package nsu.neural.networks.lab;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class AttributeNode extends TreeNode {
    private Map<String, TreeNode> valueNodeMap = new HashMap<>();
    private Attribute attributeValue;
    private PlayClass freqClass;

    AttributeNode(Attribute attributeValue, PlayClass freqClass) {
        this.attributeValue = attributeValue;
        this.freqClass = freqClass;
    }

    AttributeNode(Attribute attributeValue, PlayClass freqClass, TreeNode parentNode) {
        super(parentNode);
        this.attributeValue = attributeValue;
        this.freqClass = freqClass;
    }

    void addNode(TreeNode node, String value) {
        if (!valueNodeMap.containsKey(value)) {
            valueNodeMap.put(value, node);
        }
    }

    @Override
    PlayClass getMostFrequentlyClass() {
        return freqClass;
    }

    Attribute getAttributeValue() {
        return attributeValue;
    }

    TreeNode getNode(String value) {
        return valueNodeMap.get(value);
    }
}
