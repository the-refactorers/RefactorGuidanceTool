package analysis.dataflow;

public class VariableFlowTable {

    public String name;

    public VariableFacts before_region = new VariableFacts();
    public VariableFacts within_region  = new VariableFacts();
    public VariableFacts after_region  = new VariableFacts();

    public VariableFlowTable(String varName)
    {
        this.name = varName;
    }

    public boolean allFactsInRegionMarkedFalse() {
        return before_region.areAllFactsFalse()
                && within_region.areAllFactsFalse()
                && after_region.areAllFactsFalse();
    }
}
