package analysis.context;

import aig.CodeContext;
import analysis.MethodAnalyzer.ClassMethodFinder;
import analysis.MethodAnalyzer.MethodDescriber;

public class MethodNoneInterfaceDeclaration extends ContextDetector {

    MethodInterfaceDeclaration _mid = null;

    public MethodNoneInterfaceDeclaration(ContextConfiguration cc)    {

        super(cc);
        _mid = new MethodInterfaceDeclaration(cc);
    }

    public MethodNoneInterfaceDeclaration(ClassMethodFinder cmf, MethodDescriber method) {
        _mid = new MethodInterfaceDeclaration(cmf, method);
    }

    @Override
    public boolean detect() throws Exception {
        return !_mid.detect();
    }

    @Override
    public CodeContext.CodeContextEnum getType() {
        return CodeContext.CodeContextEnum.MethodNoneInterfaceDeclaration;
    }
}
