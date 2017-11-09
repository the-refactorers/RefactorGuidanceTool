package ait;

public class AIT_TestGenerator {

    AdaptiveInstructionTree tree = new AdaptiveInstructionTree();

    public AIT_TestGenerator() {
        tree.setDescription("An adaptive instruction tree template just for unit test purposes");
        tree.setRefactorMechanic("Unit Testing Template");

        Instruction i1 = new Instruction(1, "Parameter fill test: Dummy method $method is located in dummy $class");
        tree.setFirstInstruction(i1);
    }

    public AdaptiveInstructionTree getAdaptiveInstructionTree()
    {
        return tree;
    }
}
