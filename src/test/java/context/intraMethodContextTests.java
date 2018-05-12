package context;

import analysis.JavaParserTestSetup;
import analysis.context.CodeSection;
import analysis.context.NoneLocalVarDependencies;
import com.github.javaparser.ast.body.MethodDeclaration;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class intraMethodContextTests extends JavaParserTestSetup {

    @Test
    public void detectNoneLocalVarDependencies()
    {
        NoneLocalVarDependencies nlvdCtxt = new NoneLocalVarDependencies();

        try {
            setupTestClass("ExtractMethodCases", "ExtractionWithoutDependencies");

            nlvdCtxt.setupAnalysis(_cu,
                    "ExtractMethodCases",
                    "ExtractionWithoutDependencies",
                    new CodeSection(7, 10)
                    );

            assertEquals(true, nlvdCtxt.analyse());
        }
        catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }
}
