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


    public List<String> variablesForInput() {

        List<String> inputVariables = new ArrayList<>();

        variableDataFlowSet.getListOfVariableFlowTables().forEach(
                flowTable -> { if (isVariableNeededInExtractMethod(flowTable))
                        inputVariables.add(flowTable.name);
                }
        );

        return inputVariables;
    }

    private boolean isVariableNeededInExtractMethod(VariableFlowTable flowTable) {
        // When variable is changed in before region and used(read) in within region
        return flowTable.before_region.write && flowTable.within_region.read;
    }
}
