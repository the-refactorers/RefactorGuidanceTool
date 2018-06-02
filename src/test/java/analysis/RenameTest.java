package analysis;

import aig.*;
import analysis.MethodAnalyzer.ClassMethodFinder;
import analysis.MethodAnalyzer.MethodDescriber;
import com.github.javaparser.ast.CompilationUnit;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//@todo: adjust renameTest to new generic way of generating
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
        MethodDescriber method = cmf.getMethodDescriberForLocation(22);

        // Instruction in template Parameter fill test: Dummy method $method is located in dummy $class
        Map<String, List<String>> parameterMap = new HashMap<>();
        parameterMap.put("$method", Arrays.asList(method.fullTypeSignature()));
        parameterMap.put("$class", Arrays.asList(className));

        AdaptiveInstructionGraph graph = new AIG_RenameGeneration().getAdaptiveInstructionGraph();
        InstructionGenerator generator = new InstructionGenerator(graph);

        //generator.contextSet = codeContext;
        generator.setParameterMap(parameterMap);

        // Analyze context and set-up code context of generator
        generator.setContext(_analyzer.AnalyzeContext(cmf, method));

        List<String> instructionSteps = generator.generateInstruction();

        for(String i : instructionSteps)
            System.out.println(i + "\n");
    }

    @Test
    @Deprecated
    public void MethodIsDeclaredInInterface()
    {
        CreateCompilationUnitFromTestClass("RenameMethod.java.txt");

        String className = "MyMethod";
        ClassMethodFinder cmf = new ClassMethodFinder();
        cmf.initialize(_cu, className);

        // Determine name based on location context
        MethodDescriber method = cmf.getMethodDescriberForLocation(28);

        // Instruction in template Parameter fill test: Dummy method $method is located in dummy $class
        Map<String, List<String>> parameterMap = new HashMap<>();
        parameterMap.put("$method", Arrays.asList(method.fullTypeSignature()));
        parameterMap.put("$class", Arrays.asList(className));

        AdaptiveInstructionGraph graph = new AIG_RenameGeneration().getAdaptiveInstructionGraph();
        InstructionGenerator generator = new InstructionGenerator(graph);

        //generator.contextSet = codeContext;
        generator.setParameterMap(parameterMap);

        // Analyze context and set-up code context of generator
        generator.setContext(_analyzer.AnalyzeContext(cmf, method));

        try
        {
            if (cmf.contextDeclaredInInterface(method))
            {
                parameterMap.put("$interface", Arrays.asList(cmf.methodDefinedInInterface()));
            }
        }
        catch(Exception e)
        {
            System.out.println(e.getMessage());
        }

        List<String> instructionSteps = generator.generateInstruction();

        for(String i : instructionSteps)
            System.out.println(i + "\n");
    }
}

