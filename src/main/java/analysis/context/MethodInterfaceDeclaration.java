package analysis.context;

import ait.CodeContext;
import analysis.MethodAnalyzer.ClassMethodFinder;
import analysis.MethodAnalyzer.MethodDescriber;

public class MethodInterfaceDeclaration extends ContextDetector {

    private ClassMethodFinder _analyzer = null;
    private MethodDescriber _method = null;

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
                getParameters().addInterfaceName(_analyzer.methodDefinedInInterface());
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
    public CodeContext.CodeContextEnum getType() {
        return CodeContext.CodeContextEnum.MethodInterfaceDeclaration;
    }
}
