package analysis.context;

import aig.CodeContext;
import analysis.MethodAnalyzer.ClassMethodFinder;
import analysis.MethodAnalyzer.MethodDescriber;

public class MethodNoneOverrideWithoutNoAnnotation extends ContextDetector {

    MethodOverrideWithoutNoAnnotation _mowna = null;

    public MethodNoneOverrideWithoutNoAnnotation(ContextConfiguration cc)    {

        super(cc);
        _mowna = new MethodOverrideWithoutNoAnnotation(cc);
    }

    @Override
    public boolean detect() throws Exception {
        return !_mowna.detect();
    }

    @Override
    public CodeContext.CodeContextEnum getType() {
        return CodeContext.CodeContextEnum.MethodNoneOverrideNoAnnotation;
    }
}
