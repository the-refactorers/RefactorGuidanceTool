package analysis;

import analysis.dataflow.VariableFacts;
import analysis.dataflow.VariableFlowTable;
import org.junit.Assert;
import org.junit.Test;

public class VariableFlowHelperTests {

    @Test
    public void GivenNewVariableFactsShouldInitializeToFalse()
    {
        VariableFacts facts = new VariableFacts();
        Assert.assertTrue(facts.allFalse());
    }

    @Test
    public void GivenNewVariableFlowTableShouldInitializeAllFactsToFalse()
    {
        VariableFlowTable flowTable = new VariableFlowTable("dummyVar");
        Assert.assertTrue(flowTable.isAllFalse());
    }
}
