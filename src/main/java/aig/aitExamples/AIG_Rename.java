package aig.aitExamples;

import aig.*;
import static aig.CodeContext.CodeContextEnum.*;

public class AIG_Rename implements I_AIG{

    AdaptiveInstructionGraph _graph;

    public AIG_Rename() {
        _graph.setDescription("... Remember your code is for a human first and a computer second. Humans need good names....");
        _graph.setRefactorMechanic("Rename Method");

        Instruction i1 = new Instruction(1, "De naam van methode $method in class $class willen we renamen");
        ContextDecision d1 = new ContextDecision(MethodSingleDeclaration, 2);

        Instruction i2 = new Instruction(2, "Maak een nieuwe methode aan met een verbeterde naam in class $class");
        d1.setNextInstructionID(2);
        i1.addDecision(d1);

        _graph.setFirstInstruction(i1);
        _graph.addInstruction(i2);
    }

    @Override
    public AdaptiveInstructionGraph getAdaptiveInstructionGraph() {
        return _graph;
    }
}
