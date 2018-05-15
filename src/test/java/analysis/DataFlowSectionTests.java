package analysis;

import analysis.dataflow.MethodDataFlowAnalyzer;
import analysis.dataflow.VariableFlowSet;
import analysis.dataflow.VariableFlowTable;
import com.github.javaparser.ast.body.MethodDeclaration;
import org.junit.Assert;
import org.junit.Test;

/**
 * Some tests to see if markers are placed in the correct section (BEFORE, AFTER, WITHIN)
 */
public class DataFlowSectionTests extends JavaParserTestSetup {

   // Case: Invalid section result in marking all variables in within section
    @Test
    public void WriteWithInvalidSectionsTest()
    {
        MethodDeclaration md = setupTestClass("ExtractMethodMarkerCases", "WriteMarkers");
        MethodDataFlowAnalyzer analyzer = new MethodDataFlowAnalyzer();
        analyzer.setMethod(md);

        analyzer.setExtractSection(0,0);
        analyzer.start();
        VariableFlowSet dataFlowSet = analyzer.getVariableFlowSet();

        VariableFlowTable varFT = dataFlowSet.getVariableFlowTable("a");
        Assert.assertTrue(varFT.within_region.write);
        varFT = dataFlowSet.getVariableFlowTable("b");
        Assert.assertTrue(varFT.within_region.write);
        varFT = dataFlowSet.getVariableFlowTable("g");
        Assert.assertTrue(varFT.within_region.write);

        analyzer.setExtractSection(-1,-1);
        analyzer.start();
        dataFlowSet = analyzer.getVariableFlowSet();

        varFT = dataFlowSet.getVariableFlowTable("a");
        Assert.assertTrue(varFT.within_region.write);
        varFT = dataFlowSet.getVariableFlowTable("b");
        Assert.assertTrue(varFT.within_region.write);
        varFT = dataFlowSet.getVariableFlowTable("g");
        Assert.assertTrue(varFT.within_region.write);
    }

    @Test
    public void WriteValidSectionsBeforeTest() {
        MethodDeclaration md = setupTestClass("ExtractMethodMarkerCases", "WriteMarkers");
        MethodDataFlowAnalyzer analyzer = new MethodDataFlowAnalyzer();
        analyzer.setMethod(md);

        analyzer.setExtractSection(16, 17);
        analyzer.start();
        VariableFlowSet dataFlowSet = analyzer.getVariableFlowSet();

        //before
        VariableFlowTable varFT = dataFlowSet.getVariableFlowTable("a");
        Assert.assertTrue(varFT.before_region.write);
    }

    @Test
    public void WriteValidSectionsWithinTest() {
        MethodDeclaration md = setupTestClass("ExtractMethodMarkerCases", "WriteMarkers");
        MethodDataFlowAnalyzer analyzer = new MethodDataFlowAnalyzer();
        analyzer.setMethod(md);

        analyzer.setExtractSection(16, 17);
        analyzer.start();
        VariableFlowSet dataFlowSet = analyzer.getVariableFlowSet();

        VariableFlowTable varFT = dataFlowSet.getVariableFlowTable("c");
        Assert.assertTrue(varFT.within_region.write);
        varFT = dataFlowSet.getVariableFlowTable("f");
        Assert.assertTrue(varFT.within_region.write);
    }

    @Test
    public void WriteValidSectionsAfterTest() {
        MethodDeclaration md = setupTestClass("ExtractMethodMarkerCases", "WriteMarkers");
        MethodDataFlowAnalyzer analyzer = new MethodDataFlowAnalyzer();
        analyzer.setMethod(md);

        analyzer.setExtractSection(16, 17);
        analyzer.start();
        VariableFlowSet dataFlowSet = analyzer.getVariableFlowSet();

        //after
        VariableFlowTable varFT = dataFlowSet.getVariableFlowTable("g");
        Assert.assertTrue(varFT.after_region.write);
    }

