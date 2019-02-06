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

import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

/**
 * The root of the graph were start is made to
 * generate adaptive instructions for refactoring procedures
 * based on specific context of code structure
 */
@XmlRootElement(name="AIT")
@XmlType(propOrder = {"refactorMechanic", "description", "allInstructions"})
public class AdaptiveInstructionGraph {

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
    public EnumSet<CodeContext.CodeContextEnum> allUniqueCodeContextInGraph()
    {
        for(Instruction i : allInstructions) {
            for(ContextDecision cd : i.getDecisions()) {
                if(!contextSet.contains(cd.getContextType())) contextSet.add(cd.getContextType());
            }
        }

        return contextSet;
    }

    /**
     * Returns the set of enum that identifies unique context decisions.
     * It has filtered out always_true, which is a placeholder for those actions that always should take place
     * @return
     */
    public EnumSet<CodeContext.CodeContextEnum> allSpecializedCodeContextInGraph()
    {
        EnumSet<CodeContext.CodeContextEnum> set = allUniqueCodeContextInGraph();
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

    public EnumSet<CodeContext.CodeContextEnum> getSetOfRiskContext() {
        EnumSet<CodeContext.CodeContextEnum> contextWithRisk = EnumSet.noneOf(CodeContext.CodeContextEnum.class);

        for(Instruction i : allInstructions) {
            for(ContextDecision cd : i.getDecisions()) {
                if(!cd.getRiskDescription().isEmpty())
                {
                    contextWithRisk.add(cd.getContextType());
                }
            }
        }
        return contextWithRisk;
    }

    public String getRiskDescription(CodeContext.CodeContextEnum riskContext) {

        String riskDescription = "";

        for(Instruction i : allInstructions) {
            for(ContextDecision cd : i.getDecisions()) {
                if(riskContext == cd.getContextType())
                    riskDescription = cd.getRiskDescription();
            }
        }

        return riskDescription;
    }
}
