package analysis.context;

import ait.CodeContext;
import analysis.MethodAnalyzer.ClassMethodFinder;

public class MethodSingleDeclaration implements IContextDetector {

    ClassMethodFinder _analyzer = null;
    String _methodName = null;

    public MethodSingleDeclaration(ClassMethodFinder cmf, String methodName)
    {
        _analyzer = cmf;
        _methodName = methodName;
    }

    @Override
    public boolean detect() throws Exception {
        boolean result = false;

        if(_analyzer != null)
        {
            result = !_analyzer.isMethodDefinedInSuperClass(_methodName) &&
                    !_analyzer.isMethodDeclaredFirstTimeInInterface(_methodName);
        }
        else
        {
            throw(new Exception("Analyzer = null"));
        }

        return result;
    }

    @Override
    public CodeContext.CodeContextEnum getType() {
        return CodeContext.CodeContextEnum.MethodSingleDeclaration;
    }
}
