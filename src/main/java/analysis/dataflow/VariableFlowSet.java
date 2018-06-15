
/**
 *  This file is part of RefactorGuidance project. Which explores possibilities to generate context based
 *  instructions on how to refactor a piece of Java code. This applied in an education setting (bachelor SE students)
 *
 *      Copyright (C) 2018, Patrick de Beer, p.debeer@fontys.nl
 *
 *          This program is free software: you can redistribute it and/or modify
 *          it under the terms of the GNU General Public License as published by
 *          the Free Software Foundation, either version 3 of the License, or
 *          (at your option) any later version.
 *
 *          This program is distributed in the hope that it will be useful,
 *          but WITHOUT ANY WARRANTY; without even the implied warranty of
 *          MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *          GNU General Public License for more details.
 *
 *          You should have received a copy of the GNU General Public License
 *          along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package analysis.dataflow;

import java.util.ArrayList;
import java.util.List;

public class VariableFlowSet {

    List<VariableFlowTable> dataFlowMethodVariables  = new ArrayList<>();

    private boolean isVariableAlreadyAdded(String varName) {
        boolean variableAlreadyAdded = false;
        for (VariableFlowTable vi : dataFlowMethodVariables) {
            if(vi.name.contentEquals(varName)) {
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
            if(vi.name.contentEquals(varName)) {
                tableForReturn = vi;
                break;
            }
        }

        return tableForReturn;
    }

    public boolean areAllSectionsInTableSetFalse()
    {
        return dataFlowMethodVariables.stream().allMatch(VariableFlowTable::allFactsInRegionMarkedFalse);
    }

    public List<String> getVariablesUsedInWithinSection() {
        List<String> variables = new ArrayList<>();

        dataFlowMethodVariables.forEach(flowTable ->
        {
            if(flowTable.within_region.read || flowTable.within_region.write)
                variables.add(flowTable.name);
        });

        return variables;
    }
}
