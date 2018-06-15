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

public class AIG_ExtractMethodGeneration implements I_AIG {

    private ContextDescriber cdMethodOverrideNoAnnotation = new ContextDescriber(
            CodeContext.CodeContextEnum.MethodExtractNameHiding,
            "[Name Hiding]: When local variables hide class fields with the same name. Moving code from one method to another might result in using the wrong variable."
    );

    private AdaptiveInstructionGraph _graph = new AdaptiveInstructionGraph();

    public AIG_ExtractMethodGeneration()
    {
        _graph.setDescription("... Remember, keep you methods understandable, small and simple ...");
        _graph.setRefactorMechanic("Extract Method");

        Instruction i1 = new Instruction(1, "\n\nBelow are INSTRUCTIONS for extracting your selected code from #method in class #class of your project.\n" +
                "Feel free to skip specific steps for risks which you think are not relevant. Steps are marked with [] for specific risks. \n");
        Instruction i2 = new Instruction(2, "\nCreate a new method in class #class to where the extracted code should go to." +
                "\nChoose a name for the new method that covers what the extracted code actually does." +
                "\nLet the return type of your new method be void and let it have none parameters.\n\tExample: void newName() { <Extracted code> }\n" +
                "\nAdd a call to your new method just before the code that will be extracted from method [#method].\n" +
                "\nDo not change or remove anything else of the code to extract. We will delete it later.\n" +
                "\nCopy the code to extract to the newly created method\n");
        Instruction i3 = new Instruction(3, "[Name Hiding]\n" +
                "\tThe code you want to extract contains variables: #variable-list\n" +
                "\tThese variables are hiding class fields with the same name.\n" +
                "\tStart by renaming the local variables. This will prevent that variables in your extracted code will use accidentally class fields i.s.o. the local variable. " +
                "The compiler will happily compile if you forget them.\n");
        Instruction i4 = new Instruction(4,"");
        Instruction i5 = new Instruction(5, "\nThere are no further dependencies in the extracted code\n");
        Instruction i6 = new Instruction(6, "\nCompile code\n" +
                "\nSolve any unresolved variable names in your newly created method by adding de declarations for these variables locally in the new method.\n" +
                "\nRemove extracted code from method #method\n" +
                "\nRemove variables that are not used in method #method\n" +
                "\nUpdate & Run your automatic tests.\n");
        Instruction i7= new Instruction(7, "");
        Instruction i8 = new Instruction(8, "\nVariable #argument-list is used in your new method and assigned a value in #method." +
                "\nCopy variable declaration in #method for #argument-list as an input parameter into your new method." +
                "\nAdd parameter as an argument to your new added method call in #method\n");
        Instruction i9 = new Instruction(9, "Variables [ #argument-list ] are used in your new method and are assigned a value in #method." +
                "\nCopy variable declarations for variables [ #argument-list ] as new input parameters into your new method." +
                "\nAdd the same variables as argument to your newly added method call in #method" +
                "\nDo these parameters have a close relation with each other?" +
                "\nIf so, consider grouping them in a new object that contains these parameters. This will reduce the number of needed input parameters for your new method.\n");
        Instruction i10 = new Instruction(10,"");
        Instruction i11 = new Instruction(11,"");
        Instruction i12 = new Instruction(12, "\nYour extracted code contains variable [ #result-list ] that is used later in source method #method.\n" +
                "\nAdd return type to your new method that equals the type of variable [ #result-list ].\n" +
                "\nReturn variable #result-list at end of your new method.\n" +
                "\nIn the original method #method assign the result after calling the new method to the local variable.\n" +
                "\tExample: #result-list = newName(arg)\n");
        Instruction i13 = new Instruction(13, "\nreturn in statement\n" +
        "\t ");

        // Define to what node to jump, when a specific code context is valid for a specific instruction
        ContextDecision i1_d1 = new ContextDecision(CodeContext.CodeContextEnum.MethodExtractNoNameHiding, 2);
        ContextDecision i1_d2 = new ContextDecision(cdMethodOverrideNoAnnotation, 3);

        ContextDecision i2_d1 = new ContextDecision(CodeContext.CodeContextEnum.MethodExtractNoneArguments, 7);
        ContextDecision i2_d2 = new ContextDecision(CodeContext.CodeContextEnum.MethodExtractSingleArgument, 8);
        ContextDecision i2_d3 = new ContextDecision(CodeContext.CodeContextEnum.MethodExtractMultiArgument, 9);

        ContextDecision i3_d1 = new ContextDecision(CodeContext.CodeContextEnum.always_true, 2);

        ContextDecision i4_d1 = new ContextDecision(CodeContext.CodeContextEnum.MethodExtractNoControlReturn, 5);
        ContextDecision i4_d2 = new ContextDecision(CodeContext.CodeContextEnum.MethodExtractControlReturn, 13);

        ContextDecision i5_d1 = new ContextDecision(CodeContext.CodeContextEnum.always_true, 6);

        ContextDecision i7_8_9_d1 = new ContextDecision(CodeContext.CodeContextEnum.MethodExtractNoneResults, 4);

        ContextDecision i7_8_9_d2 = new ContextDecision(CodeContext.CodeContextEnum.MethodExtractSingleResult, 10);

        ContextDecision i10_d1 = new ContextDecision(CodeContext.CodeContextEnum.MethodExtractNoControlReturn, 12);
        ContextDecision i10_d2 = new ContextDecision(CodeContext.CodeContextEnum.MethodExtractControlReturn, 13);

        ContextDecision i12_d1 = new ContextDecision(CodeContext.CodeContextEnum.always_true, 6);

        ContextDecision i13_d1 = new ContextDecision(CodeContext.CodeContextEnum.always_true, 6);

        // Couple decision points based on code context to relevant instructions
        i1.addDecision(i1_d1);
        i1.addDecision(i1_d2);

        i2.addDecision(i2_d1);
        i2.addDecision(i2_d2);
        i2.addDecision(i2_d3);

        i3.addDecision(i3_d1);
        i4.addDecision(i4_d1);
        i4.addDecision(i4_d2);
        i5.addDecision(i5_d1);

        i7.addDecision(i7_8_9_d1);
        i7.addDecision(i7_8_9_d2);

        i8.addDecision(i7_8_9_d1);
        i8.addDecision(i7_8_9_d2);

        i9.addDecision(i7_8_9_d1);
        i9.addDecision(i7_8_9_d2);

        i10.addDecision(i10_d1);
        i10.addDecision(i10_d2);

        i12.addDecision(i12_d1);

        i13.addDecision(i13_d1);

        // Set entry point of graph
        _graph.setFirstInstruction(i1);

        _graph.addInstruction(i2);
        _graph.addInstruction(i3);
        _graph.addInstruction(i4);
        _graph.addInstruction(i5);
        _graph.addInstruction(i6);
        _graph.addInstruction(i7);
        _graph.addInstruction(i8);
        _graph.addInstruction(i9);
        _graph.addInstruction(i10);
        _graph.addInstruction(i12);
        _graph.addInstruction(i13);
    }

    @Override
    public AdaptiveInstructionGraph getAdaptiveInstructionGraph()
    {
        return _graph;
    }
}
