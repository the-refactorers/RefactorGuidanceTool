package analysis.dataflow;

import com.github.javaparser.ast.body.MethodDeclaration;

public class MethodDataFlowAnalyzer {

    private MethodDeclaration _md;
    private VariableFlowSet variableDataFlowSet;

    public MethodDataFlowAnalyzer(MethodDeclaration md)
    {
        this._md = md;
        LocalDeclaredVarsFinder localVars = new LocalDeclaredVarsFinder(this._md);

        localVars.find();
        variableDataFlowSet = new VariableFlowSet(localVars.getLocalVars());
    }

    public VariableFlowSet getVariableFlowSet()
    {
        return variableDataFlowSet;
    }

    public void start() {

        LocalVariableWrittenMarker wMark = new LocalVariableWrittenMarker(_md, variableDataFlowSet);
        wMark.mark();

    }
}
