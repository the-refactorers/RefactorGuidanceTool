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
