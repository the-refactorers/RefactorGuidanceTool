package context;

import ait.AIT_RenameGeneration;
import ait.AdaptiveInstructionTree;
import analysis.context.ContextDetectorSetBuilder;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ContextBuilderTests {

    @Test
    public void getReflectiveContext()
    {
        ContextDetectorSetBuilder cb = new ContextDetectorSetBuilder();
        AdaptiveInstructionTree ait = new AIT_RenameGeneration().getAdaptiveInstructionTree();
        cb.setAIT(ait);

        int nrOfContextInTree = ait.allSpecializedCodeContextInTree().size();
        int nrOfContextDetectors = cb.getContextDetectors().size();

        //-1, because for always_true no detector exists
        assertEquals(nrOfContextInTree, nrOfContextDetectors );
    }
}
