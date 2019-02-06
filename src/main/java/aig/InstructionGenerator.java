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

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class InstructionGenerator {

    AdaptiveInstructionGraph _aig = null;
    EnumSet<CodeContext.CodeContextEnum> contextSet = null;
    private Map<String, List<String>> parameterMap = null;

    public InstructionGenerator(AdaptiveInstructionGraph graph)   {
        _aig = graph;
    }

    public void provideContext(EnumSet<CodeContext.CodeContextEnum> contextSet) {
        this.contextSet = contextSet;
    }


    /***
     * Define the values of the parameters that are parsed in the template instructions
     * @param parameters Maps variables ($<var>) in instruction to a concrete value
     */
    public void setParameterMap(Map<String, List<String>> parameters) { this.parameterMap = parameters; }


    /**
     * Default behavior when no parameters are provided
     * @return
     */
    public List<String> generateInstruction()
    {
        boolean riskOverview = false;
        return generateInstruction(riskOverview);
    }

    /**
     * Based on code context information encoded in contextSet
     * and the variable Name information in the parameter map
     * Instruction is generated based on the given instruction graph
     *
     * @return  List of strings describing refactor steps based, empty list when invalid no context set, parameter map or missing instruction graph
     */
    public List<String> generateInstruction(boolean riskOverview)
    {
        String errStr = "ERROR: unknown";
        boolean inputErr = false;

        List<String> generatedInstructionList = new ArrayList<>();
        List<String> parsedValuesInstructionList = new ArrayList<>();

        if(_aig == null) {errStr = "ERROR: AIT is null"; inputErr = true;}
        else
        if(contextSet == null || contextSet.isEmpty()) {errStr = "ERROR: Context-set is empty or null"; inputErr = true;}
        else
        if(parameterMap == null) { errStr = "ERROR: parameterMap is null"; inputErr = true; }

        // Should always be present, otherwise the algorithm is never stopping
        if(contextSet != null) contextSet.add(CodeContext.CodeContextEnum.always_true);

        if (!inputErr)
        {
            if(riskOverview) {
                generatedInstructionList.add("Identified RISKS in your code that need special attention when performing " + _aig.getRefactorMechanic() + " on method #method \n");
                SummarizeRisks(generatedInstructionList, contextSet);
            }

            // built up the instruction list, based on the code context set
            Instruction _instr = _aig.getFirstInstruction();

            generatedInstructionList.add(_instr.getInstructionDescription());

            String graphTrace = new String("\n");

            while (!_instr.endNode()) {

                int decisionPaths = 0;
                Instruction prevInstr = _instr;

                for (ContextDecision decision : _instr.getDecisions()) {

                    // Check if context for specific decision exists in code
                    if (contextSet.contains(decision.getContextType())) {
                        _instr = _aig.findInstruction(decision.getNextInstructionID());

                        if(!_instr.getInstructionDescription().isEmpty()) {
                            generatedInstructionList.add(_instr.getInstructionDescription());
                        }
                            decisionPaths++; // only one decision should be valid
                            graphTrace += " > " + decision.getNextInstructionID();
                    }

                }

                if(decisionPaths > 1) {System.out.println("\nINVALID GRAPH, unambiguous decision at " + prevInstr.instructionID + "\n");}
            }

            System.out.println(graphTrace);

            // Fill in the used variables in the generated instructions, based on the parameter map
            //        1. Are there variables present
            //        2. Are all variables found present in map? (no, error)
            //        3. replace all variables with their value
            for (String lineToParse : generatedInstructionList) {

                String parsedInstructionLine = lineToParse;

                for (Map.Entry<String, List<String>> entry : parameterMap.entrySet()) {
                    if (exactMatchInString(parsedInstructionLine, entry.getKey())) {
                        parsedInstructionLine = parsedInstructionLine.replace(entry.getKey(), String.join(", ", entry.getValue()));
                    }
                }

                parsedValuesInstructionList.add(parsedInstructionLine);
            }

            return parsedValuesInstructionList;
        }
        else
        {
            ArrayList<String> resultString = new ArrayList<>();
            resultString.add(errStr);
            return resultString;
        }
    }

    private static boolean exactMatchInString(String source, String subItem){
        String pattern = "\\s" + subItem;
        Pattern p=Pattern.compile(pattern);
        Matcher m=p.matcher(source);
        return m.find();
    }


    private void SummarizeRisks(List<String> generatedInstructionList,
                                EnumSet<CodeContext.CodeContextEnum> contextSet) {

        if (contextSet.isEmpty())
        {
            generatedInstructionList.add("<none>\n");
        }
        else
        {
            // For all items in the context stream that has a none empty risk description,
            // add those risk descriptions to the instruction list
            contextSet.stream().filter(context -> !_aig.getRiskDescription(context).isEmpty()).forEach(
                    ctxtWithRisk -> generatedInstructionList.add(_aig.getRiskDescription(ctxtWithRisk))
            );
        }
    }

    public void setContext(EnumSet<CodeContext.CodeContextEnum> context) {
        this.contextSet = context;
    }
}
