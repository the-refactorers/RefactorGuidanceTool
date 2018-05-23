package ait;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import java.util.*;

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
    public void GivenInstructionAndParameterMapFillsParameterValues()
    {
        AdaptiveInstructionTree tree = new AIT_TestGenerator().getAdaptiveInstructionTree();
        InstructionGenerator generator = new InstructionGenerator(tree);

        // Instruction in template Parameter fill test: Dummy method $method is located in dummy $class
        Map<String, List<String>> parameterMap = new HashMap<>();
        parameterMap.put("#method", Arrays.asList("printHelloWorld"));
        parameterMap.put("#class", Arrays.asList("Hello"));

        EnumSet<CodeContext.CodeContextEnum> codeContext = EnumSet.of(CodeContext.CodeContextEnum.MethodOverride);

        generator.setParameterMap(parameterMap);
        generator.setContext(codeContext);

        List<String> instructionSteps = generator.generateInstruction();
        Assert.assertEquals("Parameter fill test: Dummy method printHelloWorld is located in dummy Hello ", instructionSteps.get(0));
        Assert.assertEquals("Instruction 3: Overrides class Hello ", instructionSteps.get(1));
    }

    @Test
    public void defaultNoRiskContextDescribed()
    {
        AdaptiveInstructionTree tree = new AIT_TestGenerator().getAdaptiveInstructionTree();
        Assert.assertEquals(0, tree.getSetOfRiskContext().size());
        Assert.assertEquals("", tree.allInstructions.get(0).decisions.get(0).getRiskDescription());
    }

    @Test
    public void treeWithRiskDescriptions()
    {
        AdaptiveInstructionTree tree = new AIT_TestGenerator().getAdaptiveInstructionTreeWithRiskDescription();
        Assert.assertEquals(1, tree.getSetOfRiskContext().size());
        Assert.assertFalse(tree.allInstructions.get(0).decisions.get(1).getRiskDescription().isEmpty());
        Assert.assertTrue(tree.allInstructions.get(0).decisions.get(0).getRiskDescription().isEmpty());
    }
}
