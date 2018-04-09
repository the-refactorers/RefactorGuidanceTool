package analysis.dataflow;

import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

import java.util.ArrayList;
import java.util.List;

public class MethodDataFlowAnalyzer {

    private MethodDeclaration _md;
    private VariableFlowSet variableDataFlowSet;
    private List<MarkVariableFlowList> markRunners = new ArrayList<>();

    public MethodDataFlowAnalyzer(MethodDeclaration md)
    {
        this._md = md;
        addLocalDeclaredVarsToVariableFlowSet();

        markRunners.add(new LocalVariableWrittenMarker(_md, variableDataFlowSet));
        markRunners.add(new LocalVariableReadMarker(_md, variableDataFlowSet));
    }

    private void addLocalDeclaredVarsToVariableFlowSet() {
        LocalDeclaredVarsFinder localVars = new LocalDeclaredVarsFinder(this._md);
        localVars.find();

        variableDataFlowSet = new VariableFlowSet(localVars.getLocalVars());
    }

    public VariableFlowSet getVariableFlowSet()
    {
        return variableDataFlowSet;
    }

    public void setExtractSection(int start, int end)
    {
        for(MarkVariableFlowList markRunner : markRunners)
            markRunner.setExtractMethodRegion(start, end);
    }

    public void start() {

        for(MarkVariableFlowList markRunner : markRunners)
            markRunner.mark();

    }
}
