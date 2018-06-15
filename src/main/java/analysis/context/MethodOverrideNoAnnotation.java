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
package analysis.context;

import aig.CodeContext;
import analysis.MethodAnalyzer.MethodDescriber;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.expr.AnnotationExpr;
import com.github.javaparser.symbolsolver.javaparser.Navigator;

import java.util.List;

/**
 * Analyzes overriden methods if thyey have @Override specified or not
 * This is done for the local class and all of its overriden methods in the ancestors
 *
 */
public class MethodOverrideNoAnnotation extends MethodOverride {

    // We assume the situation is true, cycling through all methods
    private boolean method_without_override_annotation = true;

    public MethodOverrideNoAnnotation(ContextConfiguration cc) {
        super(cc);
    }

    @Override
    public boolean detect() throws Exception {

        setNoAnnotation(false);

        if(super.detect())
        {
            // first check local class in analyzed method if annotation exists
            ClassOrInterfaceDeclaration class4Analysis = Navigator.demandClassOrInterface(
                    _analyzer.getCompilationUnit(),
                   _analyzer.getQualifiedClassName());

            List<MethodDeclaration> md = class4Analysis.getMethods();

            for(MethodDeclaration item : md)
            {
                MethodDescriber methDescr = new MethodDescriber(item);

                if (methDescr.equals(_method))
                {
                    determineMethodHasNoAnnotation(item, _analyzer.getQualifiedClassName());
                }
            }

            //  Check all ancestors for matching methods
            getOverridenMethods().forEach( method ->
            {
                // Retrieve list of annotations from the wrapped node stored
                determineMethodHasNoAnnotation(method.getWrappedNode(), method.declaringType().getClassName());
            });
        }

        return method_without_override_annotation;
    }

    private void determineMethodHasNoAnnotation(MethodDeclaration item, String className) {
        // method which are overriden should have an override annotation
        NodeList<AnnotationExpr> annotations = item.getAnnotations();

        // Set no annotation flag, if none of the overrides of the local method says @Override
        if(annotations.stream().noneMatch(anno -> anno.getName().toString().contentEquals("Override")))
        {
            setNoAnnotation(true);
            parameters.addMethodNameToVariableList(item, className);
        }
    }

    private void setNoAnnotation(boolean b) {
        method_without_override_annotation = b;
    }

    @Override
    public CodeContext.CodeContextEnum getType() {
        return CodeContext.CodeContextEnum.MethodOverrideNoAnnotation;
    }
}
