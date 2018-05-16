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

        Assert.assertEquals(generator.generateInstruction().get(0), "ERROR: Context-set is empty or null");
    }

    @Test
    public void GivenTreeNullGivesErrorInstruction()
    {
        InstructionGenerator generator = new InstructionGenerator(null);
        Assert.assertEquals(generator.generateInstruction().get(0), "ERROR: AIT is null");
    }

    @Test
    public void GivenEmptyContextSetShouldResultInErrorInstruction()
    {
        AdaptiveInstructionTree tree = new AIT_TestGenerator().getAdaptiveInstructionTree();
        InstructionGenerator generator = new InstructionGenerator(tree);
        EnumSet<CodeContext.CodeContextEnum> codeContext = EnumSet.noneOf(CodeContext.CodeContextEnum.class);
        generator.setContext(codeContext);

        Assert.assertEquals(generator.generateInstruction().get(0), "ERROR: Context-set is empty or null");
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

        EnumSet<CodeContext.CodeContextEnum> codeContext = EnumSet.of(CodeContext.CodeContextEnum.MethodOverride);

        //generator.contextSet = codeContext;
        generator.setParameterMap(parameterMap);
        generator.setContext(codeContext);

        List<String> instructionSteps = generator.generateInstruction();
        Assert.assertEquals("Parameter fill test: Dummy method printHelloWorld is located in dummy Hello", instructionSteps.get(0));
        Assert.assertEquals("Instruction 3: Overrides class Hello", instructionSteps.get(1));
    }
}
