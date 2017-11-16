package ait;

public class AIT_RenameGeneration {

    AdaptiveInstructionTree tree = new AdaptiveInstructionTree();

    public AIT_RenameGeneration() {
        tree.setDescription("... Remember, code is written for humans ...");
        tree.setRefactorMechanic("Rename Method");

        Instruction i1 = new Instruction(1, "When renaming $method in class $class of your project, take steps:");
        Instruction i2 = new Instruction(2, "In the current context there is no risk in renaming method $method directly");
        Instruction i3 = new Instruction(3, "Rename $method in class $class to the new name");
        Instruction i4 = new Instruction(4, "Build your project and resolved unresolved references to $method");
        Instruction i5 = new Instruction(5, "$method is not declared for the first time in $class");
        Instruction i6 = new Instruction(6, "A declaration exists in interface $interface.\n It is a good practice to 1. Mark $method deprecated in $interface 2. Declare new method in $interface");
        Instruction i7 = new Instruction(7, "$method has been defined in the following superclasses: $super-list\nCheck if they need to be renamed also to preserve application behavior.\n When no risk. proceed...");

        ContextDecision i1_d1 = new ContextDecision(CodeContext.CodeContextEnum.method_single_declaration, 2);
        ContextDecision i1_d2 = new ContextDecision(CodeContext.CodeContextEnum.method_multiple_declares, 5);
        ContextDecision i5_d1 = new ContextDecision(CodeContext.CodeContextEnum.method_defined_in_interface, 6);
        ContextDecision i5_d2 = new ContextDecision(CodeContext.CodeContextEnum.method_override, 7);

        i1.addDecision(i1_d1);
        i1.addDecision(i1_d2);
        i5.addDecision(i5_d1);
        i5.addDecision(i5_d2);

        tree.setFirstInstruction(i1);

        tree.addInstruction(i2);
        tree.addInstruction(i3);
        tree.addInstruction(i4);
        tree.addInstruction(i5);
        tree.addInstruction(i6);
        tree.addInstruction(i7);
    }

    public AdaptiveInstructionTree getAdaptiveInstructionTree()
    {
        return tree;
    }
}
