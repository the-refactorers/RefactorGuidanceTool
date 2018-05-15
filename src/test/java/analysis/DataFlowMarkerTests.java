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

    // Case: One variable is defined.
    // Expected outcome: 1 varFlowTable which has the same name as the declared variable in the test code
    @Test
    public void variableFlowTableCreation()
    {
        MethodDeclaration md = setupTestClass("ExtractMethodZeroInZeroOut", "MethodOneLocalDeclared");

        MethodDataFlowAnalyzer analyzer = new MethodDataFlowAnalyzer();
        analyzer.setMethod(md);

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

        MethodDataFlowAnalyzer analyzer = new MethodDataFlowAnalyzer();
        analyzer.setMethod(md);

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

        MethodDataFlowAnalyzer analyzer = new MethodDataFlowAnalyzer();
        analyzer.setMethod(md);

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

        MethodDataFlowAnalyzer analyzer = new MethodDataFlowAnalyzer();
        analyzer.setMethod(md);

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
        MethodDeclaration md = setupTestClass("ExtractMethodMarkerCases", "ReadWriteMarkers");

        MethodDataFlowAnalyzer analyzer = new MethodDataFlowAnalyzer();
        analyzer.setMethod(md);
        analyzer.start();

        VariableFlowSet dataFlowSet = analyzer.getVariableFlowSet();

        VariableFlowTable varFT = dataFlowSet.getVariableFlowTable("a");
        Assert.assertTrue(varFT.within_region.write);
        varFT = dataFlowSet.getVariableFlowTable("b");
        Assert.assertTrue(varFT.within_region.read);
    }

    // Case: d = (b<c) ? b : c;
    // Expected outcome: b and c are read
    @Test
    public void ReadTertiaryTests()
    {
        MethodDeclaration md = setupTestClass("ExtractMethodMarkerCases", "ReadTertiaryMarkers");

        MethodDataFlowAnalyzer analyzer = new MethodDataFlowAnalyzer();
        analyzer.setMethod(md);

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

        MethodDataFlowAnalyzer analyzer = new MethodDataFlowAnalyzer();
        analyzer.setMethod(md);

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

        MethodDataFlowAnalyzer analyzer = new MethodDataFlowAnalyzer();
        analyzer.setMethod(md);

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

        MethodDataFlowAnalyzer analyzer = new MethodDataFlowAnalyzer();
        analyzer.setMethod(md);

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

    // Case: method with only declarations and write actions, should not mark any of the variables written
    @Test
    public void ReadNoneOfLocalVars()
    {
        MethodDeclaration md = setupTestClass("ExtractMethodMarkerCases", "noReads");

        MethodDataFlowAnalyzer analyzer = new MethodDataFlowAnalyzer();
        analyzer.setMethod(md);

        LocalVariableReadMarker wMark = new LocalVariableReadMarker(md, analyzer.getVariableFlowSet());
        wMark.mark();

        VariableFlowSet dataFlowSet = analyzer.getVariableFlowSet();

        VariableFlowTable varFT = dataFlowSet.getVariableFlowTable("a");
        Assert.assertFalse(varFT.within_region.read);
    }

    // Case: When a variable is read in the before section, and read in the within with a direct write it should be marked
    // read + write in the within section  before: b=3    ;  within: b = b + 6;
    @Test
    public void VariableAssignmentByFirstReading()
    {
        MethodDeclaration md = setupTestClass("ExtractMethodMarkerCases", "WriteVariableBasedOnOwnValue");

        MethodDataFlowAnalyzer analyzer = new MethodDataFlowAnalyzer();
        analyzer.setMethod(md);

        LocalVariableReadMarker wMark = new LocalVariableReadMarker(md, analyzer.getVariableFlowSet());
        wMark.mark();

        VariableFlowSet dataFlowSet = analyzer.getVariableFlowSet();

        VariableFlowTable varFT = dataFlowSet.getVariableFlowTable("a");
        Assert.assertTrue(varFT.within_region.read);
    }

    // Case: b = b > 10 ? 0 : b
    // In this case the variable is first read; followed by a write
    @Test
    public void VariableReadingTertiaryWriteToSame()
    {
        MethodDeclaration md = setupTestClass("ExtractMethodMarkerCases", "ReadingOfTertiaryWriteToSame");

        MethodDataFlowAnalyzer analyzer = new MethodDataFlowAnalyzer();
        analyzer.setMethod(md);

        LocalVariableReadMarker wMark = new LocalVariableReadMarker(md, analyzer.getVariableFlowSet());
        wMark.mark();

        VariableFlowSet dataFlowSet = analyzer.getVariableFlowSet();

        VariableFlowTable varFT = dataFlowSet.getVariableFlowTable("b");
        Assert.assertTrue(varFT.within_region.read);
    }

    // Case: C++; is actually 1st read C then followed bij writing new value to C
    @Test
    public void VariableReadingUnaryOperation()
    {
        MethodDeclaration md = setupTestClass("ExtractMethodMarkerCases", "ReadingOfUnaryOperation");

        MethodDataFlowAnalyzer analyzer = new MethodDataFlowAnalyzer();
        analyzer.setMethod(md);

        LocalVariableReadMarker wMark = new LocalVariableReadMarker(md, analyzer.getVariableFlowSet());
        wMark.mark();

        VariableFlowSet dataFlowSet = analyzer.getVariableFlowSet();

        VariableFlowTable varFT = dataFlowSet.getVariableFlowTable("c");
        Assert.assertTrue(varFT.within_region.read);
    }


    @Test
    public void VariableReadingIfStatement()
    {
        MethodDeclaration md = setupTestClass("ExtractMethodMarkerCases", "ReadingInIfStatement");

        MethodDataFlowAnalyzer analyzer = new MethodDataFlowAnalyzer();
        analyzer.setMethod(md);

        LocalVariableReadMarker wMark = new LocalVariableReadMarker(md, analyzer.getVariableFlowSet());
        wMark.mark();

        VariableFlowSet dataFlowSet = analyzer.getVariableFlowSet();

        VariableFlowTable varFT = dataFlowSet.getVariableFlowTable("c");
        Assert.assertTrue(varFT.within_region.read);
    }

    @Test
    public void testSectionMarkingCorrect()
    {
        MethodDeclaration md = setupTestClass("ExtractMethodMarkerCases", "PassingVariablesToMethods");

        MethodDataFlowAnalyzer analyzer = new MethodDataFlowAnalyzer();
        analyzer.setMethod(md);

        LocalVariableReadMarker wMark = new LocalVariableReadMarker(md, analyzer.getVariableFlowSet());
        wMark.mark();

        VariableFlowSet dataFlowSet = analyzer.getVariableFlowSet();

        VariableFlowTable varFT = dataFlowSet.getVariableFlowTable("a");

        // Because no extract range has been set, only updates in the within section are allowed
        // in this case write = true
        Assert.assertFalse(varFT.within_region.areAllFactsFalse());
        Assert.assertTrue(varFT.before_region.areAllFactsFalse());
        Assert.assertTrue(varFT.after_region.areAllFactsFalse());
    }

    @Test
    public void LiveMarkerNoWriteBeforeRead()
    {
        MethodDeclaration md = setupTestClass("ExtractMethodMarkerCases", "LiveMarker");

        MethodDataFlowAnalyzer analyzer = new MethodDataFlowAnalyzer();
        analyzer.setMethod(md);
        analyzer.start();

        VariableFlowSet dataFlowSet = analyzer.getVariableFlowSet();

        VariableFlowTable varFT = dataFlowSet.getVariableFlowTable("b");
        Assert.assertTrue(varFT.within_region.live);
        varFT = dataFlowSet.getVariableFlowTable("a");
        Assert.assertFalse(varFT.within_region.live);
    }

    @Test
    public void LiveMarkerWriteAfterRead()
    {
        MethodDeclaration md = setupTestClass("ExtractMethodMarkerCases", "LiveMarkerWriteAfterRead");

        MethodDataFlowAnalyzer analyzer = new MethodDataFlowAnalyzer();
        analyzer.setMethod(md);
        analyzer.start();

        VariableFlowSet dataFlowSet = analyzer.getVariableFlowSet();

        // Write occurs after read of 'b'; so this variable is live in section (it is used without any change)
        VariableFlowTable varFT = dataFlowSet.getVariableFlowTable("b");
        Assert.assertTrue(varFT.within_region.live);
    }
}
