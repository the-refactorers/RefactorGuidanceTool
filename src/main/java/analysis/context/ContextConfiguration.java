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

import analysis.MethodAnalyzer.ClassMethodFinder;
import analysis.MethodAnalyzer.MethodDescriber;
import analysis.dataflow.MethodDataFlowAnalyzer;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.MethodDeclaration;

/**
 * Class is a container that can be used to access specific analyzers or other type of
 * data in a generic way. All detectors should use this container construction to
 * pass in detector specific data
 */
public class ContextConfiguration {

    private String _methodName;
    private  MethodDescriber _method;

    private CompilationUnit _cu;
    private String _className;

    private ClassMethodFinder _cmf;
    private MethodDataFlowAnalyzer _mdfa;

    private CodeSection _codeSection = new CodeSection(-1, -1);

    public void setCompilationUnit(CompilationUnit cu) {
        _cu = cu;
    }

    public CompilationUnit getCompilationUnit() {
        return _cu;
    }

    public void setClassName(String className) {
        this._className = className;
    }

    public String getClassName() {
        return _className;
    }

    public String getMethodName()
    {
        return _methodName;
    }

    public void setCMFAnalyzer(ClassMethodFinder cmf)
    {
        this._cmf = cmf;
    }
    public ClassMethodFinder getCMFAnalyzer() {
        return _cmf;
    }

    public void setMethodDataFlowAnalyzer(MethodDataFlowAnalyzer mdfa)
    {
        this._mdfa = mdfa;
    }
    public MethodDataFlowAnalyzer getMethodDataFlowAnalyzer() {
        return this._mdfa;
    }

    public void setMethodDescriber(MethodDescriber md) {
        this._method = md;
        this._methodName = md.getName();
    }

    public MethodDescriber getMethodDescriber() {
        return this._method;
    }

    public CodeSection getCodeSection() {
        return _codeSection;
    }

    public void setCodeSection(CodeSection cs) {
         _codeSection = cs;
    }
}
