package analysis.context;

import ait.CodeContext;
import analysis.MethodAnalyzer.ClassMethodFinder;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.expr.AnnotationExpr;
import com.github.javaparser.symbolsolver.javaparser.Navigator;
import com.github.javaparser.symbolsolver.javaparsermodel.JavaParserFacade;
import com.github.javaparser.symbolsolver.model.declarations.MethodDeclaration;
import com.github.javaparser.symbolsolver.model.declarations.ReferenceTypeDeclaration;
import com.github.javaparser.symbolsolver.model.typesystem.ReferenceType;
import javassist.compiler.ast.MethodDecl;

import java.lang.annotation.Annotation;
import java.util.*;

public class MethodOverride implements IContextDetector{

    private String _className = null;
    protected ClassMethodFinder _analyzer = null;
    protected String _methodName = null;

    protected Map<String,List<String>> classesFound = new HashMap<>();
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
        this._className = cc.getClassName();
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

                            ClassOrInterfaceDeclaration class4Analysis = Navigator.demandClassOrInterface(
                                    _analyzer.getCompilationUnit(),
                                    _analyzer.getQualifiedClassName());

                            List<com.github.javaparser.ast.body.MethodDeclaration> md = class4Analysis.getMethods();
                            for(com.github.javaparser.ast.body.MethodDeclaration item : md)
                            {
                                if (item.getName().toString().contentEquals(_methodName))
                                {
                                    NodeList<AnnotationExpr> annotations = item.getAnnotations();
                                    if (!annotations.isEmpty())
                                    {
                                        for(AnnotationExpr ea : annotations)
                                        {
                                            System.out.println(ea.getName().asString());
                                        }
                                    }
                                }
                            }
                            NodeList<AnnotationExpr> ae =  md.get(0).getAnnotations();
                        }
                });
            }

        });

        return !classesFound.isEmpty();
    }

    @Override
    public Map<String,List<String>> getParameterMap() {
        return classesFound;
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
