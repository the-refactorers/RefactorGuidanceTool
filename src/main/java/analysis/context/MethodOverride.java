package analysis.context;

import ait.CodeContext;
import analysis.MethodAnalyzer.ClassMethodFinder;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.symbolsolver.javaparser.Navigator;
import com.github.javaparser.symbolsolver.javaparsermodel.JavaParserFacade;
import com.github.javaparser.symbolsolver.model.declarations.MethodDeclaration;
import com.github.javaparser.symbolsolver.model.declarations.ReferenceTypeDeclaration;
import com.github.javaparser.symbolsolver.model.typesystem.ReferenceType;

import java.util.*;

public class MethodOverride implements IContextDetector{

    private ClassMethodFinder _analyzer = null;
    private String _methodName = null;

    private Map<String,List<String>> classesFound = new HashMap<>();

    private final String V_CLASS_LIST = "$class-list";

    public MethodOverride() {
    }

    public MethodOverride(ClassMethodFinder cmf, String methodName) {
        this._analyzer = cmf;
        this._methodName = methodName;
    }

    public MethodOverride(ContextConfiguration cc) {
        this._analyzer = cc.getCMFAnalyzer();
        this._methodName = cc.getMethodName();
    }

    @Override
    public boolean detect() throws Exception {

        boolean ovveride_exists = false;

        // Determine all classes/interfaces that are superseeding the class being analyzed
        ReferenceTypeDeclaration rtd = _analyzer.getReferenceTypeDeclarationOfClass();
        List<ReferenceType> rt = rtd.getAllAncestors();

        rt.forEach( ancestor ->
        {
            ReferenceTypeDeclaration rtd_ancestor = ancestor.getTypeDeclaration();

            if (!_analyzer.isIgnoredPackage(rtd_ancestor) && !ancestor.getTypeDeclaration().isInterface()) {
                // when ancestor is not a interface declaration, check if any method in super classes equals name of
                // provided method name
                rtd_ancestor.getDeclaredMethods().forEach(m ->
                {
                        if (m.getName().contentEquals(_methodName)) {
                            //System.out.println("Class " + m.declaringType().getQualifiedName() + " has method " + nameOfMethod);
                            //System.out.println("Full signature = " + m.getSignature());
                            addClassNameToClassList(m.declaringType().getQualifiedName());
                        }
                });
            }

        });

        return !classesFound.isEmpty();
    }

    @Override
    public Map<String,List<String>> getParameterMap() {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public CodeContext.CodeContextEnum getType() {
        return CodeContext.CodeContextEnum.MethodOverride;
    }

    protected void addClassNameToClassList(String className) {
        if(!classesFound.isEmpty())
        {
            List<String> actualList = new ArrayList<>(classesFound.get(V_CLASS_LIST));
            if(!actualList.contains(className)) {
                actualList.add(className);
                classesFound.put(V_CLASS_LIST, actualList);
            }
        }
        else
        {
            classesFound.put(V_CLASS_LIST, Arrays.asList(className));
        }
    }
}
