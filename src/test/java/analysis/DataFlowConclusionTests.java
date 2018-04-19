package analysis;

import analysis.dataflow.MethodDataFlowAnalyzer;
import analysis.dataflow.VariableFlowSet;
import analysis.dataflow.VariableFlowTable;
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

    private MethodDeclaration setupTestClass(String className, String methodName) {
        //String className = "ExtractMethodMarkerCases";
        CreateCompilationUnitFromTestClass(className + ".java.txt");
        return findMethodDeclarationInClass(className, methodName);
    }

    @Test
    public void ExtractCodeWithoutDependenciesNoInputNeeded()
    {
        MethodDeclaration md = setupTestClass("ExtractMethodCases", "ExtractionWithoutDependencies");
        MethodDataFlowAnalyzer analyzer = new MethodDataFlowAnalyzer(md);

        analyzer.setExtractSection(7,10);
        analyzer.start();

        List<String> vfi = analyzer.variablesForInput();
        Assert.assertEquals(0, vfi.size());
    }

    @Test
    public void ExtractCodeWith1ParamInput()
    {
        MethodDeclaration md = setupTestClass("ExtractMethodCases", "ExtractionWith1Input");
        MethodDataFlowAnalyzer analyzer = new MethodDataFlowAnalyzer(md);

        analyzer.setExtractSection(19,22);
        analyzer.start();

        List<String> vfi = analyzer.variablesForInput();
        Assert.assertEquals(1, vfi.size());
    }

    @Test
    public void ExtractCodeWith2ParamInput()
    {
        MethodDeclaration md = setupTestClass("ExtractMethodCases", "ExtractionWith2Input");
        MethodDataFlowAnalyzer analyzer = new MethodDataFlowAnalyzer(md);

        analyzer.setExtractSection(32,35);
        analyzer.start();

        List<String> vfi = analyzer.variablesForInput();
        Assert.assertEquals(2, vfi.size());
    }

    @Test
    public void ExtractCodeWith1ParamOutput()
    {
        MethodDeclaration md = setupTestClass("ExtractMethodCases", "ExtractionWith1Output");
        MethodDataFlowAnalyzer analyzer = new MethodDataFlowAnalyzer(md);

        analyzer.setExtractSection(43,46);
        analyzer.start();

        List<String> vfi = analyzer.variablesForOutput();
        Assert.assertEquals(1, vfi.size());
    }
}
