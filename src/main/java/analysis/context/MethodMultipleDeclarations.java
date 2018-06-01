package analysis.context;

import ait.CodeContext;
import analysis.MethodAnalyzer.ClassMethodFinder;
import analysis.MethodAnalyzer.MethodDescriber;

public class MethodMultipleDeclarations extends ContextDetector {

    private ClassMethodFinder _analyzer = null;
    private MethodDescriber _method = null;

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
        return CodeContext.CodeContextEnum.MethodMultipleDeclarations;
    }

}
