package ait.aitExamples;

import ait.*;
import static ait.CodeContext.CodeContextEnum.*;

public class AIT_Rename {

    AdaptiveInstructionTree tree;

    public AIT_Rename() {
        tree.setDescription("... Remember your code is for a human first and a computer second. Humans need good names....");
        tree.setRefactorMechanic("Rename Method");

        Instruction i1 = new Instruction(1, "De naam van methode $method in class $class willen we renamen");
        ContextDecision d1 = new ContextDecision(method_single_declaration, 2);

        Instruction i2 = new Instruction(2, "Maak een nieuwe methode aan met een verbeterde naam in class $class");
        d1.setNextInstructionID(2);
        i1.addDecision(d1);

        tree.setFirstInstruction(i1);
        tree.addInstruction(i2);
    }

    public AdaptiveInstructionTree getAdaptiveInstructionTree()
    {
        return tree;
    }
}
