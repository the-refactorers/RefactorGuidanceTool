package analysis.dataflow;

import java.util.ArrayList;
import java.util.List;

public class VariableFlowSet {

    List<VariableFlowTable> dataFlowMethodVariables  = new ArrayList<VariableFlowTable>();

    private boolean isVariableAlreadyAdded(String varName) {
        boolean variableAlreadyAdded = false;
        for (VariableFlowTable vi : dataFlowMethodVariables) {
            if(vi.name.contains(varName)) {
                variableAlreadyAdded = true;
                break;
            }
        }
        return variableAlreadyAdded;
    }

    private void addNewVariablesToVariableFLowList(String varName) {
        boolean variableAlreadyAdded = isVariableAlreadyAdded(varName);

        if (!variableAlreadyAdded)
        {
            dataFlowMethodVariables.add(new VariableFlowTable(varName));
        }
    }

    public VariableFlowSet(List<String> variableNames)
    {
        for(String varName : variableNames)
            addNewVariablesToVariableFLowList(varName);
    }

    public List<VariableFlowTable> getListOfVariableFlowTables()
    {
        return dataFlowMethodVariables;
    }

    public VariableFlowTable getVariableFlowTable(String varName) {

        boolean variableFound = false;
        VariableFlowTable tableForReturn = new VariableFlowTable("");

        for (VariableFlowTable vi : dataFlowMethodVariables) {
            if(vi.name.contains(varName)) {
                tableForReturn = vi;
                break;
            }
        }

        return tableForReturn;
    }
}
