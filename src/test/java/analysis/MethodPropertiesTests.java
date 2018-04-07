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
