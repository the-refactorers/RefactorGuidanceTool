package ait;

public class AIT_ExtractMethodGeneration implements I_AIT {

    AdaptiveInstructionTree tree = new AdaptiveInstructionTree();

    public AIT_ExtractMethodGeneration()
    {
        tree.setDescription("... Remember, keep you methods understandable, small and simple ...");
        tree.setRefactorMechanic("Extract Method");

        Instruction i1 = new Instruction(1, "Create the new method $new_method. Choose a proper meaningful name.");
        Instruction i2 = new Instruction(2, "Copy code to extract from $method to $new_mehod.");
        Instruction i3 = new Instruction(3, "Check your local declared methods. Do they have meaningful names. If not rename the local vars.");
        Instruction i4 = new Instruction(4, "Compile your code");
        Instruction i5 = new Instruction(5, "When no compilation errors. You can now safely replace the extracted code in $method with a call to $new_method");
    }

    public AdaptiveInstructionTree getAdaptiveInstructionTree()
    {
        return tree;
    }
}
