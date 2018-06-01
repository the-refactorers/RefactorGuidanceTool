package analysis.context;

import ait.CodeContext;

import java.util.*;

/**
 * Determines which specific contexts are present in a piece of code in a generic way.
 * by evaluating all given context detectors.
 */
public class ContextAnalyzer {

    List<IContextDetector> _contextDetectors = new ArrayList<>();
    EnumSet<CodeContext.CodeContextEnum> _detectedSet = EnumSet.noneOf(CodeContext.CodeContextEnum.class);
    Map<String, List<String>> _parameterDefinitions = new HashMap<>();

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
                    extendParameterDefinitions(detector);
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
    }

    private void extendParameterDefinitions(IContextDetector detector) {
        Map<String, List<String>> parameters = detector.getParameters().getCollection();
        parameters.forEach((parameter, value) -> _parameterDefinitions.put(parameter, value));
    }

    public Map<String,List<String>> getParameterMap() {
        return _parameterDefinitions;
    }
}
