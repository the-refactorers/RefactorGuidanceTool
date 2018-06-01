/**
 Copyright (C) 2018, Patrick de Beer

 This program is free software: you can redistribute it and/or modify
 it under the terms of the GNU General Public License as published by
 the Free Software Foundation, either version 3 of the License, or
 (at your option) any later version.

 This program is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 GNU General Public License for more details.

 You should have received a copy of the GNU General Public License
 along with this program.  If not, see <http://www.gnu.org/licenses/>.
 **/
package analysis.context;

import ait.CodeContext;
import analysis.MethodAnalyzer.ClassMethodFinder;
import com.github.javaparser.symbolsolver.javaparsermodel.declarations.JavaParserMethodDeclaration;
import com.github.javaparser.symbolsolver.model.declarations.MethodDeclaration;
import com.github.javaparser.symbolsolver.model.declarations.ReferenceTypeDeclaration;
import com.github.javaparser.symbolsolver.model.typesystem.ReferenceType;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.*;

/**
 * Determine those methods that have the same name as the given method
 * but differ in parameters or type
 */
public class MethodOverload implements IContextDetector {

    private ClassMethodFinder _analyzer = null;
    private String _methodName = null;
    private List<JavaParserMethodDeclaration>  methodsMatchingInName = new ArrayList<>();
    private ParameterCollector pc = new ParameterCollector();
    private final String V_METHOD_LIST = "#method-list";

    public MethodOverload(ClassMethodFinder cmf, String methodName) {
        this._analyzer = cmf;
        this._methodName = methodName;
    }

    public MethodOverload(ContextConfiguration cc) {
        this._analyzer = cc.getCMFAnalyzer();
        this._methodName = cc.getMethodName();
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
                    if(MethodNameOnlyMatch(m)) {
                        pc.addMethodNameToVariableList(((JavaParserMethodDeclaration)m).getWrappedNode(), m.declaringType().getClassName());
                        methodsMatchingInName.add((JavaParserMethodDeclaration)m);
                    }
                });
            }
        });

        return !methodsMatchingInName.isEmpty();
    }

    private boolean MethodNameOnlyMatch(MethodDeclaration m) {
        return m.getName().contentEquals(_methodName);
    }

    @Override
    public ParameterCollector getParameters() {
        return pc;
    }

    @Override
    public Map<String, List<String>> getParameterMap() {
        return null;
    }

    @Override
    public CodeContext.CodeContextEnum getType() {
        return CodeContext.CodeContextEnum.MethodOverload;
    }
}
