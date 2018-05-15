package analysis.context;

import ait.AdaptiveInstructionTree;

import java.util.ArrayList;
import java.util.List;

/**
 *  Class builds up a set of context detectors that are needed to detect contextual situations
 *  in a given adaptive instruction tree. The list of detectors can be provided to the procedural
 *  guidance generator
 */
public class ContextDetectorBuilder {

    AdaptiveInstructionTree _ait = null;
    List<IContextDetector> _contextDetectors = new ArrayList<IContextDetector>();

    public void SetAIT() {

    }

    public void SetAIT(AdaptiveInstructionTree ait)  {
        setAIT(ait);
    }

    public void setAIT(AdaptiveInstructionTree ait) {
        this._ait = ait;
    }
    public List<IContextDetector> getContextDetectors()
    {
        return this._contextDetectors;
    }
}
