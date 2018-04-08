package analysis;

import analysis.dataflow.*;
import com.github.javaparser.ast.body.MethodDeclaration;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

/**
 * Some tests to check if intra procedural dataflow algorithm detects read, write actions to variable
 * correct
 */
public class DataFlowMarkerTests extends JavaParserTestSetup {

    private MethodDeclaration setupTestClass(String className, String methodName) {
        //String className = "ExtractMethodMarkerCases";
        CreateCompilationUnitFromTestClass(className + ".java.txt");
        return findMethodDeclarationInClass(className, methodName);
    }

    // Case: One variable is defined.
    // Expected outcome: 1 varFlowTable which has the same name as the declared variable in the test code
    @Test
    public void variableFlowTableCreation()
    {
        MethodDeclaration md = setupTestClass("ExtractMethodZeroInZeroOut", "MethodOneLocalDeclared");

        MethodDataFlowAnalyzer analyzer = new MethodDataFlowAnalyzer(md);

        VariableFlowSet variableFlowTableSet = analyzer.getVariableFlowSet();

        List<VariableFlowTable> allVarFlowTables = variableFlowTableSet.getListOfVariableFlowTables();

        Assert.assertEquals(1, allVarFlowTables.size());
        Assert.assertEquals(variableFlowTableSet.getVariableFlowTable("a").name,"a");
    }

    // Case: c++
    @Test
    public void WriteunaryAssignmentTest()
    {
        // In the example test variable c is increased by c++

        MethodDeclaration md = setupTestClass("ExtractMethodMarkerCases", "WriteMarkers");

        MethodDataFlowAnalyzer analyzer = new MethodDataFlowAnalyzer(md);

        LocalVariableWrittenMarker wMark = new LocalVariableWrittenMarker(md, analyzer.getVariableFlowSet());
        wMark.mark();

        VariableFlowSet dataFlowSet = analyzer.getVariableFlowSet();

        VariableFlowTable varFT = dataFlowSet.getVariableFlowTable("c");
        Assert.assertTrue(varFT.within_region.write);

        dataFlowSet.getListOfVariableFlowTables().forEach(flowTable ->
            {
                if (flowTable.within_region.write)
                {
                    System.out.println("Variable " + flowTable.name + " is WRITTEN");
                }
                else
                {
                    System.out.println(flowTable.name);
                }
            }
        );
    }

    // Case int a = 4
    @Test
    public void DeclaredWithInitializationTest()
    {
        MethodDeclaration md = setupTestClass("ExtractMethodMarkerCases", "WriteMarkers");

        MethodDataFlowAnalyzer analyzer = new MethodDataFlowAnalyzer(md);

        LocalVariableWrittenMarker wMark = new LocalVariableWrittenMarker(md, analyzer.getVariableFlowSet());
        wMark.mark();

        VariableFlowSet dataFlowSet = analyzer.getVariableFlowSet();

        VariableFlowTable varFT = dataFlowSet.getVariableFlowTable("a");
        Assert.assertTrue(varFT.within_region.write);
    }

    // Case: b = a + 2
    @Test
    public void WriteAssignmentTest()
    {
        MethodDeclaration md = setupTestClass("ExtractMethodMarkerCases", "WriteMarkers");

        MethodDataFlowAnalyzer analyzer = new MethodDataFlowAnalyzer(md);

        LocalVariableWrittenMarker wMark = new LocalVariableWrittenMarker(md, analyzer.getVariableFlowSet());
        wMark.mark();

        VariableFlowSet dataFlowSet = analyzer.getVariableFlowSet();

        // b should be marked written
        VariableFlowTable varFT = dataFlowSet.getVariableFlowTable("b");
        Assert.assertTrue(varFT.within_region.write);
    }

