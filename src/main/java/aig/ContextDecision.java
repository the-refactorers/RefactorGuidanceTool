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

package aig;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

@XmlRootElement(name="DECISION")
public class ContextDecision {

    @XmlTransient
    Instruction previousInstruction;
    @XmlTransient
    private Instruction                     nextInstruction;
    private CodeContext.CodeContextEnum     _contextType;
    private int                             _nextInstructionID = AdaptiveInstructionGraph.FINAL_NODE;
    private String                          _riskDescription = new String("");

    public ContextDecision(CodeContext.CodeContextEnum decisionCtxt, int resultsIn)
    {
        _contextType = decisionCtxt;
        _nextInstructionID = resultsIn;
    }

    public ContextDecision(ContextDescriber description, int resultsIn)
    {
        this._contextType = description.decisionContext;
        this._riskDescription = description.riskDescription;
        this._nextInstructionID = resultsIn;
    }

    public ContextDecision(CodeContext.CodeContextEnum decisionCtxt)
    {
        this._contextType = decisionCtxt;
    }

    public String getRiskDescription(){
        return _riskDescription;
    }

    @XmlAttribute
    public void setContextType(CodeContext.CodeContextEnum contextType) {
        this._contextType = contextType;
    }
    public CodeContext.CodeContextEnum getContextType() {
        return this._contextType;
    }

    @XmlAttribute
    public void setRiskDescription(String s) {
        this._riskDescription = s;
    }

    @XmlElement(name="NEXT_INSTRUCTION")
    public void setNextInstructionID(int instructionResult) {
        this._nextInstructionID = instructionResult;
    }
    public int getNextInstructionID() {
        return _nextInstructionID;
    }
}
