/**
 *  This file is part of RefactorGuidance project. Which explores possibilities to generate context based
 *  instructions on how to refactor a piece of Java code. This applied in an education setting (bachelor SE students)
 *
 *      Copyright (C) 2018, Patrick de Beer, p.debeer@fontys.nl
 *
 *          This program is free software: you can redistribute it and/or modify
 *          it under the terms of the GNU General Public License as published by
 *          the Free Software Foundation, either version 3 of the License, or
 *          (at your option) any later version.
 *
 *          This program is distributed in the hope that it will be useful,
 *          but WITHOUT ANY WARRANTY; without even the implied warranty of
 *          MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *          GNU General Public License for more details.
 *
 *          You should have received a copy of the GNU General Public License
 *          along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

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
