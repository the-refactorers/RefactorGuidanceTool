package analysis.dataflow;

import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.expr.*;

import java.util.Optional;

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

            String name = parentNode.get().getMetaModel().getTypeName();

            // todo: Code below can  be generalized. e.g. set of strategies. Or a chain of functionality, where new handling can be added
            // visitor pattern can also be a solution
            if(name.contains("AssignExpr")) {
                AssignExpr ae = (AssignExpr)parentNode.get();

                // when name of target variable is not the name of the variable in the FlowTable
                // the conclusion we are dealing with the variables that are read in this expression
                if (!ae.getTarget().toString().contains(flowTable.name))
                    MarkFlowTable(flowTable, E_ACTION.read, startLine(ae.getRange()));
            }
            else
            if(name.contains("MethodCallExpr")) {
                MethodCallExpr mce = (MethodCallExpr)parentNode.get();

                NodeList<Expression> allMethodArguments = mce.getArguments();

                // Arguments passed as variables which are present in one of the local declared
                // flowtables, can be concluded that this is a variable where information is read from
                allMethodArguments.forEach(argument ->
                {
                    if(argument.toString().contains(flowTable.name))
                        MarkFlowTable(flowTable, E_ACTION.read, startLine(mce.getRange()));
                });
            }
        });
    }
}
