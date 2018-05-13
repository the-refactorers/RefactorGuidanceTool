package analysis.context;

import analysis.dataflow.MethodDataFlowAnalyzer;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.symbolsolver.javaparser.Navigator;

import java.util.List;

public class MethodExtractSingleArgument implements IContextDetection {
    CompilationUnit _cu  = null;
    MethodDataFlowAnalyzer _analyzer = null;

    public MethodExtractSingleArgument(MethodDataFlowAnalyzer mdfa)
    {
        super();
        _analyzer = mdfa;
    }

    public void setupAnalysis(CompilationUnit cu, String className, String methodName, CodeSection cs) {

        _cu = cu;

        MethodDeclaration md = Navigator.demandMethod(Navigator.demandClass(_cu, className), methodName);
        _analyzer = new MethodDataFlowAnalyzer(md);
        _analyzer.setExtractSection(cs.begin(),cs.end());
    }

    public boolean detect() throws Exception {

        boolean result = false;

        if (_analyzer != null) {

            _analyzer.start();
            List<String> vfi = _analyzer.variablesForInput();

            result = (vfi.size() == 1);
        }
        else
        {
            throw new Exception("Analyzer not configured. Call setupAnalysis(...) first");
        }

        return result;
    }
}
