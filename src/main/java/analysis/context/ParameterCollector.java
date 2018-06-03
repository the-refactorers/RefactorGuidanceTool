package analysis.context;

import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.symbolsolver.javaparsermodel.declarations.JavaParserMethodDeclaration;

import java.util.*;

public class ParameterCollector {

    private Map<String,List<String>> paramList = new HashMap<>();

    private final String V_CLASS_LIST = "#class-list";
    private final String V_METHOD_LIST = "#method-list";
    private final String V_CLASS = "#class";
    private final String V_METHOD = "#method";
    private final String V_INTERFACE = "#interface";
    private final String V_ARGUMENTS = "#argument-list";
    private final String V_RESULTS = "#result-list";
    private final String V_VARS = "#variable-list";

    public  String getMethodListType() {return V_METHOD_LIST;}
    public  String getClassListType() {return V_CLASS_LIST;}
    public String  getMethodType() { return V_METHOD;}
    public String  getClassType() { return V_CLASS;}
    public String getInterfaceType() { return V_INTERFACE;}
    public String getArgumentListType() { return  V_ARGUMENTS; }
    public String getResultListType() { return V_RESULTS; }
    public String getVariableListType() { return V_VARS; }

    protected void addClassNameToVariableList(JavaParserMethodDeclaration jpClass) {

        String qualifiedClassName = jpClass.declaringType().getQualifiedName();
        addParameterValue(getClassListType(), qualifiedClassName);
    }

    public void addMethodNameToVariableList(MethodDeclaration item, String className) {
        String methodName =  className + " :: " + item.getType().asString() + " " + item.getSignature().toString();
        addParameterValue(getMethodListType(), methodName);
    }

    private void addParameterValue(String type, String value) {

        if(paramList.containsKey(type))
        {
            List<String> actualList = new ArrayList<>(paramList.get(type));

            if(!actualList.contains(value)) {
                actualList.add(value);
                paramList.put(type, actualList);
            }
        }
        else
        {
            paramList.put(type, Arrays.asList(value));
        }
    }

    public Map<String,List<String>> getCollection() { return paramList; }

    public void addInterfaceName(String interfaceName) {
        addParameterValue(getInterfaceType(), interfaceName);
    }

    public void addSingleMethodName(String methodName)    {
        addParameterValue(getMethodType(), methodName);
    }

    public void addSingleClassName(String className)    {
        addParameterValue(getClassType(), className);
    }

    public void addArgumentName(String argumentName) {addParameterValue(getArgumentListType(), argumentName);}

    public void addResultName(String resultName) { addParameterValue(getResultListType(), resultName); }

    public void addVariableName(String resultName) { addParameterValue(getVariableListType(), resultName); }
}
