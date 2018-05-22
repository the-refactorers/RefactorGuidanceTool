/**
 Copyright (C) 2018, Patrick de Beer

 This program is free software: you can redistribute it and/or modify
 it under the terms of the GNU General Public License as published by
 the Free Software Foundation, either version 3 of the License, or
 (at your option) any later version.

 This program is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 GNU General Public License for more details.

 You should have received a copy of the GNU General Public License
 along with this program.  If not, see <http://www.gnu.org/licenses/>.
 **/

package analysis;

import ait.*;
import analysis.MethodAnalyzer.ClassMethodFinder;
import analysis.MethodAnalyzer.MethodDescriber;
import analysis.context.ContextAnalyzer;
import analysis.context.ContextConfiguration;
import analysis.context.ContextDetectorSetBuilder;
import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;

import java.io.InputStream;
import java.util.*;

public class RenameMethodAnalyzer {

    @Deprecated
    public EnumSet<CodeContext.CodeContextEnum> AnalyzeContext(ClassMethodFinder cmf, MethodDescriber method)
    {
        EnumSet<CodeContext.CodeContextEnum> codeContext = EnumSet.noneOf(CodeContext.CodeContextEnum.class);

        try {
            if (!cmf.contextMultipleDeclarations(method)) {
                codeContext.add(CodeContext.CodeContextEnum.MethodSingleDeclaration);
            } else {
                codeContext.add(CodeContext.CodeContextEnum.MethodMultipleDeclarations);
            }

            if (cmf.contextDeclaredInInterface(method)) {
                codeContext.add(CodeContext.CodeContextEnum.MethodInterfaceDeclaration);
            }
            if (cmf.contextDeclaredInSuperClass(method)) {
                codeContext.add(CodeContext.CodeContextEnum.MethodOverride);
            }

            codeContext.add(CodeContext.CodeContextEnum.always_true);
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
        }

        return codeContext;
    }


    public List<String> generateInstructions(String refactorAction, String testResource, String className, String newMethodName, int lineNumber) {

        // load java class from the resource set
        InputStream parseStream = this.getClass().getClassLoader().getResourceAsStream(testResource);

        if (parseStream == null) {
            throw new RuntimeException("Unable to find sample " + testResource);
        }

        // Initialize compilation unit
        CompilationUnit cu = JavaParser.parse(parseStream);

        // set up analyzer to make it possible to retrieve method-number based on line number
        // @todo: This should be taken out of analyzer class
        ClassMethodFinder cmf = new ClassMethodFinder();
        cmf.initialize(cu, className);

        // Determine name based on location
        MethodDescriber selectedMethod = cmf.getMethodDescriberForLocation(lineNumber);

        List<String> instructionSteps = new ArrayList<>();

        // When we have a method name, start generating instructions for renaming this method
        if (!selectedMethod.getName().isEmpty()) {
            // SELECT refactoring
            AdaptiveInstructionTree tree = null;
            if(refactorAction.contentEquals("Rename"))
                 tree = new AIT_RenameGeneration().getAdaptiveInstructionTree();
            else if(refactorAction.contentEquals("ExtractMethod"))
                tree = new AIT_ExtractMethodGeneration().getAdaptiveInstructionTree();

            // Analyze context and set-up code context of generator
            ContextConfiguration cac = new ContextConfiguration();

            // SPECIFY necessary refactoring properties
            cac.setMethodDescriber(selectedMethod);
            cac.setCompilationUnit(cu);
            cac.setClassName(className);

            ContextDetectorSetBuilder cb = new ContextDetectorSetBuilder();
            cb.setConfiguration(cac);
            cb.setAIT(tree);

            ContextAnalyzer ca = new ContextAnalyzer();

            try {
                // CONFIGURE context detectors
                // Build up specific set of context detectors belonging to the tree provided
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
            InstructionGenerator generator = new InstructionGenerator(tree);

            // Provide concrete parameter values and detected context set
            generator.setParameterMap(ca.getParameterMap());
            generator.setContext(ca.getDetectedContextSet());

            // GENERATE (=Filter nodes from tree + parsing of parametrized values in resulting nodes)
            instructionSteps = generator.generateInstruction(true);
        }
        else
        {
            instructionSteps.add("There is not a valid scope of method (method not found) on line "+ lineNumber + ". \nI cannot give any suggestions for renaming activity");
        }

        return instructionSteps;
    }
}
