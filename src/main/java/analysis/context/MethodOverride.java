package analysis.context;

import ait.CodeContext;
import analysis.MethodAnalyzer.ClassMethodFinder;

public class MethodOverride implements IContextDetector{

    private ClassMethodFinder _analyzer = null;
    private String _methodName = null;

    public MethodOverride(ClassMethodFinder cmf, String methodName) {
        this._analyzer = cmf;
        this._methodName = methodName;
    }

    @Override
    public boolean detect() throws Exception {
        return false;
    }

    @Override
    public CodeContext.CodeContextEnum getType() {
        return CodeContext.CodeContextEnum.MethodOverride;
    }

}
