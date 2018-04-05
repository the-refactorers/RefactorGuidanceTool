package analysis;

public class VariableFlowTable {

    public String name;

    public VariableFacts before_region = new VariableFacts();
    public VariableFacts within_region  = new VariableFacts();
    public VariableFacts after_region  = new VariableFacts();

    public VariableFlowTable(String varName)
    {
        this.name = varName;
    }
}
