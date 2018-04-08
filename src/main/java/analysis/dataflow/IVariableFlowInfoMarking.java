package analysis.dataflow;

public interface IVariableFlowInfoMarking {
    void setVariableFlowList(VariableFlowSet lst);
    VariableFlowSet getVariableFlowList();
}
