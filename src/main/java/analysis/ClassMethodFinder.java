/**
 Copyright (C) 2018, Patrick de Beer

 This program is free software: you can redistribute it and/or modify
 it under the terms of the GNU General Public License as published by
 the Free Software Foundation, either version 3 of the License, or
 (at your option) any later version.

 This program is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 GNU General Public License for more details.

 You should have received a copy of the GNU General Public License
 along with this program.  If not, see <http://www.gnu.org/licenses/>.
 **/

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
import com.github.javaparser.symbolsolver.model.typesystem.ReferenceType;
import helpers.Checker;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ClassMethodFinder {

    private CompilationUnit _cu;
    private String _qname;
    private TypeSolver _symbolSolver;
    private String _declaredInInterface;

    public ClassMethodFinder(CompilationUnit cu, String qualifiedName) {
        _cu = cu;
        _qname = qualifiedName;

        _symbolSolver = new CombinedTypeSolver(
                new ReflectionTypeSolver(),
                new JavaParserTypeSolver(new File("SymbolSolver/src/main/java/"))
        );
    }

    private boolean isIgnoredPackage(ReferenceTypeDeclaration rtd)
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

        ClassOrInterfaceDeclaration class4Analysis = Navigator.demandClass(_cu, _qname);
        ReferenceTypeDeclaration rtd = JavaParserFacade.get(_symbolSolver).getTypeDeclaration(class4Analysis);

         //simple test to find all declared methods in local class and all of its inherited classes
        List<ReferenceType> rt = rtd.getAllAncestors();
        rt.forEach( ancestor ->
        {
            ReferenceTypeDeclaration rtd_ancestor = ancestor.getTypeDeclaration();

            if (!isIgnoredPackage(rtd_ancestor)) {
                rtd_ancestor.getDeclaredMethods().forEach(m ->
                {
                   // System.out.println(String.format("A:  %s", m.getQualifiedSignature()));
                   // System.out.println(String.format("declared in:  %s", m.declaringType().getName()));
                   // System.out.println(String.format("is interface? %s", m.declaringType().isInterface()?"yes": "no"));
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

         System.out.println();

        return allDefinedMethods;
    }

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
    public String getMethodNameForLocation(int location) {
        String methodName = "";

        if(isLocationInMethod(location)) {
            // Find in the AST the class declaration of the provided class in the Ctor
            List<MethodDeclaration> lmd = getMethodDeclarations();

            for (MethodDeclaration method : lmd) {
                if (Checker.inRangeInclusive(method.getRange().get().begin.line, method.getRange().get().end.line, location)) {
                    methodName = method.getNameAsString();
                    break;
                }
            }
        }

        return methodName;
    }

    /**
     * Determine if the provided methodName has been declared in an interface or not
     * @param methodName Name of method to be resolved
     * @return true, method name has been declared initially in an interface definition
     */
    public boolean isMethodDeclaredFirstTimeInInterface(String methodName) {

        boolean methodDeclaredInInterface = false;

        // When specific method is visible in class, figure out if it has been defined
        // in an interface or not
        if (hasMethodDefined(methodName))
        {
            // Get type declaration of given class, so we can resolve method declaration outside
            // the class definition
            ClassOrInterfaceDeclaration class4Analysis = Navigator.demandClass(_cu, _qname);
            ReferenceTypeDeclaration rtd = JavaParserFacade.get(_symbolSolver).getTypeDeclaration(class4Analysis);
            List<ReferenceType> rt = rtd.asClass().getAllInterfaces();

            for(ReferenceType ancestor : rt)
            {
                ReferenceTypeDeclaration rtd_ancestor = ancestor.getTypeDeclaration();

                // When interface declaration and not one of the ignored packages
                // Check if provided methodName is present in the stream of declared methods
                // of this interface
                if (!isIgnoredPackage(rtd_ancestor) &&
                        rtd_ancestor.getDeclaredMethods().stream().anyMatch(method -> method.getName().equals(methodName)))
                {
                    _declaredInInterface = rtd_ancestor.getName();
                    methodDeclaredInInterface = true;
                    break;
                }
            }
        }

        return methodDeclaredInInterface;
    }

    public String methodDefinedInInterface()
    {
        return _declaredInInterface;
    }

    /**
     * Is method defined in this specific class [Note: no check on return types and parameters yet in this case]
     * @param methodName Name of method to be looked up
     * @return true, method name exists in class definition
     */
    public boolean hasMethodDefined(String methodName) {
        boolean methodFound = false;

        for (MethodDeclaration methodDecl : getMethodDeclarations())
        {
            methodFound = methodDecl.getName().toString().equals(methodName) || methodFound;
        }

        return methodFound;
    }

    public boolean isMethodDefinedInSuperClass(String methodName) {
        boolean methodDeclaredInSuperClass = false;

        // When specific method is visible in class, figure out if it has been defined
        // in an interface or not
        if (hasMethodDefined(methodName))
        {
            // Get type declaration of given class, so we can resolve method declaration outside
            // the class definition
            ClassOrInterfaceDeclaration class4Analysis = Navigator.demandClass(_cu, _qname);
            ReferenceTypeDeclaration rtd = JavaParserFacade.get(_symbolSolver).getTypeDeclaration(class4Analysis);
            List<ReferenceType> rt = rtd.asClass().getAllSuperClasses();

            for(ReferenceType ancestor : rt)
            {
                ReferenceTypeDeclaration rtd_ancestor = ancestor.getTypeDeclaration();

                // When interface declaration and not one of the ignored packages
                // Check if provided methodName is present in the stream of declared methods
                // of this interface
                if (!isIgnoredPackage(rtd_ancestor) &&
                        rtd_ancestor.getDeclaredMethods().stream().anyMatch(method -> method.getName().equals(methodName)))
                {
                    methodDeclaredInSuperClass = true;
                    break;
                }
            }
        }

        return methodDeclaredInSuperClass;
    }

    public boolean contextMultipleDeclarations(String methodName)
    {
        return isMethodDefinedInSuperClass(methodName) || isMethodDeclaredFirstTimeInInterface(methodName);
    }

    public boolean contextDeclaredInInterface(String methodName)
    {
        return isMethodDeclaredFirstTimeInInterface(methodName);
    }

    public boolean contextDeclaredInSuperClass(String methodName)
    {
        return isMethodDefinedInSuperClass(methodName);
    }
}
