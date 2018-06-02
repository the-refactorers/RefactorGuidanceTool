package analysis.context;

import aig.CodeContext;
import analysis.MethodAnalyzer.ClassMethodFinder;
import analysis.MethodAnalyzer.MethodDescriber;
import com.github.javaparser.symbolsolver.javaparsermodel.declarations.JavaParserMethodDeclaration;
import com.github.javaparser.symbolsolver.model.declarations.ReferenceTypeDeclaration;
import com.github.javaparser.symbolsolver.model.typesystem.ReferenceType;

import java.util.*;

/**
 * Determines if given method is an override of any parent class.
 * It is not taken into account an interface. This is done bu MethodInterfaceDeclaration
 */
public class MethodOverride extends ContextDetector{

    protected String _className = null;
    protected ClassMethodFinder _analyzer = null;
    protected MethodDescriber _method = null;

    protected List<ReferenceType> _rt = null;

    protected List<JavaParserMethodDeclaration>  listOfNodesForOverrideMethods = new ArrayList<>();

    public MethodOverride() {
    }

    public MethodOverride(ClassMethodFinder cmf, MethodDescriber md) {
        this._analyzer = cmf;
        this._method = md;
    }

    public MethodOverride(ContextConfiguration cc) {
        super(cc);

        this._analyzer = cc.getCMFAnalyzer();
        this._method = cc.getMethodDescriber();
        this._className = cc.getClassName();
    }

    protected List<JavaParserMethodDeclaration> getOverridenMethods()
    {
        return listOfNodesForOverrideMethods;
    }

    @Override
    public boolean detect() throws Exception {

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
                        if(_analyzer.fullMethodSignatureMatch(m, _method)) {
                        //if (m.getName().contentEquals(_method.getName())) {
                            //System.out.println("Class " + m.declaringType().getQualifiedName() + " has method " + nameOfMethod);
                            //System.out.println("Full signature = " + m.getSignature());
                            //System.out.println("Returns " + m.getReturnType().describe());
                            //System.out.println("Returns " + m.getQualifiedSignature());

                            parameters.addClassNameToVariableList((JavaParserMethodDeclaration)m);
                            listOfNodesForOverrideMethods.add((JavaParserMethodDeclaration)m);
                        }
                });
            }
        });

        return !parameters.getCollection().isEmpty();
    }

    @Override
    public CodeContext.CodeContextEnum getType() {
        return CodeContext.CodeContextEnum.MethodOverride;
    }
}
