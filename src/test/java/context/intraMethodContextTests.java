package context;

import analysis.JavaParserTestSetup;
import analysis.context.CodeSection;
import analysis.context.NoneLocalVarDependencies;
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

            CodeSection cs = new CodeSection(7, 10);
            _analyzer.setExtractSection(cs.begin(),cs.end());

            NoneLocalVarDependencies nlvdCtxt = new NoneLocalVarDependencies(_analyzer);

            assertEquals(true, nlvdCtxt.detect());
        }
        catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }
}
