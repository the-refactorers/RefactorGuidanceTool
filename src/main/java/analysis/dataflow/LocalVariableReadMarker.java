package analysis.dataflow;

import com.github.javaparser.ast.Node;
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

            int type = nodeType(parentNode.get());
            String name = parentNode.get().getMetaModel().getTypeName();

            if(name.contains("AssignExpr")) {
                AssignExpr ae = (AssignExpr)parentNode.get();
                if (!ae.getTarget().toString().contains(flowTable.name))
                    flowTable.within_region.read = true;
            }

            if(name.contains("MethodCallExpr")) {
                MethodCallExpr mce = (MethodCallExpr)parentNode.get();

                if(mce.getArgument(0).toString().contains(flowTable.name))
                    flowTable.within_region.read = true;
            }


            // If a local variable has been declared AND an initializer value is present.
            // The variable is seen as being written to.
           // if (flowTable.name.contains(ae.getTarget().toString()))
//            {
//                flowTable.within_region.write = false;
//            }
        });
    }

    public int nodeType(Node n)
    {
        return -1;
    }

    public int nodeType(AssignExpr ae)
    {
        return 1;
    }
}
