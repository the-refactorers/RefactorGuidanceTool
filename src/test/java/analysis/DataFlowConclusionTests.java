package analysis;

import analysis.context.CodeSection;
import analysis.dataflow.MethodDataFlowAnalyzer;
import com.github.javaparser.ast.body.MethodDeclaration;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

/**
 * Some tests to validate if conclusions drawn by the dataflow algorithm are correct
 * about necessary input and output variables when extracting a method from a piece of code within
 * another method
 */
public class DataFlowConclusionTests extends JavaParserTestSetup {

    @Test
    public void ExtractCodeWithoutDependenciesNoInputNeeded()
    {
        extractRegion(7, 10);
        setupTestClass("ExtractMethodCases", "ExtractionWithoutDependencies");

        mdfaAnalysis();

        List<String> vfi = _mdfaAna.variablesForInput();
        Assert.assertEquals(0, vfi.size());
    }

    @Test
    public void ExtractCodeWith1ParamInput()
    {
        extractRegion(19,22);
        setupTestClass("ExtractMethodCases", "ExtractionWith1Input");
        mdfaAnalysis();

        List<String> vfi = _mdfaAna.variablesForInput();
        Assert.assertEquals(1, vfi.size());
    }

    @Test
    public void ExtractCodeWith2ParamInput()
    {
        extractRegion(32,35);
        setupTestClass("ExtractMethodCases", "ExtractionWith2Input");
        mdfaAnalysis();

        List<String> vfi = _mdfaAna.variablesForInput();
        Assert.assertEquals(2, vfi.size());
    }

    @Test
    public void ExtractCodeWith1ParamOutput()
    {
        extractRegion(43,46);
        setupTestClass("ExtractMethodCases", "ExtractionWith1Output");
        mdfaAnalysis();

        List<String> vfi = _mdfaAna.variablesForOutput();
        Assert.assertEquals(1, vfi.size());
    }

    @Test
    public void ExtractCodeWith1ParamOutputAndUseOfParameterAfterThatWasOnlyRead()
    {
        extractRegion(55,58);
        setupTestClass("ExtractMethodCases", "ExtractWith1OutputButVariableUsedAfter");
        mdfaAnalysis();

        List<String> vfi = _mdfaAna.variablesForOutput();
        Assert.assertEquals(1, vfi.size());
    }

    @Test
    public void ExtractCodeWith2Output()
    {
        extractRegion(68,72);
        setupTestClass("ExtractMethodCases", "ExtractWith2Output");
        mdfaAnalysis();

        List<String> vfi = _mdfaAna.variablesForOutput();
        Assert.assertEquals(2, vfi.size());
    }

    @Test
    public void ExtractCodeWhereBeforeVariableIsReadAfterWrittenInWithin()
    {
        extractRegion(82,85);
        setupTestClass("ExtractMethodCases", "ExtractWhereBeforeVariableIsReadAfterWrittenInWithin");
        mdfaAnalysis();

        // Because the variable is assigned, before a read. It is not
        // needed to pass on to extract method
        List<String> vfi = _mdfaAna.variablesForInput();
        Assert.assertEquals(0, vfi.size());
    }
}
