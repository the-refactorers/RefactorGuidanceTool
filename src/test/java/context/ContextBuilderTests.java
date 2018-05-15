package context;

import analysis.context.ContextDetectorBuilder;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ContextBuilderTests {

    @Test
    public void getReflectiveContext()
    {
        ContextDetectorBuilder cb = new ContextDetectorBuilder();
        assertEquals(1, cb.getContextDetectorsReflective().size());
    }
}
