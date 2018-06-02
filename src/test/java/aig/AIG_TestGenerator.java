package aig;

public class AIG_TestGenerator {

    AdaptiveInstructionGraph _graph = new AdaptiveInstructionGraph();

    public AIG_TestGenerator() {

    }

    public AdaptiveInstructionGraph getAdaptiveInstructionGraph()
    {
        _graph.setDescription("An adaptive instruction graph template just for unit test purposes");
        _graph.setRefactorMechanic("Unit Testing Template");

        Instruction i1 = new Instruction(1, "Parameter fill test: Dummy method #method is located in dummy #class ");
        ContextDecision i1_d1 = new ContextDecision(CodeContext.CodeContextEnum.MethodSingleDeclaration, 2);
        ContextDecision i1_d2 = new ContextDecision(CodeContext.CodeContextEnum.MethodOverride, 3);
        i1.addDecision(i1_d1);
        i1.addDecision(i1_d2);

        Instruction i2 = new Instruction( 2, "Instruction 2: Single declaration method #method ");
        Instruction i3 = new Instruction(3,"Instruction 3: Overrides class #class ");

        _graph.addInstruction(i2);
        _graph.addInstruction(i3);

        _graph.setFirstInstruction(i1);

        return _graph;
    }

    public AdaptiveInstructionGraph getAdaptiveInstructionGraphWithRiskDescription()
    {
        _graph.setDescription("An adaptive instruction graph template just for unit test purposes");
        _graph.setRefactorMechanic("Unit Testing Template");

        Instruction i1 = new Instruction(1, "Parameter fill test: Dummy method $method is located in dummy $class");
        ContextDecision i1_d1 = new ContextDecision(CodeContext.CodeContextEnum.MethodSingleDeclaration, 2);
        ContextDecision i1_d2 = new ContextDecision(CodeContext.CodeContextEnum.MethodOverride, 3);
        i1_d2.setRiskDescription("Method defined in more than one class (override).");
        i1.addDecision(i1_d1);
        i1.addDecision(i1_d2);
        Instruction i2 = new Instruction( 2, "Instruction 2: Single declaration method $method");
        Instruction i3 = new Instruction(3,"Instruction 3: Overrides class $class");

        _graph.addInstruction(i2);
        _graph.addInstruction(i3);

        _graph.setFirstInstruction(i1);
        return _graph;
    }
}
