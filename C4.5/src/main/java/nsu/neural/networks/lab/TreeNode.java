package nsu.neural.networks.lab;

public abstract class TreeNode {
    private TreeNode parentNode = null;

    TreeNode() {
    }

    TreeNode(TreeNode parentNode) {
        this.parentNode = parentNode;
    }

    TreeNode getParentNode() {
        return parentNode;
    }

    abstract PlayClass getMostFrequentlyClass();
}
