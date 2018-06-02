package context;

import aig.AIG_RenameGeneration;
import aig.AdaptiveInstructionGraph;
import analysis.context.ContextConfiguration;
import analysis.context.ContextDetectorSetBuilder;
import org.junit.Test;

import static junit.framework.TestCase.fail;
import static org.junit.Assert.assertEquals;

public class ContextBuilderTests {

    @Test(expected=Exception.class)
    public void contextDetectorsCanNotBeRetrievedWithoutConfigObject() throws Exception {
        ContextDetectorSetBuilder cdsb = new ContextDetectorSetBuilder();
        cdsb.getContextDetectors();
    }

    @Test
    public void getReflectiveContext()
    {
        ContextDetectorSetBuilder cb = new ContextDetectorSetBuilder();
        AdaptiveInstructionGraph aig = new AIG_RenameGeneration().getAdaptiveInstructionGraph();
        cb.setAIT(aig);

        int nrOfContextInGraph = aig.allSpecializedCodeContextInGraph().size();

        try {
            cb.setContextConfiguration(new ContextConfiguration());
            int nrOfContextDetectors = cb.getContextDetectors().size();

            //-1, because for always_true no detector exists
            assertEquals(nrOfContextInGraph, nrOfContextDetectors);
        }
        catch(Exception e)
        {
            fail(e.getMessage());
        }
    }
}
