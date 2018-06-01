package analysis.context;

import ait.CodeContext;
import analysis.MethodAnalyzer.ClassMethodFinder;
import analysis.MethodAnalyzer.MethodDescriber;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MethodSingleDeclaration implements IContextDetector {

    private ClassMethodFinder _analyzer = null;
    private MethodDescriber _method = null;

    private ParameterCollector _params = new ParameterCollector();

    public MethodSingleDeclaration(ClassMethodFinder cmf, MethodDescriber method) {
        this._analyzer = cmf;
        this._method = method;
    }

    /**
     * Used in generic context builder
     * @param cc
     */
    public MethodSingleDeclaration(ContextConfiguration cc) {
        this._analyzer = cc.getCMFAnalyzer();
        this._method = cc.getMethodDescriber();
    }

    @Override
    public boolean detect() throws Exception {

        if(_analyzer != null)
        {
            if(!_analyzer.isMethodDefinedInSuperClass(_method) &&
                !_analyzer.isMethodDeclaredFirstTimeInInterface(_method))
            {
                _params.addSingleMethodName(this._method.fullTypeSignature());
                _params.addSingleClassName(this._analyzer.getQualifiedClassName());
            }
        }
        else
        {
            throw(new Exception("Analyzer = null"));
        }

        return !_params.getCollection().isEmpty();
    }

    public ParameterCollector getParameters(){
        return _params;
    }

    @Override
    public CodeContext.CodeContextEnum getType() {
        return CodeContext.CodeContextEnum.MethodSingleDeclaration;
    }
}
