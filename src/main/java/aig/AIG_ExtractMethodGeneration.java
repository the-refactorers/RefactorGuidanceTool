package aig;

public class AIG_ExtractMethodGeneration implements I_AIG {

    AdaptiveInstructionGraph _graph = new AdaptiveInstructionGraph();

    public AIG_ExtractMethodGeneration()
    {
        _graph.setDescription("... Remember, keep you methods understandable, small and simple ...");
        _graph.setRefactorMechanic("Extract Method");

        // All possible textual instructions in the graph
        // parameterized values by '$<param>'
        Instruction i1 = new Instruction(1, "Create the new method #new_method. Choose a proper meaningful name.");
        Instruction i2 = new Instruction(2, "Copy code to extract from #method to #new_mehod.");
        Instruction i3 = new Instruction(3, "Check your local declared methods. Do they have meaningful names. If not rename the local vars.");
        Instruction i4 = new Instruction(4, "Compile your code");
        Instruction i5 = new Instruction(5, "When no compilation errors. You can now safely replace the extracted code in #method with a call to #new_method");
        Instruction i6 = new Instruction(6, "Parameters need to be passed on");

        // Define to what node to jump, when a specific code context is valid for a specific instruction
        ContextDecision i1_d1 = new ContextDecision(CodeContext.CodeContextEnum.MethodExtractNoneLocalDependencies, 2);
        // For risk based contexts, give a description of the risk that is relevant to this context
        i1_d1.setRiskDescription("");
        ContextDecision i1_d2 = new ContextDecision(CodeContext.CodeContextEnum.MethodExtractSingleArgument, 6);
        i1_d2.setRiskDescription("");

        // Couple decision points based on code context to relevant instructions
        i1.addDecision(i1_d1);
        i1.addDecision(i1_d2);

        // Set entry point of graph
        _graph.setFirstInstruction(i1);

        _graph.addInstruction(i2);
        _graph.addInstruction(i3);
        _graph.addInstruction(i4);
        _graph.addInstruction(i5);
        _graph.addInstruction(i6);
    }

    @Override
    public AdaptiveInstructionGraph getAdaptiveInstructionGraph()
    {
        return _graph;
    }
}
