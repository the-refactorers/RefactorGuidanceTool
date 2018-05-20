package ait;

public class AIT_ExtractMethodGeneration implements I_AIT {

    AdaptiveInstructionTree tree = new AdaptiveInstructionTree();

    public AIT_ExtractMethodGeneration()
    {
        tree.setDescription("... Remember, keep you methods understandable, small and simple ...");
        tree.setRefactorMechanic("Extract Method");
    }

    public AdaptiveInstructionTree getAdaptiveInstructionTree()
    {
        return tree;
    }
}
