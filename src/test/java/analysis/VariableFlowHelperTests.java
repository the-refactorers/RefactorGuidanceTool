/**
 *  This file is part of RefactorGuidance project. Which explores possibilities to generate context based
 *  instructions on how to refactor a piece of Java code. This applied in an education setting (bachelor SE students)
 *
 *      Copyright (C) 2018, Patrick de Beer, p.debeer@fontys.nl
 *
 *          This program is free software: you can redistribute it and/or modify
 *          it under the terms of the GNU General Public License as published by
 *          the Free Software Foundation, either version 3 of the License, or
 *          (at your option) any later version.
 *
 *          This program is distributed in the hope that it will be useful,
 *          but WITHOUT ANY WARRANTY; without even the implied warranty of
 *          MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *          GNU General Public License for more details.
 *
 *          You should have received a copy of the GNU General Public License
 *          along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
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
