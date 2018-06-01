package analysis.context;

import ait.CodeContext;
import analysis.MethodAnalyzer.ClassMethodFinder;
import analysis.MethodAnalyzer.MethodDescriber;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.*;

public class MethodMultipleDeclarations implements IContextDetector {

    private ClassMethodFinder _analyzer = null;
    private MethodDescriber _method = null;

    private ParameterCollector _params = new ParameterCollector();

    public MethodMultipleDeclarations(ClassMethodFinder cmf, MethodDescriber md)
    {
        this._analyzer = cmf;
        this._method = md;
    }

    public MethodMultipleDeclarations(ContextConfiguration cc) {
        this._analyzer = cc.getCMFAnalyzer();
        this._method   = cc.getMethodDescriber();
    }

    public boolean detect() throws Exception {

        if(_analyzer != null)
        {
            if(_analyzer.isMethodDefinedInSuperClass(_method) ||
                _analyzer.isMethodDeclaredFirstTimeInInterface(_method))
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

    @Override
    public ParameterCollector getParameters() {
        return _params;
    }

    @Override
    public CodeContext.CodeContextEnum getType() {
        return CodeContext.CodeContextEnum.MethodMultipleDeclarations;
    }

}
