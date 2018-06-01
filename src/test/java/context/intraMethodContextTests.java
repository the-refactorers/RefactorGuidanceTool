package context;

import analysis.JavaParserTestSetup;
import analysis.context.CodeSection;
import analysis.context.ContextConfiguration;
import analysis.context.MethodExtractNoneLocalDependencies;
import analysis.context.MethodExtractSingleArgument;
import analysis.dataflow.MethodDataFlowAnalyzer;
import com.github.javaparser.ast.body.MethodDeclaration;
import org.junit.Assert;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class intraMethodContextTests extends JavaParserTestSetup {

    @Test
    public void detectNoneLocalVarDependencies()
    {
        try {
            MethodDeclaration md = setupTestClass("ExtractMethodCases", "ExtractionWithoutDependencies");

            CodeSection code2Extract = new CodeSection(7, 10);
            MethodDataFlowAnalyzer _analyzer = new MethodDataFlowAnalyzer();

            _analyzer.initialize(md, code2Extract);

            ContextConfiguration cc = new ContextConfiguration();
            cc.setMethodDataFlowAnalyzer(_analyzer);

            MethodExtractNoneLocalDependencies nlvdCtxt = new MethodExtractNoneLocalDependencies(cc);

            assertEquals(true, nlvdCtxt.detect());
        }
        catch (Exception e) {
            e.printStackTrace();
            fail(e.getMessage());
        }
    }

    @Test
    public void detectSingleParameter()
    {
            try {
                MethodDeclaration md = setupTestClass("ExtractMethodCases", "ExtractionWith1Input");

                MethodDataFlowAnalyzer _analyzer = new MethodDataFlowAnalyzer();
                CodeSection code2Extract = new CodeSection(19, 22);
                _analyzer.initialize(md, code2Extract);

                ContextConfiguration cc = new ContextConfiguration();
                cc.setMethodDataFlowAnalyzer(_analyzer);

                MethodExtractSingleArgument mesp = new MethodExtractSingleArgument(cc);

                Assert.assertEquals(true, mesp.detect());
                retrieveParams(mesp);

                Assert.assertTrue(_params.get(_pc.getArgumentListType()).contains("a"));
                Assert.assertEquals(1, _params.get(_pc.getArgumentListType()).size());
            }
            catch (Exception e) {
                e.printStackTrace();
                fail(e.getMessage());
            }
    }
}
