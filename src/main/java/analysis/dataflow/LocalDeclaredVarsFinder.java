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

import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.Parameter;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.nodeTypes.NodeWithSimpleName;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Responsibility of this class is to determine which variables are declared local in
 * a provided MethodDeclaration object. declarations are those items in the AST
 * which have as property : VariableDeclarator
 */
public class LocalDeclaredVarsFinder extends VoidVisitorAdapter<Void> {

    private final MethodDeclaration _method;
    private List<String> _localVars = new ArrayList<>();
    
    public LocalDeclaredVarsFinder(MethodDeclaration md)
    {
        this._method = md;
    }

    // Kick-off the visitor pattern to start detecting declaration cases
    public void find()
    {
        this.visit(_method, null);
    }

    private void addToLocalVars(NodeWithSimpleName<Node> n)
    {
        this._localVars.add(n.getName().toString());
    }

    @Override
    public void visit(VariableDeclarator vd, Void arg) {
        super.visit(vd, arg);
        addToLocalVars((NodeWithSimpleName)vd);
    }

    @Override
    public void visit(Parameter p, Void arg) {
        super.visit(p, arg);
        addToLocalVars((NodeWithSimpleName)p);
    }
    
    public List<String> getLocalVars()
    {
        return this._localVars;
    }
}
