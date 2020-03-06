package nsu.neural.networks.lab;

import java.io.BufferedReader;
import java.io.Console;
import java.io.IOException;
import java.io.InputStreamReader;

public class TreeTester {
    private TreeNode rootNode;

    public TreeTester(TreeNode rootNode) {
        this.rootNode = rootNode;
    }

    TreeTester() {
    }

    void setRootNode(TreeNode rootNode) {
        this.rootNode = rootNode;
    }

    PlayClass getPlayClass(Example example) {
        if (rootNode instanceof ClassNode) {
            return ((ClassNode) rootNode).getPlayClass();
        }
        AttributeNode attributeNode = (AttributeNode) rootNode;
        String attributeValue = example.getAttributeValue(attributeNode.getAttributeValue());
        return getPlayClass(example, attributeNode.getNode(attributeValue));
    }

    private PlayClass getPlayClass(Example example, TreeNode currentNode) {
        if (currentNode instanceof ClassNode) {
            return ((ClassNode) currentNode).getPlayClass();
        }
        AttributeNode attributeNode = (AttributeNode) currentNode;
        String attributeValue = example.getAttributeValue(attributeNode.getAttributeValue());
        return getPlayClass(example, attributeNode.getNode(attributeValue));
    }

    public void startInteractiveTest() throws IOException {
        System.out.println("Enter no to stop");
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String line = "";
        Attribute[] attributes = Attribute.values();
        while (!line.equalsIgnoreCase("no")) {
            Example example = new Example();
            System.out.println("-----------------");
            for (Attribute attribute : attributes) {
                System.out.println("Enter " + attribute + " parameter");
                line = reader.readLine();
                if (line.equalsIgnoreCase("no")) {
                    return;
                }
                example.addAttributeValue(attribute, line);
            }
            System.out.println("Answer is " + getPlayClass(example));
        }
    }
}
