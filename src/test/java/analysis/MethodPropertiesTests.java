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
package analysis;

import analysis.dataflow.LocalDeclaredVarsFinder;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.symbolsolver.javaparser.Navigator;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

public class MethodPropertiesTests extends JavaParserTestSetup{

    @Test
    public void MethodNoneVarDeclaredLocal() {
        CreateCompilationUnitFromTestClass("ExtractMethodZeroInZeroOut.java.txt");

        MethodDeclaration md = findMethodDeclarationInClass("ExtractMethodZeroInZeroOut", "NonLocalsDeclared");

        LocalDeclaredVarsFinder localVars = new LocalDeclaredVarsFinder(md);

        localVars.find();
        Assert.assertEquals(0, localVars.getLocalVars().size());
    }

    @Test
    public void MethodOneVarDeclaredLocal() {
        CreateCompilationUnitFromTestClass("ExtractMethodZeroInZeroOut.java.txt");

        MethodDeclaration md = findMethodDeclarationInClass("ExtractMethodZeroInZeroOut", "MethodOneLocalDeclared");

        LocalDeclaredVarsFinder localVars = new LocalDeclaredVarsFinder(md);

        localVars.find();
        Assert.assertEquals(1, localVars.getLocalVars().size());
    }

    @Test
    public void MethodMultipleVarsDeclaredLocal() {
        CreateCompilationUnitFromTestClass("ExtractMethodZeroInZeroOut.java.txt");

        MethodDeclaration md = findMethodDeclarationInClass("ExtractMethodZeroInZeroOut", "MethodThreeLocalDeclared");

        LocalDeclaredVarsFinder localVars = new LocalDeclaredVarsFinder(md);

        localVars.find();
        Assert.assertEquals(3, localVars.getLocalVars().size());
    }

    @Test
    public void MethodVarsDeclaredinMethodSignatureAndScope() {
        CreateCompilationUnitFromTestClass("ExtractMethodZeroInZeroOut.java.txt");

        MethodDeclaration md = findMethodDeclarationInClass("ExtractMethodZeroInZeroOut", "MethodOneLocalOneInputParams");

        LocalDeclaredVarsFinder localVars = new LocalDeclaredVarsFinder(md);

        localVars.find();
        Assert.assertEquals(2, localVars.getLocalVars().size());
    }
}
