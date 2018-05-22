package analysis.context;

import analysis.MethodAnalyzer.ClassMethodFinder;
import analysis.MethodAnalyzer.MethodDescriber;
import analysis.dataflow.MethodDataFlowAnalyzer;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.MethodDeclaration;

public class ContextConfiguration {

    String _methodName;
    MethodDescriber _method;

    CompilationUnit _cu;
    String _className;

    ClassMethodFinder _cmf;
    MethodDataFlowAnalyzer _mdfa;

    CodeSection _codeSection = new CodeSection(-1, -1);

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

    public void setMethodDescriber(MethodDescriber md) {
        this._method = md;
        this._methodName = md.getName();
    }

    public MethodDescriber getMethodDescriber() {
        return this._method;
    }

    public CodeSection getCodeSection() {
        return _codeSection;
    }

    public void setCodeSection(CodeSection cs) {
         _codeSection = cs;
    }
}
