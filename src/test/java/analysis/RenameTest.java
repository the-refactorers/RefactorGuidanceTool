package analysis;

import ait.*;
import analysis.ClassMethodFinder;
import com.github.javaparser.ast.CompilationUnit;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RenameTest {

    private ResourceExampleClassParser _loader;
    CompilationUnit _cu;

    @Before
    public void Setup()
    {
        _loader = new ResourceExampleClassParser();
    }

    private void CreateCompilationUnitFromTestClass(String classTemplate)
    {
        _cu = _loader.Parse(classTemplate);
    }

    private EnumSet<CodeContext.CodeContextEnum> AnalyzeContextForRenaming(ClassMethodFinder cmf, String methodName)
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

        return codeContext;
    }

    @Test
    public void MethodOnlyDeclaredLocal()
    {
        CreateCompilationUnitFromTestClass("RenameMethod.java.txt");

        String className = "MyMethod";
        ClassMethodFinder cmf = new ClassMethodFinder(_cu, className);

        // Determine name based on location context
        String methodName = cmf.getMethodNameForLocation(15);

        // Instruction in template Parameter fill test: Dummy method $method is located in dummy $class
        Map<String, String> parameterMap = new HashMap<>();
        parameterMap.put("$method", methodName);
        parameterMap.put("$class", className);

        AdaptiveInstructionTree tree = new AIT_RenameGeneration().getAdaptiveInstructionTree();
        InstructionGenerator generator = new InstructionGenerator(tree);

        //generator.contextSet = codeContext;
        generator.setParameterMap(parameterMap);

        // Analyze context and set-up code context of generator
        generator.setContext(AnalyzeContextForRenaming(cmf, methodName));

        List<String> instructionSteps = generator.generateInstruction();

        for(String i : instructionSteps)
            System.out.println(i + "\n");
    }

    @Test
    public void MethodIsDeclaredInInterface()
    {
        CreateCompilationUnitFromTestClass("RenameMethod.java.txt");

        String className = "MyMethod";
        ClassMethodFinder cmf = new ClassMethodFinder(_cu, className);

        // Determine name based on location context
        String methodName = cmf.getMethodNameForLocation(22);

        // Instruction in template Parameter fill test: Dummy method $method is located in dummy $class
        Map<String, String> parameterMap = new HashMap<>();
        parameterMap.put("$method", methodName);
        parameterMap.put("$class", className);

        AdaptiveInstructionTree tree = new AIT_RenameGeneration().getAdaptiveInstructionTree();
        InstructionGenerator generator = new InstructionGenerator(tree);

        //generator.contextSet = codeContext;
        generator.setParameterMap(parameterMap);

        // Analyze context and set-up code context of generator
        generator.setContext(AnalyzeContextForRenaming(cmf, methodName));

        if (cmf.contextDeclaredInInterface(methodName))
        {
            parameterMap.put("$interface", cmf.methodDefinedInInterface());
        }

        List<String> instructionSteps = generator.generateInstruction();

        for(String i : instructionSteps)
            System.out.println(i + "\n");
    }
}

