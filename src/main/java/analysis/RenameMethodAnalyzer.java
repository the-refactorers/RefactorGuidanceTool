package analysis;

import ait.AIT_RenameGeneration;
import ait.AdaptiveInstructionTree;
import ait.CodeContext;
import ait.InstructionGenerator;
import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;

import java.io.InputStream;
import java.util.*;

public class RenameMethodAnalyzer {

    public EnumSet<CodeContext.CodeContextEnum> AnalyzeContext(ClassMethodFinder cmf, String methodName)
    {
        EnumSet<CodeContext.CodeContextEnum> codeContext = EnumSet.noneOf(CodeContext.CodeContextEnum.class);

        if (!cmf.contextMultipleDeclarations(methodName)) {
            codeContext.add(CodeContext.CodeContextEnum.method_single_declaration);
        }
        else
        {
            codeContext.add(CodeContext.CodeContextEnum.method_multiple_declares);
        }

        if (cmf.contextDeclaredInInterface(methodName))
        {
            codeContext.add(CodeContext.CodeContextEnum.method_defined_in_interface);
        }
        if (cmf.contextDeclaredInSuperClass(methodName))
        {
            codeContext.add(CodeContext.CodeContextEnum.method_override);
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
        ClassMethodFinder cmf = new ClassMethodFinder(cu, className);

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

            //generator.contextSet = codeContext;
            generator.setParameterMap(parameterMap);

            // Analyze context and set-up code context of generator
            generator.setContext(AnalyzeContext(cmf, methodName));

            instructionSteps = generator.generateInstruction();
        }
        else
        {
            instructionSteps.add("There is not a valid scope of method (method not found) on line "+ lineNumber + ". \nI cannot give any suggestions for renaming activity");
        }

        return instructionSteps;
    }
}
