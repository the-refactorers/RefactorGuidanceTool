package ait;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class InstructionGenerator {

    AdaptiveInstructionTree aitTree = null;
    EnumSet<CodeContext.CodeContextEnum> contextSet = null;
    private Map<String, List<String>> parameterMap = null;

    public InstructionGenerator(AdaptiveInstructionTree tree)   {
        aitTree = tree;
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
     * Instruction is generated based on the given instruction tree
     *
     * @return  List of strings describing refactor steps based, empty list when invalid no context set, parameter map or missing instruction tree
     */
    public List<String> generateInstruction(boolean riskOverview)
    {
        String errStr = "ERROR: unknown";
        boolean inputErr = false;

        List<String> generatedInstructionList = new ArrayList<>();
        List<String> parsedValuesInstructionList = new ArrayList<>();

        if(aitTree == null) {errStr = "ERROR: AIT is null"; inputErr = true;}
        else
        if(contextSet == null || contextSet.isEmpty()) {errStr = "ERROR: Context-set is empty or null"; inputErr = true;}
        else
        if(parameterMap == null) { errStr = "ERROR: parameterMap is null"; inputErr = true; }

        // Should always be present, otherwise the algorithm is never stopping
        if(contextSet != null) contextSet.add(CodeContext.CodeContextEnum.always_true);

        if (!inputErr)
        {
            if(riskOverview) {
                generatedInstructionList.add("Identified RISKS in your code that need special attention when performing " + aitTree.getRefactorMechanic());
                SummarizeRisks(generatedInstructionList, contextSet);
            }

            // built up the instruction list, based on the code context set
            Instruction _instr = aitTree.getFirstInstruction();

            generatedInstructionList.add(_instr.getInstructionDescription());

            while (!_instr.endNode()) {
                for (ContextDecision decision : _instr.getDecisions()) {
                    // Check if context for specific decision exists in code
                    if (contextSet.contains(decision.getContextType())) {
                        _instr = aitTree.findInstruction(decision.getNextInstructionID());
                        generatedInstructionList.add(_instr.getInstructionDescription());
                    }
                }
            }

            // Fill in the used variables in the generated instructions, based on the parameter map
            //        1. Are there variables present
            //        2. Are all variables found present in map? (no, error)
            //        3. replace all variables with their value
            for (String lineToParse : generatedInstructionList) {

                String parsedInstructionLine = lineToParse;

                for (Map.Entry<String, List<String>> entry : parameterMap.entrySet()) {
                    if (exactMatchInString(parsedInstructionLine, entry.getKey())) {
                        parsedInstructionLine = parsedInstructionLine.replace(entry.getKey(), String.join("", entry.getValue()));
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
        String pattern = "\\b" + subItem + "\\b";
        Pattern p=Pattern.compile(pattern);
        Matcher m=p.matcher(source);
        return m.find();
    }


    private void SummarizeRisks(List<String> generatedInstructionList,
                                EnumSet<CodeContext.CodeContextEnum> contextSet) {

        EnumSet<CodeContext.CodeContextEnum> riskContext = aitTree.getSetOfRiskContext();

        if (riskContext.isEmpty())
        {
            generatedInstructionList.add("<none>\n");
        }
    }

    public void setContext(EnumSet<CodeContext.CodeContextEnum> context) {
        this.contextSet = context;
    }
}
