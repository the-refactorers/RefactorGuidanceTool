package analysis.context;

import analysis.MethodAnalyzer.ClassMethodFinder;
import analysis.dataflow.MethodDataFlowAnalyzer;
import com.github.javaparser.ast.CompilationUnit;
import sun.security.krb5.internal.MethodData;

public class ContextConfiguration {

    String _methodName;
    CompilationUnit _cu;
    String _className;

    ClassMethodFinder _cmf;
    MethodDataFlowAnalyzer _mdfa;

    public void setCompilationUnit(CompilationUnit cu) {
        _cu = cu;
    }

    public CompilationUnit getCompilationUnit() {
        return _cu;
    }

    public void setClassName(String className) {
        this._className = className;
    }

    public String getClassName() {
        return _className;
    }

    public void setMethodName(String methodName) {
        this._methodName = methodName;
    }

    public String getMethodName()
    {
        return _methodName;
    }

    public void setCMFAnalyzer(ClassMethodFinder cmf)
    {
        this._cmf = cmf;
    }
    public ClassMethodFinder getCMFAnalyzer() {
        return _cmf;
    }

    public void setMethodDataFlowAnalyzer(MethodDataFlowAnalyzer mdfa)
    {
        this._mdfa = mdfa;
    }
    public MethodDataFlowAnalyzer getMethodDataFlowAnalyzer() {
        return this._mdfa;
    }
}
