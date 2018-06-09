package aig;

import analysis.context.MethodNoneOverride;

public class AIG_RenameGeneration implements I_AIG {

    private ContextDescriber cdMethodInterfaceDeclaration = new ContextDescriber(
            CodeContext.CodeContextEnum.MethodInterfaceDeclaration,
            "[Method Interface Declaration]: Dependencies with external packages might be broken. They no longer can access this method because the name changed."
    );

    private ContextDescriber cdMethodOverride = new ContextDescriber(
            CodeContext.CodeContextEnum.MethodOverride,
            "[Method override]: Not renaming methods that you override might change behavior of your program due to polymorphism."
    );

    private ContextDescriber cdMethodOverload = new ContextDescriber(
            CodeContext.CodeContextEnum.MethodOverload,
            "[Method overload]: To make your code more understandable it should be considered to change names of all methods which share a common name."
    );

    private ContextDescriber cdMethodOverrideNoAnnotation = new ContextDescriber(
            CodeContext.CodeContextEnum.MethodOverrideNoAnnotation,
            "[None @Override]: It is recommended to preceed each method that is overriden with @Override."
    );

    AdaptiveInstructionGraph _graph = new AdaptiveInstructionGraph();

    public AIG_RenameGeneration() {
        _graph.setDescription("... Remember, code is written for humans ...");
        _graph.setRefactorMechanic("Rename Method");

        Instruction i1 = new Instruction(1, "\n\nBelow are INSTRUCTIONS for renaming #method in class #class of your project, assuming all risks are taken into account.\n" +
                "Feel free to skip specific steps for risks which you think are not relevant. Steps are marked for specific risks. \n");
        Instruction i2 = new Instruction(2, "\nIn the current context there is no risk in renaming method #method directly\n");
        Instruction i3 = new Instruction(3, "\nRename #method in class #class to your new name");
        Instruction i4 = new Instruction(4, "\nBuild project.\nResolve unresolved references to #method indicated by compiler by changing the old name to the new name.\nRun your automatic tests and solve issues.\n");
        Instruction i5 = new Instruction(5, "\nMethod #method is not declared for the first time in class #class .");
        Instruction i6 = new Instruction(6, "\n[Method Interface Declaration]\nA declaration exists in (public) interface #interface.\n" +
                "It is a good practice to \n" +
                "\t1. Mark public #method deprecated in #interface with annotation '@Deprecated' \n" +
                "\t2. Add method with the new name to interface #interface\n" +
                "\t3. Rename #method in class #class to the new name\n" +
                "\t3. Create method #method in #class and make a direct call to your method from within #method to new method");
        Instruction i7 = new Instruction(7, "\n[Method Override]\nMethod #method has been defined in the following superclasses:\n#class-list\n" +
                "To eliminate any side-effect risks, I suggest to rename #method also in: \n#class-list\n");
        Instruction i9 = new Instruction( 9, "\n[Method Overload]\nThere are methods present in your class hierarchy with the same name (method override), but different number of parameters.\n" +
                "It is a good practice to also perform refactoring Rename Method also for these methods.");
        Instruction i10 = new Instruction(10, "");
        Instruction i11 = new Instruction(11, "\n[None @Override]\nIn these classes @Override has not been added everywhere." +
                "Before renaming these methods, Add @Override above  methods\n#method-list");

        ContextDecision i1_d1 = new ContextDecision(CodeContext.CodeContextEnum.MethodSingleDeclaration, 2);
        i1_d1.setRiskDescription("");
        ContextDecision i1_d2 = new ContextDecision(CodeContext.CodeContextEnum.MethodMultipleDeclarations, 5);
        ContextDecision i2_d1 = new ContextDecision(CodeContext.CodeContextEnum.always_true, 3);
        ContextDecision i3_d1 = new ContextDecision(CodeContext.CodeContextEnum.always_true, 4);

        ContextDecision i5_d1 = new ContextDecision(cdMethodInterfaceDeclaration, 6);
        ContextDecision i5_d2 = new ContextDecision(CodeContext.CodeContextEnum.MethodNoneInterfaceDeclaration, 10);
        ContextDecision i10_d1 = new ContextDecision(cdMethodOverride, 7);
        ContextDecision i10_d2 = new ContextDecision(CodeContext.CodeContextEnum.MethodNoneOverride, 3);

        ContextDecision i6_d1 = new ContextDecision(cdMethodOverride, 7);
        ContextDecision i6_d2 = new ContextDecision(CodeContext.CodeContextEnum.MethodNoneOverride, 4);

        ContextDecision i7_d1 = new ContextDecision(CodeContext.CodeContextEnum.MethodNoneOverrideNoAnnotation, 3);
        ContextDecision i7_d2 = new ContextDecision(cdMethodOverrideNoAnnotation, 11);

        ContextDecision i9_d1 = new ContextDecision(CodeContext.CodeContextEnum.always_true);

        ContextDecision i4_d1 = new ContextDecision(cdMethodOverload, 9);
        ContextDecision i4_d2 = new ContextDecision(CodeContext.CodeContextEnum.MethodNoneOverload);

        ContextDecision i11_d1 = new ContextDecision(CodeContext.CodeContextEnum.always_true, 3);

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
        i7.addDecision(i7_d2);
        i9.addDecision(i9_d1);
        i10.addDecision(i10_d1);
        i10.addDecision(i10_d2);
        i11.addDecision(i11_d1);

        _graph.setFirstInstruction(i1);

        _graph.addInstruction(i2);
        _graph.addInstruction(i3);
        _graph.addInstruction(i4);
        _graph.addInstruction(i5);
        _graph.addInstruction(i6);
        _graph.addInstruction(i7);
        _graph.addInstruction(i9);
        _graph.addInstruction(i10);
        _graph.addInstruction(i11);
    }

    @Override
    public AdaptiveInstructionGraph getAdaptiveInstructionGraph() {
        return _graph;
    }
}
