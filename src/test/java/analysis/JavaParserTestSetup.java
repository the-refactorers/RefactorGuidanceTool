package analysis;

import analysis.ResourceExampleClassParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.symbolsolver.javaparser.Navigator;
import org.junit.Before;

public class JavaParserTestSetup {

    protected CompilationUnit _cu;
    protected ResourceExampleClassParser _loader;

    protected MethodDeclaration findMethodDeclarationInClass(String className, String methodName) {
        return Navigator.demandMethod(Navigator.demandClass(_cu, className), methodName);
    }

    protected void CreateCompilationUnitFromTestClass(String classTemplate)
    {
        _cu = _loader.Parse(classTemplate);
    }

    protected MethodDeclaration setupTestClass(String className, String methodName) {
        //String className = "ExtractMethodMarkerCases";
        CreateCompilationUnitFromTestClass(className + ".java.txt");
        return findMethodDeclarationInClass(className, methodName);
    }

    protected CompilationUnit CreateCompilationUnitFromTestClass(ResourceExampleClassParser resourceLoader, String classTemplate)
    {
        return resourceLoader.Parse(classTemplate);
    }

    @Before
    public void Setup()
    {
        _loader = new ResourceExampleClassParser();
    }
}
