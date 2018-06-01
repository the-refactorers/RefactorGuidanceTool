package analysis.context;

import ait.CodeContext;
import analysis.MethodAnalyzer.ClassMethodFinder;
import analysis.MethodAnalyzer.MethodDescriber;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MethodSingleDeclaration extends ContextDetector {

    private ClassMethodFinder _analyzer = null;
    private MethodDescriber _method = null;

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
                getParameters().addSingleMethodName(this._method.fullTypeSignature());
                getParameters().addSingleClassName(this._analyzer.getQualifiedClassName());
            }
        }
        else
        {
            throw(new Exception("Analyzer = null"));
        }

        return !getParameters().getCollection().isEmpty();
    }

    @Override
    public CodeContext.CodeContextEnum getType() {
        return CodeContext.CodeContextEnum.MethodSingleDeclaration;
    }
}
