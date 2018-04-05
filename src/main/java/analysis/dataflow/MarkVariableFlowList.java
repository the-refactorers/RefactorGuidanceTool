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
    List<VariableFlowTable> _lst = null;

    MarkVariableFlowList (MethodDeclaration md)
    {
        this._method = md;
    }

    @Override
    public void setVariableFlowList(List<VariableFlowTable> lst) {
        this._lst = lst;
    }

    @Override
    public List<VariableFlowTable> getVariableFlowList() {
        return this._lst;
    }
}
