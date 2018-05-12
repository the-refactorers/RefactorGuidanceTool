/**
 * Dataflow package has been inspired by:
 *  Improving method Extraction: A Novel approach to Data Flow Analysis using Boolean flags & Expressions
 */
package analysis.dataflow;

import analysis.ICodeAnalyzer;
import analysis.context.CodeSection;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import com.github.javaparser.symbolsolver.javaparser.Navigator;

import java.util.ArrayList;
import java.util.List;

public class MethodDataFlowAnalyzer implements ICodeAnalyzer {

    private MethodDeclaration _md;
    private VariableFlowSet variableDataFlowSet;
    private List<MarkVariableFlowList> markRunners = new ArrayList<>();

    public MethodDataFlowAnalyzer(MethodDeclaration md)
    {
        this._md = md;
        addLocalDeclaredVarsToVariableFlowSet();

        markRunners.add(new LocalVariableWrittenMarker(_md, variableDataFlowSet));
        markRunners.add(new LocalVariableReadMarker(_md, variableDataFlowSet));
        markRunners.add(new LocalVariableLiveMarker(_md, variableDataFlowSet));
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
                flowTable -> { if (isVariableReadInExtractMethod(flowTable))
                        inputVariables.add(flowTable.name);
                }
        );

        return inputVariables;
    }

    public List<String> variablesForOutput() {
        List<String> inputVariables = new ArrayList<>();

        variableDataFlowSet.getListOfVariableFlowTables().forEach(
                flowTable -> { if (isVariableChangedInExtractMethod(flowTable))
                    inputVariables.add(flowTable.name);
                }
        );

        return inputVariables;
    }

    private boolean isVariableChangedInExtractMethod(VariableFlowTable flowTable) {
        return flowTable.within_region.write && flowTable.after_region.live;
    }

    private boolean isVariableReadInExtractMethod(VariableFlowTable flowTable) {
        // When variable is changed in before region and used(read) in within region
        return flowTable.within_region.live;
    }
}
