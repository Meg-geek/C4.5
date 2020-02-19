package nsu.neural.networks.lab;

import java.util.HashMap;
import java.util.Map;

public class Example {
    private Map<Attribute, String> attributeValues = new HashMap<>();
    private PlayClass playClass = null;

    void addAttributeValue(Attribute attribute, String value) {
        if (!attributeValues.containsKey(attribute) && value != null) {
            attributeValues.put(attribute, value);
        }
    }

    void setResultClass(PlayClass playClass) {
        if (this.playClass == null) {
            this.playClass = playClass;
        }
    }

    String getAttributeValue(Attribute attribute) {
        return attributeValues.get(attribute);
    }

    PlayClass getPlayClass() {
        return playClass;
    }
}
