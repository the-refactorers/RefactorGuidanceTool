package context;

import ait.AIT_RenameGeneration;
import ait.AdaptiveInstructionTree;
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
        AdaptiveInstructionTree ait = new AIT_RenameGeneration().getAdaptiveInstructionTree();
        cb.setAIT(ait);

        int nrOfContextInTree = ait.allSpecializedCodeContextInTree().size();

        try {
            cb.setContextConfiguration(new ContextConfiguration());
            int nrOfContextDetectors = cb.getContextDetectors().size();

            //-1, because for always_true no detector exists
            assertEquals(nrOfContextInTree, nrOfContextDetectors);
        }
        catch(Exception e)
        {
            System.out.println(e.getMessage());
            fail();
        }
    }
}
