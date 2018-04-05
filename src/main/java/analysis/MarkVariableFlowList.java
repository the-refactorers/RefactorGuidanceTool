package analysis;

import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

import java.util.List;

public class MarkVariableFlowList extends VoidVisitorAdapter<Void>
    implements IVariableFlowInfoMarking
{
    List<VariableFlowTable> _lst = null;

    @Override
    public void setVariableFlowList(List<VariableFlowTable> lst) {
        this._lst = lst;
    }

    @Override
    public List<VariableFlowTable> getVariableFlowList() {
        return this._lst;
    }
}
