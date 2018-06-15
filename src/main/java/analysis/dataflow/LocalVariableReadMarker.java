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
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.expr.*;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 *
 * Responsibility of this class is to mark those variables in the provided VariableFlowSet
 * which are read in the code.
 *
 */
public class LocalVariableReadMarker extends MarkVariableFlowList {

    public LocalVariableReadMarker(MethodDeclaration md) {
        super(md);
    }

    public LocalVariableReadMarker(MethodDeclaration md, VariableFlowSet variableFlowSet) {
        super(md, variableFlowSet);
    }

    // Every variable used in an expression will be part of a node NameExpr that is visited.
    @Override
    public void visit(NameExpr sn, Void args)
    {
        _lst.getListOfVariableFlowTables().forEach( flowTable ->
        {
            Optional<Node> parentNode = sn.getParentNode();

            if (parentNode.isPresent()) {

                try {
                    // todo: Code below can  be generalized. e.g. set of strategies. Or a chain of functionality, where new handling can be added
                    // visitor pattern can also be a solution
                    if (parentNode.get() instanceof AssignExpr) {
                        AssignExpr ae = (AssignExpr) parentNode.get();

                        //If specific variable is present in the children nodes of the value part of the assignment expression
                        // then it is read
                        if (varnamePresentInSimpleNameNodeList(ae.getValue().getChildNodesByType(SimpleName.class), flowTable.name)) {
                            MarkFlowTable(flowTable, E_ACTION.read, toLoc(ae.getRange()));
                        }
                    }

                    if (parentNode.get() instanceof BinaryExpr) {
                        BinaryExpr be = (BinaryExpr) parentNode.get();

                        //If specific variable is present in the children nodes of the assignment expression
                        // then it is read
                        if (varnamePresentInSimpleNameNodeList(be.getChildNodesByType(SimpleName.class), flowTable.name)) {
                            MarkFlowTable(flowTable, E_ACTION.read, toLoc(be.getRange()));
                            //}
                        }
                    }

                    if (parentNode.get() instanceof MethodCallExpr)
                    {
                        MethodCallExpr mce = (MethodCallExpr) parentNode.get();

                        mce.getChildNodesByType(SimpleName.class);
                        if (varnamePresentInSimpleNameNodeList(mce.getChildNodesByType(SimpleName.class), flowTable.name)) {
                            MarkFlowTable(flowTable, E_ACTION.read, toLoc(mce.getRange()));
                            //}
                        }
                    }

                    if (parentNode.get() instanceof UnaryExpr)
                    {
                        UnaryExpr ue = (UnaryExpr) parentNode.get();
                        ue.getChildNodesByType(SimpleName.class);

                        if (varnamePresentInSimpleNameNodeList(ue.getChildNodesByType(SimpleName.class), flowTable.name)) {
                            MarkFlowTable(flowTable, E_ACTION.read, toLoc(ue.getRange()));
                            //}
                        }
                    }
                }
                catch(ClassCastException ce)
                {
                    System.out.println(ce.getMessage());
                }
            }
        });
    }

    private boolean varNameIsWrittenTo(AssignExpr ae, VariableFlowTable flowTable) {
        return ae.getTarget().toString().contentEquals(flowTable.name);
    }

    /**
     * Determine if name of variable can be found in a given expression
     *
     * @param childNodes
     * @param name
     * @return true = given name found
     */
    private boolean varnamePresentInNodeList(List<Node> childNodes, String name) {

        boolean found = false;

        List<SimpleName> nodeOfSimpleNames = childNodes.stream().filter(
            node -> node instanceof SimpleName
            ).map(SimpleName.class::cast).collect(Collectors.toList());

        found = nodeOfSimpleNames.stream().filter(
                sn -> sn.asString().contentEquals(name)
        ).findFirst().isPresent();

        return found;
    }

    /**
     * Determine if name of variable can be found in a given expression
     *
     * @param names
     * @param name
     * @return true = given name found
     */
    private boolean varnamePresentInSimpleNameNodeList(List<SimpleName> names, String name) {

        boolean found = false;
        //List<SimpleName> names = childNodes.getChildNodesByType(SimpleName.class);

        found = names.stream().filter(
                varName -> varName.toString().contentEquals(name)).findFirst().isPresent();

        return found;
    }
}
