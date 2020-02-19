import nsu.neural.networks.lab.*;

import java.io.IOException;
import java.util.List;

public class Main {
    public static void main(String[] args) throws IOException {
        ExcelReader excelReader = new ExcelReader();
        List<Example> examples = excelReader.getExamplesFromFile("Test.xlsx");
        AlgorithmC4point5 algorithm = new AlgorithmC4point5();
        TreeNode solutionTree = algorithm.getSolutionTree(examples);
        TreeTester treeTester = new TreeTester(solutionTree);
        treeTester.startInteractiveTest();
    }
}
