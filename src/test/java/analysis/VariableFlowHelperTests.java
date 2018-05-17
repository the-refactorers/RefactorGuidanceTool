package analysis;

import analysis.dataflow.VariableFacts;
import analysis.dataflow.VariableFlowSet;
import analysis.dataflow.VariableFlowTable;
import org.junit.Assert;
import org.junit.Test;
import java.util.Arrays;


public class VariableFlowHelperTests {

    @Test
    public void GivenNewVariableFactsShouldInitializeToFalse()
    {
        VariableFacts facts = new VariableFacts();
        Assert.assertTrue(facts.areAllFactsFalse());
    }

    @Test
    public void GivenNewVariableFlowTableShouldInitializeAllFactsToFalse()
    {
        VariableFlowTable flowTable = new VariableFlowTable("dummyVar");
        Assert.assertTrue(flowTable.allFactsInRegionMarkedFalse());
    }

    @Test
    public void GivenNewFlowSetShouldINitializeToFalse()
    {
        VariableFlowSet flowSet = new VariableFlowSet(Arrays.asList("dummyVarA", "dummyVarB"));
        Assert.assertTrue(flowSet.areAllSectionsInTableSetFalse());
    }
}
