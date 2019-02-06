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
import com.github.javaparser.ast.body.MethodDeclaration;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

/**
 * Some tests to validate if conclusions drawn by the dataflow algorithm are correct
 * about necessary input and output variables when extracting a method from a piece of code within
 * another method
 */
public class DataFlowConclusionTests extends JavaParserTestSetup {

    @Test
    public void ExtractCodeWithoutDependenciesNoInputNeeded()
    {
        extractRegion(7, 10);
        setupTestClass("ExtractMethodCases", "ExtractionWithoutDependencies");

        mdfaAnalysis();

        List<String> vfi = _mdfaAna.variablesForInput();
        Assert.assertEquals(0, vfi.size());
    }

    @Test
    public void ExtractCodeWith1ParamInput()
    {
        extractRegion(19,22);
        setupTestClass("ExtractMethodCases", "ExtractionWith1Input");
        mdfaAnalysis();

        List<String> vfi = _mdfaAna.variablesForInput();
        Assert.assertEquals(1, vfi.size());
    }

    @Test
    public void ExtractCodeWith2ParamInput()
    {
        extractRegion(32,35);
        setupTestClass("ExtractMethodCases", "ExtractionWith2Input");
        mdfaAnalysis();

        List<String> vfi = _mdfaAna.variablesForInput();
        Assert.assertEquals(2, vfi.size());
    }

    @Test
    public void ExtractCodeWith1ParamOutput()
    {
        extractRegion(43,46);
        setupTestClass("ExtractMethodCases", "ExtractionWith1Output");
        mdfaAnalysis();

        List<String> vfi = _mdfaAna.variablesForOutput();
        Assert.assertEquals(1, vfi.size());
    }

    @Test
    public void ExtractCodeWith1ParamOutputAndUseOfParameterAfterThatWasOnlyRead()
    {
        extractRegion(55,58);
        setupTestClass("ExtractMethodCases", "ExtractWith1OutputButVariableUsedAfter");
        mdfaAnalysis();

        List<String> vfi = _mdfaAna.variablesForOutput();
        Assert.assertEquals(1, vfi.size());
    }

    @Test
    public void ExtractCodeWith2Output()
    {
        extractRegion(68,72);
        setupTestClass("ExtractMethodCases", "ExtractWith2Output");
        mdfaAnalysis();

        List<String> vfi = _mdfaAna.variablesForOutput();
        Assert.assertEquals(2, vfi.size());
    }

    @Test
    public void ExtractCodeWhereBeforeVariableIsReadAfterWrittenInWithin()
    {
        extractRegion(82,85);
        setupTestClass("ExtractMethodCases", "ExtractWhereBeforeVariableIsReadAfterWrittenInWithin");
        mdfaAnalysis();

        // Because the variable is assigned, before a read. It is not
        // needed to pass on to extract method
        List<String> vfi = _mdfaAna.variablesForInput();
        Assert.assertEquals(0, vfi.size());
    }

    @Test
    public void retrieveVariablesUsedInExtractedSection()
    {
        extractRegion(126,129);
        setupTestClass("ExtractMethodCases", "ExtractionWith2Input1NoneRelated");
        mdfaAnalysis();

        // Because the variable is assigned, before a read. It is not
        // needed to pass on to extract method
        List<String> varsInExtract = _mdfaAna.getVariablesUsedInExtractSection();
        Assert.assertEquals(2, varsInExtract.size());
        Assert.assertTrue(varsInExtract.contains("a"));
        Assert.assertTrue(varsInExtract.contains("_hiddenName"));
    }

    @Test
    public void failingCase1()
    {
        extractRegion(137,138);
        setupTestClass("ExtractMethodCases", "ExtractMe");
        mdfaAnalysis();

        // Because the variable is assigned, before a read. It is not
        // needed to pass on to extract method
        List<String> varsInExtract = _mdfaAna.getVariablesUsedInExtractSection();
        Assert.assertEquals(2, varsInExtract.size());
    }
}