    @Test
    public void ReadValidSectionsBeforeTest() {
        MethodDeclaration md = setupTestClass("ExtractMethodMarkerCases", "multipleReads");
        MethodDataFlowAnalyzer analyzer = new MethodDataFlowAnalyzer();
        analyzer.setMethod(md);

        analyzer.setExtractSection(81, 82);
        analyzer.start();
        VariableFlowSet dataFlowSet = analyzer.getVariableFlowSet();

        VariableFlowTable varFT = dataFlowSet.getVariableFlowTable("a");
        Assert.assertTrue(varFT.before_region.read);
        varFT = dataFlowSet.getVariableFlowTable("b");
        Assert.assertFalse(varFT.before_region.read);
        varFT = dataFlowSet.getVariableFlowTable("c");
        Assert.assertFalse(varFT.before_region.read);
    }

    @Test
    public void ReadValidSectionsWithinTest() {
        MethodDeclaration md = setupTestClass("ExtractMethodMarkerCases", "multipleReads");
        MethodDataFlowAnalyzer analyzer = new MethodDataFlowAnalyzer();
        analyzer.setMethod(md);

        analyzer.setExtractSection(81, 82);
        analyzer.start();
        VariableFlowSet dataFlowSet = analyzer.getVariableFlowSet();

        VariableFlowTable varFT = dataFlowSet.getVariableFlowTable("a");
        Assert.assertTrue(varFT.before_region.read);
        //varFT = dataFlowSet.getVariableFlowTable("f");
        //Assert.assertTrue(varFT.within_region.write);
    }

    @Test
    public void ReadValidSectionsAfterTest() {
        MethodDeclaration md = setupTestClass("ExtractMethodMarkerCases", "multipleReads");
        MethodDataFlowAnalyzer analyzer = new MethodDataFlowAnalyzer();

        analyzer.setMethod(md);
        analyzer.setExtractSection(81, 82);

        analyzer.start();
        VariableFlowSet dataFlowSet = analyzer.getVariableFlowSet();

        //after
        VariableFlowTable varFT = dataFlowSet.getVariableFlowTable("c");
        Assert.assertTrue(varFT.after_region.read);
    }

    @Test
    public void ReadWriteMultipleReadExample()
    {
        MethodDeclaration md = setupTestClass("ExtractMethodMarkerCases", "multipleReads");
        MethodDataFlowAnalyzer analyzer = new MethodDataFlowAnalyzer();
        analyzer.setMethod(md);

        analyzer.setExtractSection(81, 82);
        analyzer.start();
        VariableFlowSet dataFlowSet = analyzer.getVariableFlowSet();

        VariableFlowTable varFTa = dataFlowSet.getVariableFlowTable("a");
        VariableFlowTable varFTb = dataFlowSet.getVariableFlowTable("b");
        VariableFlowTable varFTc = dataFlowSet.getVariableFlowTable("c");

        // Expected outcome for a,b and c when analyzing multipleReads
        Assert.assertTrue(varFTa.before_region.read);
        Assert.assertTrue(varFTa.before_region.write);
        Assert.assertTrue(varFTa.within_region.read);
        Assert.assertFalse(varFTa.within_region.write);
        Assert.assertFalse(varFTa.after_region.read);
        Assert.assertTrue(varFTa.after_region.write);

        Assert.assertFalse(varFTb.before_region.read);
        Assert.assertTrue(varFTb.before_region.write);
        Assert.assertTrue(varFTb.within_region.read);
        Assert.assertFalse(varFTb.within_region.write);
        Assert.assertFalse(varFTb.after_region.read);
        Assert.assertFalse(varFTb.after_region.write);

        Assert.assertFalse(varFTc.before_region.read);
        Assert.assertFalse(varFTc.before_region.write);
        Assert.assertFalse(varFTc.within_region.read);
        Assert.assertTrue(varFTc.within_region.write);
        Assert.assertTrue(varFTc.after_region.read);
        Assert.assertFalse(varFTc.after_region.write);
    }

}
