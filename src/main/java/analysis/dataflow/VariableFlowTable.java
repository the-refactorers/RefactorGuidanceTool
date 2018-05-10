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

    public boolean isAllFalse() {
        return before_region.allFalse() && within_region.allFalse() && after_region.allFalse();
    }
}
