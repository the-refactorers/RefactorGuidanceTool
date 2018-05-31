package analysis.context;

import ait.CodeContext;
import analysis.MethodAnalyzer.ClassMethodFinder;
import analysis.MethodAnalyzer.MethodDescriber;
import com.github.javaparser.symbolsolver.javaparsermodel.declarations.JavaParserMethodDeclaration;
import com.github.javaparser.symbolsolver.model.declarations.MethodDeclaration;
import com.github.javaparser.symbolsolver.model.declarations.ReferenceTypeDeclaration;
import com.github.javaparser.symbolsolver.model.typesystem.ReferenceType;

import java.util.*;

/**
 * Determines if given method is an override of any parent class.
 * It is not taken into account an interface. This is done bu MethodInterfaceDeclaration
 */
public class MethodOverride implements IContextDetector{

    private String _className = null;
    protected ClassMethodFinder _analyzer = null;
    protected MethodDescriber _method = null;

    protected List<ReferenceType> _rt = null;

    protected Map<String,List<String>> classesFound = new HashMap<>();
    private final String V_CLASS_LIST = "#class-list";

    protected List<JavaParserMethodDeclaration>  listOfNodesForOverrideMethods = new ArrayList<>();

    public MethodOverride() {
    }

    public MethodOverride(ClassMethodFinder cmf, MethodDescriber md) {
        this._analyzer = cmf;
        this._method = md;
    }

    public MethodOverride(ContextConfiguration cc) {
        this._analyzer = cc.getCMFAnalyzer();
        this._method = cc.getMethodDescriber();
        this._className = cc.getClassName();
    }

    @Override
    public boolean detect() throws Exception {

        boolean ovveride_exists = false;

        // Determine all classes/interfaces that are superseeding the class being analyzed
        ReferenceTypeDeclaration rtd = _analyzer.getReferenceTypeDeclarationOfClass();
        _rt = rtd.getAllAncestors();

        _rt.forEach( ancestor ->
        {
            ReferenceTypeDeclaration rtd_ancestor = ancestor.getTypeDeclaration();

            if (!_analyzer.isIgnoredPackage(rtd_ancestor) && !ancestor.getTypeDeclaration().isInterface()) {
                // when ancestor is not a interface declaration, check if any method in super classes equals name of
                // provided method name
                rtd_ancestor.getDeclaredMethods().forEach(m ->
                {
                        if(fullSignatureMatch(m)) {
                        //if (m.getName().contentEquals(_method.getName())) {
                            //System.out.println("Class " + m.declaringType().getQualifiedName() + " has method " + nameOfMethod);
                            //System.out.println("Full signature = " + m.getSignature());
                            //System.out.println("Returns " + m.getReturnType().describe());
                            //System.out.println("Returns " + m.getQualifiedSignature());

                            addClassNameToClassList((JavaParserMethodDeclaration)m);
                            listOfNodesForOverrideMethods.add((JavaParserMethodDeclaration)m);
                        }
                });
            }
        });

        return !classesFound.isEmpty();
    }

    protected boolean fullSignatureMatch(MethodDeclaration m) {
        return  m.getReturnType().describe().contentEquals(_method.getType()) &&
                m.getName().contentEquals(_method.getName()) &&
                m.getSignature().contentEquals(_method.getSignature());
    }

    @Override
    public Map<String,List<String>> getParameterMap() {
        return classesFound;
    }

    @Override
    public CodeContext.CodeContextEnum getType() {
        return CodeContext.CodeContextEnum.MethodOverride;
    }

    protected void addClassNameToClassList(JavaParserMethodDeclaration jpClass) {
        if(!classesFound.isEmpty())
        {
            List<String> actualList = new ArrayList<>(classesFound.get(V_CLASS_LIST));

            if(!actualList.contains(jpClass.declaringType().getQualifiedName())) {
                actualList.add(jpClass.declaringType().getQualifiedName());
                classesFound.put(V_CLASS_LIST, actualList);
            }
        }
        else
        {
            classesFound.put(V_CLASS_LIST, Arrays.asList(jpClass.declaringType().getQualifiedName()));
        }
    }

    protected List<JavaParserMethodDeclaration> getOverridenMethods()
    {
        return listOfNodesForOverrideMethods;
    }
}
