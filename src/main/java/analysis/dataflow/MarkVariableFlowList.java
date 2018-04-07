package analysis.dataflow;

import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

import java.util.List;

/**
 * Responsibility of this class is to offer base functionality in marking
 * in what way variables are used in a provided AST method definition.
 */
public class MarkVariableFlowList extends VoidVisitorAdapter<Void>
    implements IVariableFlowInfoMarking
{
    private final MethodDeclaration _method;
    VariableFlowSet _lst = null;

    MarkVariableFlowList (MethodDeclaration md)
    {
        this._method = md;
    }

    MarkVariableFlowList (MethodDeclaration md, VariableFlowSet set)
    {

        this._method = md;
        this._lst = set;
    }

    public void mark()
    {
        this.visit(_method, null);
    }

    @Override
    public void setVariableFlowList(VariableFlowSet lst) {
        this._lst = lst;
    }

    @Override
    public VariableFlowSet getVariableFlowList() {
        return this._lst;
    }
}
