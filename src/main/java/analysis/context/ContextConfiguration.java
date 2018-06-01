package analysis.context;

import analysis.MethodAnalyzer.ClassMethodFinder;
import analysis.MethodAnalyzer.MethodDescriber;
import analysis.dataflow.MethodDataFlowAnalyzer;
import com.github.javaparser.ast.CompilationUnit;

/**
 * Class is a container that can be used to access specific analyzers or other type of
 * data in a generic way. All detectors should use this container construction to
 * pass in detector specific data
 */
public class ContextConfiguration {

    private String _methodName;
    private  MethodDescriber _method;

    private CompilationUnit _cu;
    private String _className;

    private ClassMethodFinder _cmf;
    private MethodDataFlowAnalyzer _mdfa;

    private CodeSection _codeSection = new CodeSection(-1, -1);

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
