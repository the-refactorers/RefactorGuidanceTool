package analysis.dataflow;

import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.expr.*;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

import java.util.List;
import java.util.Optional;

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

                        // When evaluating the variable in the flowtable, it is read if:
                        // 1. It is not part of the target
                        // 2. It is present as a child in the value.childnodes
                        if (!ae.getTarget().toString().contentEquals(flowTable.name) &&
                                varnamePresentInExpression(ae.getValue(),flowTable.name)) {
                            MarkFlowTable(flowTable, E_ACTION.read, startLine(ae.getRange()));
                        }
                    } else if (parentNode.get() instanceof MethodCallExpr) {
                        MethodCallExpr mce = (MethodCallExpr) parentNode.get();

                        NodeList<Expression> allMethodArguments = mce.getArguments();

                        // Arguments passed as variables which are present in one of the local declared
                        // flowtables, can be concluded that this is a variable where information is read from
                        allMethodArguments.forEach(argument ->
                        {
                            if (argument.toString().contentEquals(flowTable.name))
                                MarkFlowTable(flowTable, E_ACTION.read, startLine(mce.getRange()));
                        });
                    }
                }
                catch(ClassCastException ce)
                {
                    System.out.println(ce.getMessage());
                }
            }
        });
    }

    /**
     * Determine if name of variable can be found in a given expression
     *
     * @param childNodes
     * @param name
     * @return true = given name found
     */
    private boolean varnamePresentInExpression(Expression childNodes, String name) {

        boolean found = false;
        List<SimpleName> names = childNodes.getChildNodesByType(SimpleName.class);

        found = names.stream().filter(
            varName -> varName.toString().contentEquals(name)).findFirst().isPresent();

        return found;
    }
}
