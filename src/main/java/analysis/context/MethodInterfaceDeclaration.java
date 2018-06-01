package analysis.context;

import ait.CodeContext;
import analysis.MethodAnalyzer.ClassMethodFinder;
import analysis.MethodAnalyzer.MethodDescriber;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MethodInterfaceDeclaration implements IContextDetector {

    private ClassMethodFinder _analyzer = null;
    private MethodDescriber _method = null;
    private Map<String,List<String>> _parameterMap = new HashMap<>();

    public MethodInterfaceDeclaration(ClassMethodFinder cmf, MethodDescriber method) {
        this._analyzer = cmf;
        this._method = method;
    }

    public MethodInterfaceDeclaration(ContextConfiguration cc) {
        this._analyzer = cc.getCMFAnalyzer();
        this._method = cc.getMethodDescriber();
    }

    public boolean detect() throws Exception {
        boolean result = false;

        if(_analyzer != null)
        {
            if(_analyzer.isMethodDeclaredFirstTimeInInterface(_method))
            {
                _parameterMap.put("#interface", Arrays.asList(_analyzer.methodDefinedInInterface()));
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
    public Map<String,List<String>> getParameterMap() {
        return _parameterMap;
    }

    public ParameterCollector getParameters() {
        throw new NotImplementedException();
    }

    @Override
    public CodeContext.CodeContextEnum getType() {
        return CodeContext.CodeContextEnum.MethodInterfaceDeclaration;
    }
}
