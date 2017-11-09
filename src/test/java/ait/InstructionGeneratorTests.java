package ait;

import org.junit.Assert;
import org.junit.Test;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InstructionGeneratorTests {

    @Test
    public void GivenNoContextResultsInEmptyInstructions()
    {
        AdaptiveInstructionTree tree = new AIT_TestGenerator().getAdaptiveInstructionTree();
        InstructionGenerator generator = new InstructionGenerator(tree);

        Assert.assertTrue(generator.generateInstruction().isEmpty());
    }

    @Test
    public void GivenTreeWithThreeInstructionsAndDecisionResultsCorrectInstruction()
    {

    }

    @Test
    public void GivenInstructionAndParameterMapFillsParameterValues()
    {
        AdaptiveInstructionTree tree = new AIT_TestGenerator().getAdaptiveInstructionTree();
        InstructionGenerator generator = new InstructionGenerator(tree);

        // Instruction in template Parameter fill test: Dummy method $method is located in dummy $class
        Map<String, String> parameterMap = new HashMap<>();
        parameterMap.put("$method", "printHelloWorld");
        parameterMap.put("$class", "Hello");

        EnumSet<CodeContext.CodeContextEnum> codeContext = EnumSet.of(CodeContext.CodeContextEnum.method_override);

        //generator.contextSet = codeContext;
        generator.setParameterMap(parameterMap);
        List<String> instructionSteps = generator.generateInstruction();
        Assert.assertEquals("Parameter fill test: Dummy method printHelloWorld is located in dummy Hello", instructionSteps.get(0));
        Assert.assertEquals("Instruction 3: Overrides class Hello", instructionSteps.get(1));
    }
}
