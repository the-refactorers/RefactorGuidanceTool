package context;

import ait.CodeContext;
import analysis.JavaParserTestSetup;
import analysis.MethodAnalyzer.ClassMethodFinder;
import analysis.MethodAnalyzer.MethodDescriber;
import analysis.context.CodeSection;
import analysis.context.ContextConfiguration;
import analysis.context.MethodExtractNoneLocalDependencies;
import analysis.context.MethodExtractSingleArgument;
import analysis.context.MethodExtractSingleResult;
import analysis.dataflow.MethodDataFlowAnalyzer;
import com.github.javaparser.ast.body.MethodDeclaration;
import javassist.compiler.ast.MethodDecl;
import org.junit.Assert;
import org.junit.Test;

import static ait.CodeContext.CodeContextEnum.MethodExtractSingleResult;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class intraMethodContextTests extends JavaParserTestSetup {

    @Test
    public void detectNoneLocalVarDependencies()
    {
        try {
            extractRegion(7, 10);
            setupTestClass("ExtractMethodCases", "ExtractionWithoutDependencies");

            mdfaAnalysis();

            ContextConfiguration cc = new ContextConfiguration();
            cc.setMethodDataFlowAnalyzer(_mdfaAna);

            MethodExtractNoneLocalDependencies nlvdCtxt = new MethodExtractNoneLocalDependencies(cc);

            assertEquals(true, nlvdCtxt.detect());
            Assert.assertEquals(CodeContext.CodeContextEnum.MethodExtractNoneLocalDependencies, nlvdCtxt.getType());
        }
        catch (Exception e) {
            e.printStackTrace();
            fail(e.getMessage());
        }
    }

    @Test
    public void detectSingleArgument()
    {
            try {
                extractRegion(19,22);
                setupTestClass("ExtractMethodCases", "ExtractionWith1Input");

                mdfaAnalysis();

                ContextConfiguration cc = new ContextConfiguration();
                cc.setMethodDataFlowAnalyzer(_mdfaAna);

                MethodExtractSingleArgument mesp = new MethodExtractSingleArgument(cc);

                Assert.assertEquals(true, mesp.detect());
                retrieveParams(mesp);

                Assert.assertTrue(_params.get(_pc.getArgumentListType()).contains("a"));
                Assert.assertEquals(1, _params.get(_pc.getArgumentListType()).size());

                Assert.assertEquals(CodeContext.CodeContextEnum.MethodExtractSingleArgument, mesp.getType());
            }
            catch (Exception e) {
                e.printStackTrace();
                fail(e.getMessage());
            }
    }

    @Test
    public void detectSingleResult()
    {
        try {
            extractRegion(43,46);
            setupTestClass("ExtractMethodCases", "ExtractionWith1Output");

            mdfaAnalysis();

            ContextConfiguration cc = new ContextConfiguration();
            cc.setMethodDataFlowAnalyzer(_mdfaAna);

            MethodExtractSingleResult mesr = new MethodExtractSingleResult(cc);

            Assert.assertEquals(true, mesr.detect());
            retrieveParams(mesr);

            Assert.assertTrue(_params.get(_pc.getResultListType()).contains("b"));
            Assert.assertEquals(1, _params.get(_pc.getResultListType()).size());

            Assert.assertEquals(CodeContext.CodeContextEnum.MethodExtractSingleResult, mesr.getType());
        }
        catch (Exception e) {
            e.printStackTrace();
            fail(e.getMessage());
        }
    }
}
