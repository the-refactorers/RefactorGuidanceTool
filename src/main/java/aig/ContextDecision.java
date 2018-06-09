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
