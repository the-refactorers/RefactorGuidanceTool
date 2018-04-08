package ait;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement (name = "INSTRUCTION")
@XmlType(propOrder = {"instructionDescription", "decisions"})
public class Instruction {

    @XmlAttribute
    int     instructionID = -1;
    String  instructionDescription;

    List<ContextDecision> decisions = new ArrayList<ContextDecision>();

    public Instruction() {
    }

    public Instruction(int i, String s) {
        this.instructionDescription = s;
        this.instructionID = i;
    }

    @XmlElement(name = "TEXT")
    public void setInstructionDescription(String description) {
        this.instructionDescription = description;
    }
    public String getInstructionDescription() {
        return instructionDescription;
    }

    @XmlElement(name= "DECISION")
    public void setDecisions(List<ContextDecision> decisions) {
        this.decisions = decisions;
    }
    public List<ContextDecision> getDecisions()
    {
        return decisions;
    }

    /**
     * Adds one or more decisions that can be taken after the specific instruction
     * @param decision defines a code context based decision that leads to a new instruction
     */
    public void addDecision(ContextDecision decision)
    {
        if (decisions == null)
        {
            decisions = new ArrayList<ContextDecision>();
        }

        decisions.add(decision);
    }

    public boolean endNode() {
        return decisions.isEmpty();
    }
}
