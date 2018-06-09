package aig;

public class AIG_RenameGeneration implements I_AIG {

    private ContextDescriber cdMethodInterfaceDeclaration = new ContextDescriber(
            CodeContext.CodeContextEnum.MethodInterfaceDeclaration,
            "[Method Interface Declaration]. Dependencies with external packages might be broken."
    );

    private ContextDescriber cdMethodOverride = new ContextDescriber(
            CodeContext.CodeContextEnum.MethodOverride,
            "[Method override]. Single method rename might change behavior due to polymorphism."
    );

    private ContextDescriber cdMethodOverload = new ContextDescriber(
            CodeContext.CodeContextEnum.MethodOverload,
            "[Method overload]. For readibility it should be considered to change all method which share a common name."
    );

    AdaptiveInstructionGraph _graph = new AdaptiveInstructionGraph();

    public AIG_RenameGeneration() {
        _graph.setDescription("... Remember, code is written for humans ...");
        _graph.setRefactorMechanic("Rename Method");

        Instruction i1 = new Instruction(1, "\nBelow are INSTRUCTIONS for renaming #method in class #class of your project, assuming all risks are taken into account.\n" +
                "Feel free to skip specific steps for risks which you think are not relevant. Steps are marked for specific risks. \n");
        Instruction i2 = new Instruction(2, "In the current context there is no risk in renaming method #method directly\n");
        Instruction i3 = new Instruction(3, "Rename #method in class #class to your new name");
        Instruction i4 = new Instruction(4, "Build project. Resolve any unresolved references to #method indicated by compiler.");
        Instruction i5 = new Instruction(5, "Method #method is not declared for the first time in class #class .");
        Instruction i6 = new Instruction(6, "[Interface Declaration] A declaration exists in (public) interface #interface.\nIt is a good practice to \n\t1. Mark public #method deprecated in #interface with @deprecated \n\t2. Declare new method in #interface \n\t 3. Create your new method in #class and call this method from within #method");
        Instruction i7 = new Instruction(7, "[Method Override] Method #method has been defined in the following superclasses: #class-list\nCheck if you want to rename them also to preserve application behavior.\n When no risk. proceed...");
        Instruction i8 = new Instruction( 8, "To eliminate any side-effect risks, I suggest to rename #method also in #class-list\n");
        Instruction i9 = new Instruction( 9, "[Method Overload] There are methods present in your class hierarchy with the same name (method override), but different number of parameters.\nIt is a good practice to rename also these methods to the new name you have choosen ");

        ContextDecision i1_d1 = new ContextDecision(CodeContext.CodeContextEnum.MethodSingleDeclaration, 2);
        i1_d1.setRiskDescription("");
        ContextDecision i1_d2 = new ContextDecision(CodeContext.CodeContextEnum.MethodMultipleDeclarations, 5);
        ContextDecision i2_d1 = new ContextDecision(CodeContext.CodeContextEnum.always_true, 3);
        ContextDecision i3_d1 = new ContextDecision(CodeContext.CodeContextEnum.always_true, 4);

        ContextDecision i5_d1 = new ContextDecision(cdMethodInterfaceDeclaration, 6);

        ContextDecision i5_d2 = new ContextDecision(cdMethodOverride, 7);

        ContextDecision i6_d1 = new ContextDecision(cdMethodOverride, 7);

        ContextDecision i6_d2 = new ContextDecision(CodeContext.CodeContextEnum.always_true, 3);
        ContextDecision i7_d1 = new ContextDecision(CodeContext.CodeContextEnum.always_true, 8);
        ContextDecision i8_d1 = new ContextDecision(CodeContext.CodeContextEnum.always_true, 3);
        ContextDecision i9_d1 = new ContextDecision(CodeContext.CodeContextEnum.always_true);
        ContextDecision i4_d1 = new ContextDecision(cdMethodOverload, 9);

        ContextDecision i4_d2 = new ContextDecision(CodeContext.CodeContextEnum.always_true);

        i1.addDecision(i1_d1);
        i1.addDecision(i1_d2);
        i2.addDecision(i2_d1);
        i3.addDecision(i3_d1);
        i4.addDecision(i4_d1);
        i4.addDecision(i4_d2);
        i5.addDecision(i5_d1);
        i5.addDecision(i5_d2);
        i6.addDecision(i6_d1);
        i6.addDecision(i6_d2);
        i7.addDecision(i7_d1);
        i8.addDecision(i8_d1);
        i9.addDecision(i9_d1);

        _graph.setFirstInstruction(i1);

        _graph.addInstruction(i2);
        _graph.addInstruction(i3);
        _graph.addInstruction(i4);
        _graph.addInstruction(i5);
        _graph.addInstruction(i6);
        _graph.addInstruction(i7);
        _graph.addInstruction(i8);
        _graph.addInstruction(i9);
    }

    @Override
    public AdaptiveInstructionGraph getAdaptiveInstructionGraph() {
        return _graph;
    }
}
