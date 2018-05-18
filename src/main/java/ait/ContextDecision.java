package ait;

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
    private CodeContext.CodeContextEnum     contextType;
    private int                             nextInstructionID = AdaptiveInstructionTree.FINAL_NODE;
    private String                          _riskDescription = new String("");

    public ContextDecision(CodeContext.CodeContextEnum decisionCtxt, int resultsIn)
    {
        contextType = decisionCtxt;
        nextInstructionID = resultsIn;
    }

    public ContextDecision(CodeContext.CodeContextEnum decisionCtxt)
    {
        contextType = decisionCtxt;
    }

    public String getRiskDescription(){
        return _riskDescription;
    }

    @XmlAttribute
    public void setContextType(CodeContext.CodeContextEnum contextType) {
        this.contextType = contextType;
    }
    public CodeContext.CodeContextEnum getContextType() {
        return contextType;
    }

    @XmlAttribute
    public void setRiskDescription(String s) {
        this._riskDescription = s;
    }

    @XmlElement(name="NEXT_INSTRUCTION")
    public void setNextInstructionID(int instructionResult) {
        this.nextInstructionID = instructionResult;
    }
    public int getNextInstructionID() {
        return nextInstructionID;
    }
}
