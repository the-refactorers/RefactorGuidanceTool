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

import ait.AIT_RenameGeneration;
import ait.AdaptiveInstructionTree;
import ait.CodeContext;
import ait.InstructionGenerator;
import analysis.MethodAnalyzer.ClassMethodFinder;
import analysis.context.ContextAnalyzer;
import analysis.context.ContextDetectorSetBuilder;
import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;

import java.io.InputStream;
import java.util.*;

public class RenameMethodAnalyzer {

    public EnumSet<CodeContext.CodeContextEnum> AnalyzeContext(ClassMethodFinder cmf, String methodName)
    {
        EnumSet<CodeContext.CodeContextEnum> codeContext = EnumSet.noneOf(CodeContext.CodeContextEnum.class);

        if (!cmf.contextMultipleDeclarations(methodName)) {
            codeContext.add(CodeContext.CodeContextEnum.MethodSingleDeclaration);
        }
        else
        {
            codeContext.add(CodeContext.CodeContextEnum.MethodMultipleDeclarations);
        }

        if (cmf.contextDeclaredInInterface(methodName))
        {
            codeContext.add(CodeContext.CodeContextEnum.MethodInterfaceDeclaration);
        }
        if (cmf.contextDeclaredInSuperClass(methodName))
        {
            codeContext.add(CodeContext.CodeContextEnum.MethodOverride);
        }

        codeContext.add(CodeContext.CodeContextEnum.always_true);

        return codeContext;
    }


    public List<String> generateInstructions(String testResource, int lineNumber) {
        InputStream parseStream = this.getClass().getClassLoader().getResourceAsStream(testResource);

        if (parseStream == null) {
            throw new RuntimeException("Unable to find sample " + testResource);
        }

        CompilationUnit cu = JavaParser.parse(parseStream);

        String className = "MyMethod";
        ClassMethodFinder cmf = new ClassMethodFinder();
        cmf.initialize(cu, className);

        // Determine name based on location
        String methodName = cmf.getMethodNameForLocation(lineNumber);

        List<String> instructionSteps = new ArrayList<>();

        if (!methodName.isEmpty()) {
            // Instruction in template Parameter fill test: Dummy method $method is located in dummy $class
            Map<String, String> parameterMap = new HashMap<>();
            parameterMap.put("$method", methodName);
            parameterMap.put("$class", className);

            if (cmf.contextDeclaredInInterface(methodName)) {
                parameterMap.put("$interface", cmf.methodDefinedInInterface());
            }

            AdaptiveInstructionTree tree = new AIT_RenameGeneration().getAdaptiveInstructionTree();
            InstructionGenerator generator = new InstructionGenerator(tree);

            generator.setParameterMap(parameterMap);

            // Analyze context and set-up code context of generator
            ContextDetectorSetBuilder cb = new ContextDetectorSetBuilder();
            cb.setAIT(tree);

            ContextAnalyzer ca = new ContextAnalyzer();
            ca.setContextDetectors(cb.getContextDetectors());
            ca.run();


            generator.setContext(AnalyzeContext(cmf, methodName));
            //generator.setContext(ca.getDetectedContextSet());

            instructionSteps = generator.generateInstruction();
        }
        else
        {
            instructionSteps.add("There is not a valid scope of method (method not found) on line "+ lineNumber + ". \nI cannot give any suggestions for renaming activity");
        }

        return instructionSteps;
    }
}
