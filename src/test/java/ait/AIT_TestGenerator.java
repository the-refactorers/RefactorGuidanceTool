package ait;

public class AIT_TestGenerator {

    AdaptiveInstructionTree tree = new AdaptiveInstructionTree();

    public AIT_TestGenerator() {

    }

    public AdaptiveInstructionTree getAdaptiveInstructionTree()
    {
        tree.setDescription("An adaptive instruction tree template just for unit test purposes");
        tree.setRefactorMechanic("Unit Testing Template");

        Instruction i1 = new Instruction(1, "Parameter fill test: Dummy method $method is located in dummy $class");
        ContextDecision i1_d1 = new ContextDecision(CodeContext.CodeContextEnum.MethodSingleDeclaration, 2);
        ContextDecision i1_d2 = new ContextDecision(CodeContext.CodeContextEnum.MethodOverride, 3);
        i1.addDecision(i1_d1);
        i1.addDecision(i1_d2);

        Instruction i2 = new Instruction( 2, "Instruction 2: Single declaration method $method");
        Instruction i3 = new Instruction(3,"Instruction 3: Overrides class $class");

        tree.addInstruction(i2);
        tree.addInstruction(i3);

        tree.setFirstInstruction(i1);

        return tree;
    }

    public AdaptiveInstructionTree getAdaptiveInstructionTreeWithRiskDescription()
    {
        tree.setDescription("An adaptive instruction tree template just for unit test purposes");
        tree.setRefactorMechanic("Unit Testing Template");

        Instruction i1 = new Instruction(1, "Parameter fill test: Dummy method $method is located in dummy $class");
        ContextDecision i1_d1 = new ContextDecision(CodeContext.CodeContextEnum.MethodSingleDeclaration, 2);
        ContextDecision i1_d2 = new ContextDecision(CodeContext.CodeContextEnum.MethodOverride, 3);
        i1_d2.setRiskDescription("Method defined in more than one class (override).");
        i1.addDecision(i1_d1);
        i1.addDecision(i1_d2);
        Instruction i2 = new Instruction( 2, "Instruction 2: Single declaration method $method");
        Instruction i3 = new Instruction(3,"Instruction 3: Overrides class $class");

        tree.addInstruction(i2);
        tree.addInstruction(i3);

        tree.setFirstInstruction(i1);
        return tree;
    }
}
