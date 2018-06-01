package analysis;

import analysis.ResourceExampleClassParser;
import analysis.context.IContextDetector;
import analysis.context.ParameterCollector;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.symbolsolver.javaparser.Navigator;
import org.junit.Before;

import java.util.List;
import java.util.Map;

public class JavaParserTestSetup {

    protected CompilationUnit _cu;
    protected ResourceExampleClassParser _loader;

    protected ParameterCollector _pc;
    protected Map<String, List<String>> _params;

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

    protected void retrieveParams(IContextDetector detector) {
        _pc = detector.getParameters();
        _params = _pc.getCollection();
    }

    @Before
    public void Setup()
    {
        _loader = new ResourceExampleClassParser();
    }
}
