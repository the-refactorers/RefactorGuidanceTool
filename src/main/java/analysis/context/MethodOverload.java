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
import analysis.MethodAnalyzer.ClassMethodFinder;
import analysis.MethodAnalyzer.MethodDescriber;
import com.github.javaparser.symbolsolver.javaparsermodel.declarations.JavaParserMethodDeclaration;
import com.github.javaparser.symbolsolver.model.declarations.ReferenceTypeDeclaration;
import com.github.javaparser.symbolsolver.model.typesystem.ReferenceType;


import java.util.*;

/**
 * Determine those methods that have the same name as the given method
 * but differ in parameters or type
 */
public class MethodOverload extends ContextDetector {

    private ClassMethodFinder _analyzer = null;
    private MethodDescriber _md = null;
    private List<JavaParserMethodDeclaration>  methodsMatchingInName = new ArrayList<>();

    public MethodOverload(ContextConfiguration cc) {
        super(cc);

        this._analyzer = cc.getCMFAnalyzer();
        this._md = cc.getMethodDescriber();
    }

    @Override
    public boolean detect() throws Exception {

        // Determine all classes/interfaces that are superseeding the class being analyzed
        ReferenceTypeDeclaration rtd = _analyzer.getReferenceTypeDeclarationOfClass();
        List<ReferenceType> _rt = rtd.getAllAncestors();

        _rt.forEach( ancestor ->
        {
            ReferenceTypeDeclaration rtd_ancestor = ancestor.getTypeDeclaration();

            if (!_analyzer.isIgnoredPackage(rtd_ancestor) && !ancestor.getTypeDeclaration().isInterface()) {
                // when ancestor is not a interface declaration, check if any method in super classes equals name of
                // provided method name
                rtd_ancestor.getDeclaredMethods().forEach(m ->
                {
                    if(_analyzer.MethodNameOnlyMatch(m, _md) &&
                            !_analyzer.fullMethodSignatureMatch(m, _md)) {
                        parameters.addMethodNameToVariableList(((JavaParserMethodDeclaration)m).getWrappedNode(), m.declaringType().getClassName());
                        methodsMatchingInName.add((JavaParserMethodDeclaration)m);
                    }
                });
            }
        });

        return !methodsMatchingInName.isEmpty();
    }

    @Override
    public CodeContext.CodeContextEnum getType() {
        return CodeContext.CodeContextEnum.MethodOverload;
    }
}
