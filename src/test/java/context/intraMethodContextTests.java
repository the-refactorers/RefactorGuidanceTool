package context;

import ait.CodeContext;
import analysis.JavaParserTestSetup;
import analysis.MethodAnalyzer.ClassMethodFinder;
import analysis.MethodAnalyzer.MethodDescriber;
import analysis.context.*;
import analysis.dataflow.MethodDataFlowAnalyzer;
import com.github.javaparser.ast.body.MethodDeclaration;
import javassist.compiler.ast.MethodDecl;
import org.junit.Assert;
import org.junit.Test;

import javax.naming.Context;

import java.util.List;

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

            ContextConfiguration cc = mdfaAnalysis();
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

                ContextConfiguration cc = mdfaAnalysis();
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

            ContextConfiguration cc = mdfaAnalysis();
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

    @Test
    public void detectMultiArgument()
    {
        try {
            extractRegion(93,96);
            setupTestClass("ExtractMethodCases", "ExtractionWith2Input1NoneRelated");

            ContextConfiguration cc = mdfaAnalysis();
            MethodExtractMultiArgument mema = new MethodExtractMultiArgument(cc);

            Assert.assertEquals(true, mema.detect());
            retrieveParams(mema);

            Assert.assertNotNull(_params.get(_pc.getArgumentListType()));

            Assert.assertEquals(2, _params.get(_pc.getArgumentListType()).size());
            Assert.assertTrue(_params.get(_pc.getArgumentListType()).contains("a"));
            Assert.assertTrue(_params.get(_pc.getArgumentListType()).contains("b"));

            Assert.assertEquals(CodeContext.CodeContextEnum.MethodExtractMultiArgument, mema.getType());
        }
        catch (Exception e) {
            e.printStackTrace();
            fail(e.getMessage());
        }
    }
}