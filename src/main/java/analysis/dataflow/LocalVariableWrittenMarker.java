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

import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.expr.AssignExpr;
import com.github.javaparser.ast.expr.ConditionalExpr;
import com.github.javaparser.ast.expr.UnaryExpr;
import com.github.javaparser.ast.expr.VariableDeclarationExpr;

/**
 * Responsibility of this class is to mark all the variables which are assigned a new value
 * during the execution of a given methodDeclaration.
 *
 * e.g.
 *
 * int a = 6;       // a is given an initial value
 * a = 9;           // a is given a new value of 9
 * b = c + 4;       // b is given a new value c+4
 *
 */
public class LocalVariableWrittenMarker extends MarkVariableFlowList {

    public LocalVariableWrittenMarker(MethodDeclaration md) {
        super(md);
    }

    public LocalVariableWrittenMarker(MethodDeclaration md, VariableFlowSet vfs) {
        super(md,vfs);
    }

    // Case: int a = 5;
    @Override
    public void visit(VariableDeclarator vd, Void args)
    {
        _lst.getListOfVariableFlowTables().forEach( flowTable ->
        {
            // If a local variable has been declared AND an initializer value is present.
            // The variable is seen as being written to.
            if (flowTable.name.contentEquals(vd.getNameAsString()) &&
                    vd.getInitializer().isPresent())
            {
                MarkFlowTable(flowTable, E_ACTION.write, toLoc(vd.getRange()));
            }
        });
    }

    // Case: a++
    @Override
    public void visit(UnaryExpr ue, Void args)
    {
        // In the local declared list of variables lookup, if the name found is present
        // if this is the case determine position of location and write in the correct section
        _lst.getListOfVariableFlowTables().forEach( flowTable ->
        {
            if (flowTable.name.contentEquals(ue.getExpression().toString()))
            {
                MarkFlowTable(flowTable, E_ACTION.write, toLoc(ue.getRange()));
            }
        });
    }

    // Case: a = c + 8;
    @Override
    public void visit(AssignExpr ae, Void args)
    {
        _lst.getListOfVariableFlowTables().forEach( flowTable ->
        {
            // If a local variable has been declared AND an initializer value is present.
            // The variable is seen as being written to.
           if (flowTable.name.contentEquals(ae.getTarget().toString()))
            {
                MarkFlowTable(flowTable, E_ACTION.write, toLoc(ae.getRange()));
            }
        });
    }
}
