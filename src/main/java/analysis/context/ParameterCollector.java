package analysis.context;

import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.symbolsolver.javaparsermodel.declarations.JavaParserMethodDeclaration;

import java.util.*;

public class ParameterCollector {

    private Map<String,List<String>> paramList = new HashMap<>();

    private final String V_CLASS_LIST = "#class-list";
    private final String V_METHOD_LIST = "#method-list";

    public  String getMethodListType() {return V_METHOD_LIST;}
    public  String getClassListType() {return V_CLASS_LIST;}

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
}
