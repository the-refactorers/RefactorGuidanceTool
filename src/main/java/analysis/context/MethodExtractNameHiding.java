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
package analysis.context;

import aig.CodeContext;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.symbolsolver.javaparser.Navigator;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MethodExtractNameHiding extends MethodExtract {

    private final String _className;
    private final CompilationUnit _cu;

    public MethodExtractNameHiding(ContextConfiguration cc) {
        super(cc);

        _className = cc.getClassName();
        _cu = cc.getCompilationUnit();
    }

    @Override
    public boolean detect() throws Exception {

        Set<String> hiddenVars = new HashSet<>();

        if (validDataFlowAnalyzer()) {
            _analyzer.start();

            List<String> varsInExtracted = _analyzer.getVariablesUsedInExtractSection();

            hiddenVars = extractedVarsHideLocalClassField(varsInExtracted);
            hiddenVars.forEach(hv -> parameters.addVariableName(hv));
        }

        return !hiddenVars.isEmpty();
    }

    /**
     * Determine if any of the provided vars is also declared as a field in the local class
     *
     * @param varsInExtracted
     * @return
     */
    private Set<String> extractedVarsHideLocalClassField(List<String> varsInExtracted) {

        ClassOrInterfaceDeclaration class4Analysis = Navigator.demandClassOrInterface(this._cu, this._className);
        List<FieldDeclaration> fields = class4Analysis.getFields();

        Set<String> varsNameHiding = new HashSet<>();

        for(FieldDeclaration fdItem : fields)
        {
            for(String varInEx : varsInExtracted)
            {
                for(VariableDeclarator vd : fdItem.getVariables())
                {
                    if (vd.getName().asString().contentEquals(varInEx)) {
                            varsNameHiding.add(varInEx);
                    }
                }
            }
        }

        return varsNameHiding;
    }

    @Override
    public CodeContext.CodeContextEnum getType() {
        return CodeContext.CodeContextEnum.MethodExtractNameHiding;
    }
}