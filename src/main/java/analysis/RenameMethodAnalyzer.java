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

package analysis;

import aig.*;
import analysis.MethodAnalyzer.ClassMethodFinder;
import analysis.MethodAnalyzer.MethodDescriber;
import analysis.context.CodeSection;
import analysis.context.ContextAnalyzer;
import analysis.context.ContextConfiguration;
import analysis.context.ContextDetectorSetBuilder;
import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;

import java.io.InputStream;
import java.util.*;

public class RenameMethodAnalyzer {

    public List<String> generateInstructions(String refactorAction,
                                             String testResource,
                                             String className,
                                             String newMethodName,
                                             int lineNumberStart,
                                             int lineNumberEnd)
    {
        return generateInstructions(refactorAction, testResource, className, newMethodName, lineNumberStart, lineNumberEnd, true);
    }

    // @todo: rename code section to specify which part of code to be extracted
    public List<String> generateInstructions(String refactorAction,
                                             String testResource,
                                             String className,
                                             String newMethodName,
                                             int lineNumberStart,
                                             int lineNumberEnd,
                                             boolean fromResource) {

        CompilationUnit cu = null;

       if(fromResource) {
           // load java class from the resource set
           InputStream parseStream = this.getClass().getClassLoader().getResourceAsStream(testResource);

           if (parseStream == null) {
               throw new RuntimeException("Unable to find sample " + testResource);
           }

           cu = JavaParser.parse(parseStream);
       }

       // CompilationUnit cu = JavaParser.parse("src/main/java/");

        // set up analyzer to make it possible to retrieve method-number based on line number
        // @todo: This should be taken out of analyzer class
        ClassMethodFinder cmf = new ClassMethodFinder();
        cmf.initialize(cu, className);

        // Determine name based on location
        MethodDescriber selectedMethod = cmf.getMethodDescriberForLocation(lineNumberStart);

        List<String> instructionSteps = new ArrayList<>();

        // When we have a method name, start generating instructions for renaming this method
        if (!selectedMethod.getName().isEmpty()) {

            // Analyze context and set-up code context of generator
            ContextConfiguration cac = new ContextConfiguration();

            // SELECT refactoring
            AdaptiveInstructionGraph graph = null;
            if(refactorAction.contentEquals("Rename"))
                graph = new AIG_RenameGeneration().getAdaptiveInstructionGraph();
            else if(refactorAction.contentEquals("ExtractMethod")) {
                graph = new AIG_ExtractMethodGeneration().getAdaptiveInstructionGraph();
                cac.setCodeSection(new CodeSection(lineNumberStart, lineNumberEnd));
            }

            // SPECIFY necessary refactoring properties
            cac.setMethodDescriber(selectedMethod);
            cac.setCompilationUnit(cu);
            cac.setClassName(className);

            ContextDetectorSetBuilder cb = new ContextDetectorSetBuilder();
            cb.setConfiguration(cac);
            cb.setAIT(graph);

            ContextAnalyzer ca = new ContextAnalyzer();

            try {
                // CONFIGURE context detectors
                // Build up specific set of context detectors belonging to the graph provided
                ca.setContextDetectors(cb.getContextDetectors());

                // ANALYZE code
                // Evaluate all detectors -> Results in set of context detected
                ca.run();
            }
            catch(Exception e)
            {
                System.out.println(e.getMessage());
            }

            // Instantiate instruction generator
            InstructionGenerator generator = new InstructionGenerator(graph);

            // Provide concrete parameter values and detected context set
            generator.setParameterMap(ca.getParameterMap());
            generator.setContext(ca.getDetectedContextSet());

            // GENERATE (=Filter nodes from graph + parsing of parametrized values in resulting nodes)
            instructionSteps = generator.generateInstruction(true);
        }
        else
        {
            instructionSteps.add("There is not a valid scope of method (method not found) on line "+ lineNumberStart + ". \nI cannot give any suggestions for renaming activity");
        }

        return instructionSteps;
    }
}
