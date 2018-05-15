package analysis;

import ait.*;
import analysis.MethodAnalyzer.ClassMethodFinder;
import com.github.javaparser.ast.CompilationUnit;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RenameTest {

    private ResourceExampleClassParser _loader;
    private CompilationUnit _cu;
    private RenameMethodAnalyzer _analyzer = new RenameMethodAnalyzer();

    @Before
    public void Setup()
    {
        _loader = new ResourceExampleClassParser();
    }

    private void CreateCompilationUnitFromTestClass(String classTemplate)
    {
        _cu = _loader.Parse(classTemplate);
    }

    @Test
    public void MethodOnlyDeclaredLocal()
    {
        CreateCompilationUnitFromTestClass("RenameMethod.java.txt");

        String className = "MyMethod";
        ClassMethodFinder cmf = new ClassMethodFinder();
        cmf.initialize(_cu, className);

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
        generator.setContext(_analyzer.AnalyzeContext(cmf, methodName));

        List<String> instructionSteps = generator.generateInstruction();

        for(String i : instructionSteps)
            System.out.println(i + "\n");
    }

    @Test
    public void MethodIsDeclaredInInterface()
    {
        CreateCompilationUnitFromTestClass("RenameMethod.java.txt");

        String className = "MyMethod";
        ClassMethodFinder cmf = new ClassMethodFinder();
        cmf.initialize(_cu, className);

        // Determine name based on location context
        String methodName = cmf.getMethodNameForLocation(28);

        // Instruction in template Parameter fill test: Dummy method $method is located in dummy $class
        Map<String, String> parameterMap = new HashMap<>();
        parameterMap.put("$method", methodName);
        parameterMap.put("$class", className);

        AdaptiveInstructionTree tree = new AIT_RenameGeneration().getAdaptiveInstructionTree();
        InstructionGenerator generator = new InstructionGenerator(tree);

        //generator.contextSet = codeContext;
        generator.setParameterMap(parameterMap);

        // Analyze context and set-up code context of generator
        generator.setContext(_analyzer.AnalyzeContext(cmf, methodName));

        if (cmf.contextDeclaredInInterface(methodName))
        {
            parameterMap.put("$interface", cmf.methodDefinedInInterface());
        }

        List<String> instructionSteps = generator.generateInstruction();

        for(String i : instructionSteps)
            System.out.println(i + "\n");
    }
}

