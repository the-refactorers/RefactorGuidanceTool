package analysis.dataflow;

import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.expr.AssignExpr;
import com.github.javaparser.ast.expr.ConditionalExpr;
import com.github.javaparser.ast.expr.UnaryExpr;
import com.github.javaparser.ast.expr.VariableDeclarationExpr;

public class LocalVariableWrittenMarker extends MarkVariableFlowList {

    LocalVariableWrittenMarker(MethodDeclaration md) {
        super(md);
    }

    @Override
    public void visit(VariableDeclarationExpr vde, Void args)
    {

    }

    @Override
    public void visit(UnaryExpr ue, Void args)
    {

    }

    @Override
    public void visit(AssignExpr ae, Void args)
    {

    }

    @Override
    public void visit(ConditionalExpr te, Void args)
    {

    }
}
