package analysis.dataflow;

import java.util.List;

public interface IVariableFlowInfoMarking {
    void setVariableFlowList(VariableFlowSet lst);
    VariableFlowSet getVariableFlowList();
}
