package rpg.tool;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.symbolsolver.javaparser.Navigator;
import com.github.javaparser.symbolsolver.javaparsermodel.JavaParserFacade;
import com.github.javaparser.symbolsolver.model.declarations.ReferenceTypeDeclaration;
import com.github.javaparser.symbolsolver.model.resolution.TypeSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.CombinedTypeSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.JavaParserTypeSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.ReflectionTypeSolver;

import java.io.File;
import java.util.List;

class ClassMethodFinder {

    private CompilationUnit _cu;
    private String _qname;

    ClassMethodFinder(CompilationUnit cu, String qualifiedName) {
        _cu = cu;
        _qname = "TwoMethodClass"; //qualifiedName;
    }

    void ListAllMethods() {

        TypeSolver mySolver = new CombinedTypeSolver(
                new ReflectionTypeSolver(),
                new JavaParserTypeSolver(new File("SymbolSolver/src/main/java/"))
        );

        // Find in the AST the class declaration of the provided class in the Ctor
        ClassOrInterfaceDeclaration class4Analysis = Navigator.demandClass(_cu, _qname);

        List<MethodDeclaration> lmd = class4Analysis.getMethods();
        lmd.forEach (method ->
            System.out.println(String.format(" %s %s : range %s", method.getType(), method.getSignature(), method.getRange().toString()))  );
        /*
        ReferenceTypeDeclaration rtd = JavaParserFacade.get(mySolver).getTypeDeclaration(class4Analysis);

        rtd.getDeclaredMethods().forEach(m ->
                System.out.println(String.format("  %s", m.getQualifiedSignature())));
*/
         //System.out.println();
    }

    // given a location -> display method name where the location is present (if a method) and in which class it is contained
}
