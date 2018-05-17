package ait;

import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

/**
 * The root of the tree were start is made to
 * generate adaptive instructions for refactoring procedures
 * based on specific context of code structure
 */
@XmlRootElement(name="AIT")
@XmlType(propOrder = {"refactorMechanic", "description", "allInstructions"})
public class AdaptiveInstructionTree {

    public final static int FINAL_NODE = -1;

    @XmlTransient
    EnumSet<CodeContext.CodeContextEnum> contextSet = EnumSet.noneOf(CodeContext.CodeContextEnum.class);

    String refactorMechanic;
    String description;
    List<Instruction> allInstructions = new ArrayList<>();

    @XmlElement(name="MECHANIC")
    public void setRefactorMechanic(String refactorMechanic) {
        this.refactorMechanic = refactorMechanic;
    }
    public String getRefactorMechanic() {
        return refactorMechanic;
    }

    @XmlElement(name = "DESCRIPTION")
    public void setDescription(String description) {
        this.description = description;
    }
    public String getDescription() {
        return description;
    }

    /**
     * Returns the set of all unique code context decision points in the AIT
     * @return
     */
    public EnumSet<CodeContext.CodeContextEnum> allUniqueCodeContextInTree()
    {
        for(Instruction i : allInstructions) {
            for(ContextDecision cd : i.getDecisions()) {
                if(!contextSet.contains(cd.contextType)) contextSet.add(cd.contextType);
            }
        }

        return contextSet;
    }

    /**
     * Returns the set of enum that identifies unique context decisions.
     * It has filtered out always_true, which is a placeholder for those actions that always should take place
     * @return
     */
    public EnumSet<CodeContext.CodeContextEnum> allSpecializedCodeContextInTree()
    {
        EnumSet<CodeContext.CodeContextEnum> set = allUniqueCodeContextInTree();
        set.remove(CodeContext.CodeContextEnum.always_true);

        return set;
    }

    @XmlTransient
    public void setFirstInstruction(Instruction firstInstruction) {
        this.allInstructions.add(0, firstInstruction);
    }

    public Instruction getFirstInstruction() {
        return this.allInstructions.get(0);
    }

    @XmlElement(name = "INSTRUCTION")
    public List<Instruction> getAllInstructions()
    {
        return allInstructions;
    }

    public void setAllInstructions(List<Instruction> instructions)
    {
        this.allInstructions = instructions;
    }

    public void addInstruction(Instruction i) {
        allInstructions.add(i);
    }

    public Instruction findInstruction(int nextInstructionID) {

        Instruction lookedupInstruction = null;

        for (Instruction instruction : this.allInstructions)
        {
            if (instruction.instructionID == nextInstructionID)
            {
                lookedupInstruction = instruction;
                break;
            }
            else if (nextInstructionID == FINAL_NODE)
            {
                lookedupInstruction = new Instruction(-1,"");
                break;
            }
        }

        return lookedupInstruction;
    }
}
