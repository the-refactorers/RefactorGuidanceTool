package analysis.context;

import ait.CodeContext;
import analysis.dataflow.MethodDataFlowAnalyzer;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.symbolsolver.javaparser.Navigator;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.List;
import java.util.Map;

public class MethodExtractNoneLocalDependencies implements IContextDetector {

    MethodDataFlowAnalyzer _analyzer = null;

    public MethodExtractNoneLocalDependencies(ContextConfiguration cc) {
        this._analyzer = cc.getMethodDataFlowAnalyzer();
    }

    public boolean detect() throws Exception {

        boolean result = false;

        if (_analyzer != null) {

            _analyzer.start();
            List<String> vfi = _analyzer.variablesForInput();
            List<String> vfo = _analyzer.variablesForOutput();

            result = vfi.isEmpty() && vfo.isEmpty();
        }
        else
        {
            throw new Exception("Analyzer not configured. Call setupAnalysis(...) first");
        }

        return result;
    }

    @Override
    public ParameterCollector getParameters() {
        throw new NotImplementedException();
    }

    @Override
    public CodeContext.CodeContextEnum getType() {
        return CodeContext.CodeContextEnum.MethodExtractNoneLocalDependencies;
    }
}
