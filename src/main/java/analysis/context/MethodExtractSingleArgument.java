package analysis.context;

import ait.CodeContext;
import analysis.dataflow.MethodDataFlowAnalyzer;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.symbolsolver.javaparser.Navigator;

import java.util.List;
import java.util.Map;

public class MethodExtractSingleArgument implements IContextDetector {
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
        _analyzer = new MethodDataFlowAnalyzer();
        _analyzer.setMethod(md);
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

    @Override
    public Map<String,String> getParameterMap() {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public CodeContext.CodeContextEnum getType() {
        return CodeContext.CodeContextEnum.intramethod_extract_single_argument;
    }
}
