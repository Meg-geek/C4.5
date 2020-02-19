package nsu.neural.networks.lab;

public class ClassNode extends TreeNode {
    private PlayClass playClass;


    ClassNode(TreeNode parentNode, PlayClass playClass){
        super(parentNode);
        this.playClass = playClass;
    }

    PlayClass getPlayClass(){
        return playClass;
    }

    @Override
    PlayClass getMostFrequentlyClass() {
        return getPlayClass();
    }
}
