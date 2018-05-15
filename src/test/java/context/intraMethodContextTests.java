package context;

import analysis.JavaParserTestSetup;
import analysis.context.CodeSection;
import analysis.context.MethodExtractNoneLocalDependencies;
import analysis.context.MethodExtractSingleArgument;
import analysis.dataflow.MethodDataFlowAnalyzer;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.symbolsolver.javaparser.Navigator;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class intraMethodContextTests extends JavaParserTestSetup {

    @Test
    public void detectNoneLocalVarDependencies()
    {
        try {
            MethodDeclaration md = setupTestClass("ExtractMethodCases", "ExtractionWithoutDependencies");

            MethodDataFlowAnalyzer _analyzer = new MethodDataFlowAnalyzer(md);
            MethodExtractNoneLocalDependencies nlvdCtxt = new MethodExtractNoneLocalDependencies(_analyzer);

            CodeSection cs = new CodeSection(7, 10);
            _analyzer.setExtractSection(cs.begin(),cs.end());

            assertEquals(true, nlvdCtxt.detect());
        }
        catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void detectSingleParameter()
    {
            try {
                MethodDeclaration md = setupTestClass("ExtractMethodCases", "ExtractionWith1Input");

                MethodDataFlowAnalyzer _analyzer = new MethodDataFlowAnalyzer(md);
                MethodExtractSingleArgument mesp = new MethodExtractSingleArgument(_analyzer);
                CodeSection cs = new CodeSection(19, 22);
                _analyzer.setExtractSection(cs.begin(),cs.end());

                assertEquals(true, mesp.detect());
            }
            catch (Exception e) {
                e.printStackTrace();
                fail();
            }
    }

}