    // Case: Test the LocalVariableWrittenMarker as being integrated in MethodDataFlowAnalyzer
    @Test
    public void MethodDataFlowAnalyzerIntegrationTest()
    {
        MethodDeclaration md = setupTestClass("ExtractMethodMarkerCases", "WriteMarkers");

        MethodDataFlowAnalyzer analyzer = new MethodDataFlowAnalyzer(md);
        analyzer.start();

        VariableFlowSet dataFlowSet = analyzer.getVariableFlowSet();

        VariableFlowTable varFT = dataFlowSet.getVariableFlowTable("a");
        Assert.assertTrue(varFT.within_region.write);
    }

    // Case: d = (b<c) ? b : c;
    // Expected outcome: b and c are read
    @Test
    public void ReadTertiaryTests()
    {
        MethodDeclaration md = setupTestClass("ExtractMethodMarkerCases", "ReadTertiaryMarkers");

        MethodDataFlowAnalyzer analyzer = new MethodDataFlowAnalyzer(md);

        LocalVariableReadMarker wMark = new LocalVariableReadMarker(md, analyzer.getVariableFlowSet());
        wMark.mark();

        VariableFlowSet dataFlowSet = analyzer.getVariableFlowSet();

        VariableFlowTable varFT = dataFlowSet.getVariableFlowTable("b");
        Assert.assertTrue(varFT.within_region.read);
        varFT = dataFlowSet.getVariableFlowTable("c");
        Assert.assertTrue(varFT.within_region.read);
        varFT = dataFlowSet.getVariableFlowTable("a");
        Assert.assertFalse(varFT.within_region.read);
    }

    // Case: a = b;
    // Expected outcome: b is marked read
    @Test
    public void ReadAssignmentTest()
    {
        MethodDeclaration md = setupTestClass("ExtractMethodMarkerCases", "ReadVariableInOtherVariable");

        MethodDataFlowAnalyzer analyzer = new MethodDataFlowAnalyzer(md);

        LocalVariableReadMarker wMark = new LocalVariableReadMarker(md, analyzer.getVariableFlowSet());
        wMark.mark();

        VariableFlowSet dataFlowSet = analyzer.getVariableFlowSet();

        VariableFlowTable varFT = dataFlowSet.getVariableFlowTable("b");
        Assert.assertTrue(varFT.within_region.read);
        varFT = dataFlowSet.getVariableFlowTable("a");
        Assert.assertFalse(varFT.within_region.read);
    }

    // Case: PassOn(a);
    // Expected outcome: a is marked read
    @Test
    public void ReadParameterPassingTestWithoutReturn()
    {
        MethodDeclaration md = setupTestClass("ExtractMethodMarkerCases", "PassingVariablesToMethods");

        MethodDataFlowAnalyzer analyzer = new MethodDataFlowAnalyzer(md);

        LocalVariableReadMarker wMark = new LocalVariableReadMarker(md, analyzer.getVariableFlowSet());
        wMark.mark();

        VariableFlowSet dataFlowSet = analyzer.getVariableFlowSet();

        VariableFlowTable varFT = dataFlowSet.getVariableFlowTable("a");
        Assert.assertTrue(varFT.within_region.read);
    }

    // Case: PassOn(a, b);
    // Expected outcome: a, b is marked read
    @Test
    public void ReadMultipleParameterPassingTestWithoutReturn()
    {
        MethodDeclaration md = setupTestClass("ExtractMethodMarkerCases", "PassingNVariablesToMethods");

        MethodDataFlowAnalyzer analyzer = new MethodDataFlowAnalyzer(md);

        LocalVariableReadMarker wMark = new LocalVariableReadMarker(md, analyzer.getVariableFlowSet());
        wMark.mark();

        VariableFlowSet dataFlowSet = analyzer.getVariableFlowSet();

        VariableFlowTable varFT = dataFlowSet.getVariableFlowTable("a");
        Assert.assertTrue(varFT.within_region.read);
        varFT = dataFlowSet.getVariableFlowTable("b");
        Assert.assertTrue(varFT.within_region.read);
        varFT = dataFlowSet.getVariableFlowTable("c");
        Assert.assertFalse(varFT.within_region.read);
    }
}
