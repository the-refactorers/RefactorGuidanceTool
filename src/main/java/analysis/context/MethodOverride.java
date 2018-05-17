package analysis.context;

import ait.CodeContext;
import analysis.MethodAnalyzer.ClassMethodFinder;

import java.util.Map;

public class MethodOverride implements IContextDetector{

    private ClassMethodFinder _analyzer = null;
    private String _methodName = null;

    public MethodOverride(ClassMethodFinder cmf, String methodName) {
        this._analyzer = cmf;
        this._methodName = methodName;
    }

    public MethodOverride(ContextConfiguration cc) {
        this._analyzer = cc.getCMFAnalyzer();
        this._methodName = cc.getMethodName();
    }

    @Override
    public boolean detect() throws Exception {
        return false;
    }

    @Override
    public Map<String,String> getParameterMap() {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public CodeContext.CodeContextEnum getType() {
        return CodeContext.CodeContextEnum.MethodOverride;
    }

}
