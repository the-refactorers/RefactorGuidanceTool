package analysis;

import analysis.MethodAnalyzer.ClassMethodFinder;
import analysis.MethodAnalyzer.MethodDescriber;
import analysis.ResourceExampleClassParser;
import analysis.context.CodeSection;
import analysis.context.IContextDetector;
import analysis.context.ParameterCollector;
import analysis.dataflow.MethodDataFlowAnalyzer;
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

    protected ClassMethodFinder _cmf;
    protected MethodDeclaration _selectedMethod;
    protected CodeSection _extractRegion = new CodeSection(-1, -1);
    protected MethodDataFlowAnalyzer _mdfaAna;

    protected MethodDeclaration findMethodDeclarationInClass(String className, String methodName) {
        return Navigator.demandMethod(Navigator.demandClass(_cu, className), methodName);
    }

    protected void CreateCompilationUnitFromTestClass(String classTemplate)
    {
        _cu = _loader.Parse(classTemplate);
    }

    protected void setupTestClass(String className, String methodName) {

        CreateCompilationUnitFromTestClass(className + ".java.txt");

        _cmf = new ClassMethodFinder();
        _cmf.initialize(_cu, className);

        // Determine name based on location
        if(_extractRegion.notDefined())
        {
            _selectedMethod = findMethodDeclarationInClass(className, methodName);
        }
        else {
            _selectedMethod = _cmf.getMethodDeclarationForLocation(_extractRegion.begin());
        }
    }

    protected void extractRegion(int start, int end)
    {
        _extractRegion = new CodeSection(start, end);
    }

    protected CompilationUnit CreateCompilationUnitFromTestClass(ResourceExampleClassParser resourceLoader, String classTemplate)
    {
        return resourceLoader.Parse(classTemplate);
    }

    protected void retrieveParams(IContextDetector detector) {
        _pc = detector.getParameters();
        _params = _pc.getCollection();
    }

    protected void mdfaAnalysis()
    {
        _mdfaAna = new MethodDataFlowAnalyzer();
        _mdfaAna.initialize(_selectedMethod, _extractRegion);
        _mdfaAna.start();
    }

    @Before
    public void Setup()
    {
        _loader = new ResourceExampleClassParser();
    }
}
