package analysis.context;

import ait.CodeContext;

import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumSet;
import java.util.List;

public class ContextAnalyzer {

    List<IContextDetector> _contextDetectors = new ArrayList<IContextDetector>();
    EnumSet<CodeContext.CodeContextEnum> _detectedSet = EnumSet.noneOf(CodeContext.CodeContextEnum.class);

    public void setContextDetectors(List<IContextDetector> detectors) {
        this._contextDetectors = detectors;
    }

    public EnumSet<CodeContext.CodeContextEnum> getDetectedContextSet()
    {
        return _detectedSet;
    }

    public void run()
    {
        for(IContextDetector detector : _contextDetectors)
            try {
                if (detector.detect()) {
                    _detectedSet.add(detector.getType());
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
    }
}
