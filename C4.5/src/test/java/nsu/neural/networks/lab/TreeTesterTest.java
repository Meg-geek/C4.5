package nsu.neural.networks.lab;

import com.sun.source.tree.Tree;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class TreeTesterTest {
    private List<Example> examples;
    private TreeTester tester = new TreeTester();

    @Before
    public void setUp() throws Exception {
        ExcelReader excelReader = new ExcelReader();
        examples = excelReader.getExamplesFromFile("Test.xlsx");
        AlgorithmC4point5 algorithm = new AlgorithmC4point5();
        tester.setRootNode(algorithm.getSolutionTree(examples));
    }

    @Test
    public void getPlayClass() {
        for (Example example : examples) {
            PlayClass treeResult = tester.getPlayClass(example);
            System.out.println("------------");
            System.out.println("solution result is " + treeResult);
            System.out.println("example result was " + example.getPlayClass());
            assertEquals(treeResult, example.getPlayClass());
        }
    }
}