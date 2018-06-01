package analysis.context;

import ait.CodeContext;
import analysis.dataflow.MethodDataFlowAnalyzer;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;
import java.util.List;

public class MethodExtractNoneLocalDependencies extends MethodExtract {

    public MethodExtractNoneLocalDependencies(ContextConfiguration cc) {
        super(cc);
    }

    public boolean detect() throws Exception {

        boolean result = false;

        if (validDataFlowAnalyzer()) {

            _analyzer.start();
            List<String> vfi = _analyzer.variablesForInput();
            List<String> vfo = _analyzer.variablesForOutput();

            result = vfi.isEmpty() && vfo.isEmpty();
        }

        return result;
    }

    @Override
    public CodeContext.CodeContextEnum getType() {
        return CodeContext.CodeContextEnum.MethodExtractNoneLocalDependencies;
    }
}
