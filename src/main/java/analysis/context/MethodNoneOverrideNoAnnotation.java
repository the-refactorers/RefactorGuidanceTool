package analysis.context;

import aig.CodeContext;

public class MethodNoneOverrideNoAnnotation extends ContextDetector {

    MethodOverrideNoAnnotation _mowna = null;

    public MethodNoneOverrideNoAnnotation(ContextConfiguration cc)    {

        super(cc);
        _mowna = new MethodOverrideNoAnnotation(cc);
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
