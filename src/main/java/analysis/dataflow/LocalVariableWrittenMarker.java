package analysis.dataflow;

import com.github.javaparser.ast.body.MethodDeclaration;
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
    public void visit(VariableDeclarationExpr vde, Void args)
    {

    }

    // Case: a++
    @Override
    public void visit(UnaryExpr ue, Void args)
    {
        _lst.getListOfVariableFlowTables().forEach( flowTable ->
        {
            if (flowTable.name.contains("c"))
            {
                flowTable.before_region.write = true;
            }
        });
    }

    // Case: a = c + 8;
    @Override
    public void visit(AssignExpr ae, Void args)
    {

    }

    // Case: max = (a>b) ? a : b
    @Override
    public void visit(ConditionalExpr te, Void args)
    {

    }
}
