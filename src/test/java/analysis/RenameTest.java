package analysis;

import aig.*;
import analysis.MethodAnalyzer.ClassMethodFinder;
import analysis.MethodAnalyzer.MethodDescriber;
import analysis.context.ContextAnalyzer;
import analysis.context.ContextConfiguration;
import analysis.context.ContextDetectorSetBuilder;
import com.github.javaparser.ast.CompilationUnit;
import org.junit.Before;
import org.junit.Test;

import java.util.*;

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
        parameterMap.put("#method", Arrays.asList(method.fullTypeSignature()));
        parameterMap.put("#class", Arrays.asList(className));

        AdaptiveInstructionGraph graph = new AIG_RenameGeneration().getAdaptiveInstructionGraph();
        InstructionGenerator generator = new InstructionGenerator(graph);

        //generator.contextSet = codeContext;
        generator.setParameterMap(parameterMap);

        // Analyze context and set-up code context of generator
        generator.setContext(analyse(method, className, graph));

        List<String> instructionSteps = generator.generateInstruction();

        for(String i : instructionSteps)
            System.out.println(i + "\n");
    }

    public EnumSet<CodeContext.CodeContextEnum> analyse(MethodDescriber selectedMethod, String className, AdaptiveInstructionGraph graph)
    {
        ContextConfiguration cac = new ContextConfiguration();

        // SPECIFY necessary refactoring properties
        cac.setMethodDescriber(selectedMethod);
        cac.setCompilationUnit(_cu);
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

        return ca.getDetectedContextSet();
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
        parameterMap.put("#method", Arrays.asList(method.fullTypeSignature()));
        parameterMap.put("#class", Arrays.asList(className));

        AdaptiveInstructionGraph graph = new AIG_RenameGeneration().getAdaptiveInstructionGraph();
        InstructionGenerator generator = new InstructionGenerator(graph);

        //generator.contextSet = codeContext;
        generator.setParameterMap(parameterMap);

        // Analyze context and set-up code context of generator
        generator.setContext(analyse(method, className, graph));

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

