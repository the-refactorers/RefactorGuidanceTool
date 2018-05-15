package context;

import ait.AIT_RenameGeneration;
import ait.AdaptiveInstructionTree;
import analysis.context.ContextDetectorBuilder;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ContextBuilderTests {

    @Test
    public void getReflectiveContext()
    {
        ContextDetectorBuilder cb = new ContextDetectorBuilder();
        AdaptiveInstructionTree ait = new AIT_RenameGeneration().getAdaptiveInstructionTree();
        cb.setAIT(ait);
        int nrOfContextInTree = ait.allUniqueCodeContextInTree().size();
        int nrOfContextDetectors = cb.getContextDetectors().size();
        assertEquals(nrOfContextInTree, nrOfContextDetectors );
    }
}
