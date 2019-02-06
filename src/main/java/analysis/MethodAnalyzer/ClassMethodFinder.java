/**
 *  This file is part of RefactorGuidance project. Which explores possibilities to generate context based
 *  instructions on how to refactor a piece of Java code. This applied in an education setting (bachelor SE students)
 *
 *      Copyright (C) 2018, Patrick de Beer, p.debeer@fontys.nl
 *
 *          This program is free software: you can redistribute it and/or modify
 *          it under the terms of the GNU General Public License as published by
 *          the Free Software Foundation, either version 3 of the License, or
 *          (at your option) any later version.
 *
 *          This program is distributed in the hope that it will be useful,
 *          but WITHOUT ANY WARRANTY; without even the implied warranty of
 *          MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *          GNU General Public License for more details.
 *
 *          You should have received a copy of the GNU General Public License
 *          along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package analysis.MethodAnalyzer;

import analysis.ICodeAnalyzer;
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
import com.github.javaparser.symbolsolver.model.typesystem.ReferenceType;
import helpers.Checker;
import javassist.compiler.ast.MethodDecl;

import java.io.File;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class ClassMethodFinder implements ICodeAnalyzer {

    private CompilationUnit _cu;
    private String _qname;
    private TypeSolver _symbolSolver;
    private String _declaredInInterface;

    public void initialize(CompilationUnit cu, String qualifiedName) {
        _cu = cu;
        _qname = qualifiedName;

        _symbolSolver = new CombinedTypeSolver(
                new ReflectionTypeSolver(),
                new JavaParserTypeSolver(new File("src/main/java/")),
                new JavaParserTypeSolver(new File("C:\\Dropbox\\sources\\RefactorGuidance\\RefactorScenarios\\SimpleRename")));
    }

    public String getQualifiedClassName()
    {
        return _qname;
    }

    public boolean isIgnoredPackage(ReferenceTypeDeclaration rtd)
    {
        boolean ignoredPackage = false;

       if (rtd.hasName() && rtd.getPackageName().equalsIgnoreCase("java.lang") )
       {
           ignoredPackage = true;
       }

       return ignoredPackage;
    }

    public List<String> getAllDefinedMethods() {

        List<String> allDefinedMethods = new ArrayList<String>();

        // Find in the AST the class declaration of the provided class in the Ctor
        List<MethodDeclaration> lmd = getMethodDeclarations();

        lmd.forEach (method -> {
            String formattedOut = String.format(" %s %s : range %s", method.getType(), method.getSignature(), method.getRange().toString());
            //System.out.println(formattedOut);
            allDefinedMethods.add(formattedOut);
            });

        ReferenceTypeDeclaration rtd = getReferenceTypeDeclarationOfClass();

         //simple test to find all declared methods in local class and all of its inherited classes
        List<ReferenceType> rt = rtd.getAllAncestors();
        rt.forEach( ancestor ->
        {
            ReferenceTypeDeclaration rtd_ancestor = ancestor.getTypeDeclaration();

            if (!isIgnoredPackage(rtd_ancestor)) {
                rtd_ancestor.getDeclaredMethods().forEach(m ->
                {
                   //System.out.println(String.format("A:  %s", m.getQualifiedSignature()));
                   //System.out.println(String.format("declared in:  %s", m.declaringType().getName()));
                   //System.out.println(String.format("is interface? %s", m.declaringType().isInterface()?"yes": "no"));
                });
            }
        });

        if (!isIgnoredPackage(rtd)) {
            rtd.getAllMethods().forEach(m ->
            {
                if (!m.getQualifiedSignature().contains("java.lang")) {
                   // System.out.println(String.format("  %s", m.getQualifiedSignature()));
                   // System.out.println(String.format("declared in:  %s", m.declaringType().getName()));
                }
            });
        }

        // System.out.println();

        return allDefinedMethods;
    }

    /**
     * Returns list of MethodDeclaration objects, describing properties of each method in specific class _qname
     * @return
     */
    private List<MethodDeclaration> getMethodDeclarations() {

        ClassOrInterfaceDeclaration class4Analysis = Navigator.demandClassOrInterface(_cu, _qname);
        return class4Analysis.getMethods();
    }

    /**
     * Tells if a file line location lies within scope of a method
     *
     * @param location Line number in .java file
     * @return true if line number lies in method scope
     */
    public boolean isLocationInMethod(int location) {

        boolean locationInMethodDefinition = false;

        // Find in the AST the class declaration of the provided class in the Ctor
        List<MethodDeclaration> lmd = getMethodDeclarations();

        for (MethodDeclaration method : lmd)
        {
            if( Checker.inRangeInclusive(method.getRange().get().begin.line, method.getRange().get().end.line, location))
                locationInMethodDefinition = true;
        }

        return locationInMethodDefinition;
    }

    /**
     * Determine in which method  a specific location in a .java file is located
     *
     * @param location  Line number in .java file
     * @return Method name when location is inside a method, otherwise empty string
     */
    public MethodDescriber getMethodDescriberForLocation(int location) {

        MethodDescriber methodName = new MethodDescriber();

        if(isLocationInMethod(location)) {
            // Find in the AST the class declaration of the provided class in the Ctor
            List<MethodDeclaration> lmd = getMethodDeclarations();

            for (MethodDeclaration method : lmd) {
                if (Checker.inRangeInclusive(method.getRange().get().begin.line, method.getRange().get().end.line, location)) {
                    methodName = new MethodDescriber(method);
                    break;
                }
            }
        }

        return methodName;
    }

    public MethodDeclaration getMethodDeclarationForLocation(int location) {

        MethodDeclaration methodDecl = null;

        if(isLocationInMethod(location)) {
            // Find in the AST the class declaration of the provided class in the Ctor
            List<MethodDeclaration> lmd = getMethodDeclarations();

            for (MethodDeclaration method : lmd) {
                if (Checker.inRangeInclusive(method.getRange().get().begin.line, method.getRange().get().end.line, location)) {
                    methodDecl = method;
                    break;
                }
            }
        }

        return methodDecl;
    }

    /**
     * Determine if the provided methodName has been declared in an interface or not
     * @param method Name of method to be resolved
     * @return true, method name has been declared initially in an interface definition
     */
    public boolean isMethodDeclaredFirstTimeInInterface(MethodDescriber method) throws Exception {

        boolean methodDeclaredInInterface = false;

        // When specific method is visible in class, figure out if it has been defined
        // in an interface or not
        if (hasMethodDefined(method))
        {
            // Get type declaration of given class, so we can resolve method declaration outside
            // the class definition
            ReferenceTypeDeclaration rtd = getReferenceTypeDeclarationOfClass();
            List<ReferenceType> rt = rtd.asClass().getAllInterfaces();

            for(ReferenceType ancestor : rt)
            {
                ReferenceTypeDeclaration rtd_ancestor = ancestor.getTypeDeclaration();

                // When interface declaration and not one of the ignored packages
                // Check if provided methodName is present in the stream of declared methods
                // of this interface

                Set<com.github.javaparser.symbolsolver.model.declarations.MethodDeclaration>s = rtd_ancestor.getDeclaredMethods();

                for(com.github.javaparser.symbolsolver.model.declarations.MethodDeclaration md : s) {
                    System.out.println(md.getName() + "\n");
                    System.out.println(md.getReturnType().describe() + "\n");
                    System.out.println(method.getType() + "\n");
                    System.out.println(md.getSignature() + "\n");
                }

                if (!isIgnoredPackage(rtd_ancestor) &&
                        rtd_ancestor.getDeclaredMethods().stream().anyMatch(m ->
                                m.getName().equals(method.getName()) &&
                                m.getReturnType().describe().contentEquals(method.getType()) &&
                                m.getSignature().contentEquals(method.getSignature())
                        ))
                {
                    _declaredInInterface = rtd_ancestor.getName();
                    methodDeclaredInInterface = true;
                    break;
                }
            }
        }
        else
        {
            throw new Exception("Method " + toFullMethod(method) + "does not exist");
        }

        return methodDeclaredInInterface;
    }

    public ReferenceTypeDeclaration getReferenceTypeDeclarationOfClass() {
        ClassOrInterfaceDeclaration class4Analysis = Navigator.demandClass(_cu, _qname);
        ReferenceTypeDeclaration rtd = JavaParserFacade.get(_symbolSolver).getTypeDeclaration(class4Analysis);

        return rtd;
    }

    public MethodDeclaration getMethodDeclarationAST(String methodName)
    {
        return Navigator.demandMethod(Navigator.demandClass(_cu, _qname), methodName);
    }

    public String methodDefinedInInterface()
    {
        return _declaredInInterface;
    }

    /**
     * Is method defined in this specific class [Note: no check on return types and parameters yet in this case]
     * @param method Name of method to be looked up
     * @return true, method name exists in class definition
     */
    public boolean hasMethodDefined(MethodDescriber method) {
        boolean methodFound = false;

        for (MethodDeclaration methodDecl : getMethodDeclarations())
        {
            //methodFound = methodDecl.getName().toString().equals(method) || methodFound;
            System.out.print(methodDecl.getSignature().toString());
            methodFound = fullSignatureMatchAST(method, methodDecl) || methodFound;
        }

        return methodFound;
    }

    public boolean isMethodDefinedInSuperClass(MethodDescriber method) throws Exception {
        boolean methodDeclaredInSuperClass = false;

        // When specific method is visible in class, figure out if it has been defined
        // in an interface or not
        if (hasMethodDefined(method))
        {
            // Get type declaration of given class, so we can resolve method declaration outside
            // the class definition
            ReferenceTypeDeclaration rtd = getReferenceTypeDeclarationOfClass();
            List<ReferenceType> rt = rtd.asClass().getAllSuperClasses();

            for(ReferenceType ancestor : rt)
            {
                ReferenceTypeDeclaration rtd_ancestor = ancestor.getTypeDeclaration();

                // When interface declaration and not one of the ignored packages
                // Check if provided methodName is present in the stream of declared methods
                // of this interface
                if (!isIgnoredPackage(rtd_ancestor) &&
                    rtd_ancestor.getDeclaredMethods().stream().anyMatch(m ->
                            m.getName().equals(method.getName()) &&
                            m.getReturnType().describe().contentEquals(method.getType()) &&
                            m.getSignature().contentEquals(method.getSignature())
                    ))
                {
                    methodDeclaredInSuperClass = true;
                    break;
                }
            }
        }
        else
        {
            throw new Exception("Method " + toFullMethod(method) + " does not exist");
        }

        return methodDeclaredInSuperClass;
    }

    private String toFullMethod(MethodDescriber method)
    {
        return method.getType() + " " + method.getSignature();
    }

    //@todo: has been moved to context
    public boolean contextMultipleDeclarations(MethodDescriber method) throws Exception {
        return isMethodDefinedInSuperClass(method) || isMethodDeclaredFirstTimeInInterface(method);
    }

    //@todo: has been moved to context
    public boolean contextDeclaredInInterface(MethodDescriber method) throws Exception {
        return isMethodDeclaredFirstTimeInInterface(method);
    }

    //@todo: has been moved to context
    public boolean contextDeclaredInSuperClass(MethodDescriber method) throws Exception {
        return isMethodDefinedInSuperClass(method);
    }

    private boolean fullSignatureMatchAST(MethodDescriber given, MethodDeclaration match) {

        return  given.getType().contentEquals(match.getType().asString()) &&
                given.getName().contentEquals(match.getName().asString()) &&
                given.getSignature().contentEquals(match.getSignature().asString());

    }

    // Check for an exact match of the method
    // This is also done on the parameter signature level on types, so names of parameters are allowed to be different
    public boolean fullMethodSignatureMatch(com.github.javaparser.symbolsolver.model.declarations.MethodDeclaration m,
                                            MethodDescriber md) {
        return  m.getReturnType().describe().contentEquals(md.getType()) &&
                m.getName().contentEquals(md.getName()) &&
                m.getSignature().contentEquals(md.getSignature());
    }

    public boolean MethodNameOnlyMatch(com.github.javaparser.symbolsolver.model.declarations.MethodDeclaration method,
                                       MethodDescriber md) {
        return method.getName().contentEquals(md.getName());
    }

    @Override
    public void start() {
        // No special processing has to be performed
    }

    public CompilationUnit getCompilationUnit() {
        return _cu;
    }
}
