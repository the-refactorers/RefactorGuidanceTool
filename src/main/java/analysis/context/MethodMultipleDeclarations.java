package analysis.context;

import ait.CodeContext;
import analysis.MethodAnalyzer.ClassMethodFinder;

import java.util.HashMap;
import java.util.Map;

public class MethodMultipleDeclarations implements IContextDetector {

    private ClassMethodFinder _analyzer = null;
    private String _methodName = null;

    private Map<String,String> _parameterMap = new HashMap<String, String>();

    public MethodMultipleDeclarations(ClassMethodFinder cmf, String methodName) {
        this._analyzer = cmf;
        this._methodName = methodName;
    }

    public MethodMultipleDeclarations(ContextConfiguration cc) {
        this._analyzer = cc.getCMFAnalyzer();
        this._methodName = cc.getMethodName();
    }

    public boolean detect() throws Exception {
        boolean result = false;

        if(_analyzer != null)
        {
            if(_analyzer.isMethodDefinedInSuperClass(_methodName) ||
                _analyzer.isMethodDeclaredFirstTimeInInterface(_methodName))

            {
                _parameterMap.put("$method", this._methodName);
                _parameterMap.put("$class", this._analyzer.getQualifiedMethodName());
                result = true;
            }
        }
        else
        {
            throw(new Exception("Analyzer = null"));
        }

        return result;
    }

    @Override
    public Map<String,String> getParameterMap() {
        return _parameterMap;
    }

    @Override
    public CodeContext.CodeContextEnum getType() {
        return CodeContext.CodeContextEnum.MethodMultipleDeclarations;
    }

}
