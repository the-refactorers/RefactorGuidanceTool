package analysis.context;

import aig.CodeContext;
import analysis.MethodAnalyzer.ClassMethodFinder;
import analysis.MethodAnalyzer.MethodDescriber;

public class MethodNoneOverride extends ContextDetector {

    MethodOverride _mo = null;

    public MethodNoneOverride(ContextConfiguration cc)    {

        super(cc);
        _mo = new MethodOverride(cc);
    }

    public MethodNoneOverride(ClassMethodFinder cmf, MethodDescriber method) {
        _mo = new MethodOverride(cmf, method);
    }

    @Override
    public boolean detect() throws Exception {
        return !_mo.detect();
    }

    @Override
    public CodeContext.CodeContextEnum getType() {
        return CodeContext.CodeContextEnum.MethodNoneOverride;
    }
}
