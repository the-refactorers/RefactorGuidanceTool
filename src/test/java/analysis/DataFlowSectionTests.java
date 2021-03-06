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

import analysis.context.CodeSection;
import analysis.dataflow.MethodDataFlowAnalyzer;
import analysis.dataflow.VariableFlowSet;
import analysis.dataflow.VariableFlowTable;
import com.github.javaparser.ast.body.MethodDeclaration;
import org.junit.Assert;
import org.junit.Test;

/**
 * Some tests to see if markers are placed in the correct section (BEFORE, AFTER, WITHIN)
 */
public class DataFlowSectionTests extends JavaParserTestSetup {

   // Case: Invalid section result in marking all variables in within section
    @Test
    public void WriteWithInvalidSectionsTest()
    {
        extractRegion(-1,-1);
        setupTestClass("ExtractMethodMarkerCases", "WriteMarkers");
        mdfaAnalysis();
        VariableFlowSet dataFlowSet = _mdfaAna.getVariableFlowSet();

        VariableFlowTable varFT = dataFlowSet.getVariableFlowTable("a");
        Assert.assertTrue(varFT.within_region.write);
        varFT = dataFlowSet.getVariableFlowTable("b");
        Assert.assertTrue(varFT.within_region.write);
        varFT = dataFlowSet.getVariableFlowTable("g");
        Assert.assertTrue(varFT.within_region.write);
    }

    @Test
    public void WriteValidSectionsBeforeTest() {

        extractRegion(16,17);
        setupTestClass("ExtractMethodMarkerCases", "WriteMarkers");

        mdfaAnalysis();
        VariableFlowSet dataFlowSet = _mdfaAna.getVariableFlowSet();

        //before
        VariableFlowTable varFT = dataFlowSet.getVariableFlowTable("a");
        Assert.assertTrue(varFT.before_region.write);
    }

    @Test
    public void WriteValidSectionsWithinTest() {
        extractRegion(16,17);
        setupTestClass("ExtractMethodMarkerCases", "WriteMarkers");
       mdfaAnalysis();
        VariableFlowSet dataFlowSet = _mdfaAna.getVariableFlowSet();

        VariableFlowTable varFT = dataFlowSet.getVariableFlowTable("c");
        Assert.assertTrue(varFT.within_region.write);
        varFT = dataFlowSet.getVariableFlowTable("f");
        Assert.assertTrue(varFT.within_region.write);
    }

    @Test
    public void WriteValidSectionsAfterTest() {
        extractRegion(16,17);
        setupTestClass("ExtractMethodMarkerCases", "WriteMarkers");
       mdfaAnalysis();
        VariableFlowSet dataFlowSet = _mdfaAna.getVariableFlowSet();

        //after
        VariableFlowTable varFT = dataFlowSet.getVariableFlowTable("g");
        Assert.assertTrue(varFT.after_region.write);
    }

    @Test
    public void ReadValidSectionsBeforeTest() {
        extractRegion(81,82);
        setupTestClass("ExtractMethodMarkerCases", "multipleReads");
        mdfaAnalysis();
        VariableFlowSet dataFlowSet = _mdfaAna.getVariableFlowSet();

        VariableFlowTable varFT = dataFlowSet.getVariableFlowTable("a");
        Assert.assertTrue(varFT.before_region.read);
        varFT = dataFlowSet.getVariableFlowTable("b");
        Assert.assertFalse(varFT.before_region.read);
        varFT = dataFlowSet.getVariableFlowTable("c");
        Assert.assertFalse(varFT.before_region.read);
    }

    @Test
    public void ReadValidSectionsWithinTest() {
        extractRegion(81,82);
        setupTestClass("ExtractMethodMarkerCases", "multipleReads");
       mdfaAnalysis();
        VariableFlowSet dataFlowSet = _mdfaAna.getVariableFlowSet();

        VariableFlowTable varFT = dataFlowSet.getVariableFlowTable("a");
        Assert.assertTrue(varFT.before_region.read);
    }

    @Test
    public void ReadValidSectionsAfterTest() {
        extractRegion(81,82);
        setupTestClass("ExtractMethodMarkerCases", "multipleReads");
       mdfaAnalysis();
        VariableFlowSet dataFlowSet = _mdfaAna.getVariableFlowSet();

        //after
        VariableFlowTable varFT = dataFlowSet.getVariableFlowTable("c");
        Assert.assertTrue(varFT.after_region.read);
    }

    @Test
    public void ReadWriteMultipleReadExample()
    {
        extractRegion(81,82);
        setupTestClass("ExtractMethodMarkerCases", "multipleReads");
       mdfaAnalysis();
        VariableFlowSet dataFlowSet = _mdfaAna.getVariableFlowSet();

        VariableFlowTable varFTa = dataFlowSet.getVariableFlowTable("a");
        VariableFlowTable varFTb = dataFlowSet.getVariableFlowTable("b");
        VariableFlowTable varFTc = dataFlowSet.getVariableFlowTable("c");

        // Expected outcome for a,b and c when analyzing multipleReads
        Assert.assertTrue(varFTa.before_region.read);
        Assert.assertTrue(varFTa.before_region.write);
        Assert.assertTrue(varFTa.within_region.read);
        Assert.assertFalse(varFTa.within_region.write);
        Assert.assertFalse(varFTa.after_region.read);
        Assert.assertTrue(varFTa.after_region.write);

        Assert.assertFalse(varFTb.before_region.read);
        Assert.assertTrue(varFTb.before_region.write);
        Assert.assertTrue(varFTb.within_region.read);
        Assert.assertFalse(varFTb.within_region.write);
        Assert.assertFalse(varFTb.after_region.read);
        Assert.assertFalse(varFTb.after_region.write);

        Assert.assertFalse(varFTc.before_region.read);
        Assert.assertFalse(varFTc.before_region.write);
        Assert.assertFalse(varFTc.within_region.read);
        Assert.assertTrue(varFTc.within_region.write);
        Assert.assertTrue(varFTc.after_region.read);
        Assert.assertFalse(varFTc.after_region.write);
    }

}
