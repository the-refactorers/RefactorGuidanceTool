package analysis.context;

import ait.CodeContext;
import analysis.dataflow.MethodDataFlowAnalyzer;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;
import java.util.List;

public class MethodExtractSingleArgument extends MethodExtract {

    public MethodExtractSingleArgument(ContextConfiguration cc) {
        super(cc);
    }

    public boolean detect() throws Exception {

        if (validDataFlowAnalyzer()) {
            _analyzer.start();
            List<String> vfi = _analyzer.variablesForInput();

            if (vfi.size() == 1) {
                parameters.addArgumentName(vfi.get(0));
            }
        }

        return !parameters.getCollection().isEmpty();
    }

    @Override
    public CodeContext.CodeContextEnum getType() {
        return CodeContext.CodeContextEnum.MethodExtractSingleArgument;
    }
}
