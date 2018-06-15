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

import analysis.ICodeAnalyzer;
import analysis.context.CodeSection;
import analysis.context.ContextConfiguration;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import com.github.javaparser.symbolsolver.javaparser.Navigator;
import javassist.compiler.ast.MethodDecl;

import java.util.ArrayList;
import java.util.List;

public class MethodDataFlowAnalyzer implements ICodeAnalyzer {

    private MethodDeclaration _md;
    private VariableFlowSet variableDataFlowSet;
    private List<MarkVariableFlowList> markRunners = new ArrayList<>();
    private boolean analyzed = false;
    private ContextConfiguration _cc = null;

    // To satisfy the condition that when no valid code section is set, analyzers for testing still work.
    public boolean initialize (MethodDeclaration md)
    {
        return initialize(md, new CodeSection(-1,-1));
    }

    public boolean initialize(MethodDeclaration md, CodeSection cs)
    {
        this._md = md;
        addLocalDeclaredVarsToVariableFlowSet();

        markRunners.add(new LocalVariableWrittenMarker(_md, variableDataFlowSet));
        markRunners.add(new LocalVariableReadMarker(_md, variableDataFlowSet));
        markRunners.add(new LocalVariableLiveMarker(_md, variableDataFlowSet));

        setExtractSection(cs.begin(), cs.end());

        analyzed = false;

        return false;
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

    public List<String> getVariablesUsedInExtractSection()
    {
        return variableDataFlowSet.getVariablesUsedInWithinSection();
    }

    private void setExtractSection(int start, int end)
    {
        for(MarkVariableFlowList markRunner : markRunners)
            markRunner.setExtractMethodRegion(start, end);
    }

    public void start()    {
        start(false);
    }

    public void start(boolean forceAnalysis) {

        if (!analyzed || forceAnalysis) {
            for (MarkVariableFlowList markRunner : markRunners)
                markRunner.mark();

            analyzed = true;
        }
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
