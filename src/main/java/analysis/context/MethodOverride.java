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

        // Determine all classes/interfaces that are superseeding the class being analyzed
        ReferenceTypeDeclaration rtd = _analyzer.getReferenceTypeDeclarationOfClass();
        List<ReferenceType> rt = rtd.getAllAncestors();

        rt.forEach( ancestor ->
        {
            ReferenceTypeDeclaration rtd_ancestor = ancestor.getTypeDeclaration();

            if (!_analyzer.isIgnoredPackage(rtd_ancestor)) {
                // when ancestor is not a interface declaration, check if method equals name of qualified method name
                if(!ancestor.getTypeDeclaration().isInterface()) {

                    rtd_ancestor.getDeclaredMethods().forEach(m ->
                    {
                            String nameOfMethod = m.getName();

                            if (nameOfMethod.contentEquals(_methodName)) {
                                System.out.println("Class " + m.declaringType().getQualifiedName() + " has method " + nameOfMethod);
                                System.out.println("Full signature = " + m.getSignature());
                            }


                        //System.out.println(String.format("A:  %s", m.getQualifiedSignature()));
                        //System.out.println(String.format("declared in:  %s", m.declaringType().getName()));
                        //System.out.println(String.format("is interface? %s", m.declaringType().isInterface()?"yes": "no"));
                    });
                }
            }
        });

        return false;
    }

    @Override
    public Map<String,List<String>> getParameterMap() {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public CodeContext.CodeContextEnum getType() {
        return CodeContext.CodeContextEnum.MethodOverride;
    }
}
