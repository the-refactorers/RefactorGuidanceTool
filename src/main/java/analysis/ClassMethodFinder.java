package analysis;

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
import helpers.Checker;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

class ClassMethodFinder {

    private CompilationUnit _cu;
    private String _qname;

    ClassMethodFinder(CompilationUnit cu, String qualifiedName) {
        _cu = cu;
        _qname = "TwoMethodClass"; //qualifiedName;
    }

    List<String> getAllDefinedMethods() {

        List<String> allDefinedMethods = new ArrayList<String>();

        TypeSolver mySolver = new CombinedTypeSolver(
                new ReflectionTypeSolver(),
                new JavaParserTypeSolver(new File("SymbolSolver/src/main/java/"))
        );

        // Find in the AST the class declaration of the provided class in the Ctor
        ClassOrInterfaceDeclaration class4Analysis = Navigator.demandClass(_cu, _qname);

        List<MethodDeclaration> lmd = class4Analysis.getMethods();
        lmd.forEach (method -> {
            String formattedOut = String.format(" %s %s : range %s", method.getType(), method.getSignature(), method.getRange().toString());
            System.out.println(formattedOut);
            allDefinedMethods.add(formattedOut);
            });
        /*
        ReferenceTypeDeclaration rtd = JavaP)arserFacade.get(mySolver).getTypeDeclaration(class4Analysis);

        rtd.getDeclaredMethods().forEach(m ->
                System.out.println(String.format("  %s", m.getQualifiedSignature())));
*/
         //System.out.println();

        return allDefinedMethods;
    }

    public boolean isLocationInMethod(int location) {

        boolean locationInMethodDefinition = false;

        TypeSolver mySolver = new CombinedTypeSolver(
                new ReflectionTypeSolver(),
                new JavaParserTypeSolver(new File("SymbolSolver/src/main/java/"))
        );

        // Find in the AST the class declaration of the provided class in the Ctor
        ClassOrInterfaceDeclaration class4Analysis = Navigator.demandClass(_cu, _qname);
        List<MethodDeclaration> lmd = class4Analysis.getMethods();

        for (MethodDeclaration method : lmd)
        {
            if( Checker.inRangeInclusive(method.getRange().get().begin.line, method.getRange().get().end.line, location))
                locationInMethodDefinition = true;
        }

        return false;
    }

    public String getMethodNameForLocation(int location) {
        String methodName = new String();

        TypeSolver mySolver = new CombinedTypeSolver(
                new ReflectionTypeSolver(),
                new JavaParserTypeSolver(new File("SymbolSolver/src/main/java/"))
        );

        // Find in the AST the class declaration of the provided class in the Ctor
        ClassOrInterfaceDeclaration class4Analysis = Navigator.demandClass(_cu, _qname);
        List<MethodDeclaration> lmd = class4Analysis.getMethods();

        for (MethodDeclaration method : lmd)
        {
            if( Checker.inRangeInclusive(method.getRange().get().begin.line, method.getRange().get().end.line, location)) {
                methodName = method.getNameAsString();
                break;
            }
        }

        return methodName;
    }
}
