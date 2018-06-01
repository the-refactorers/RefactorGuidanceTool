package analysis.context;

import ait.CodeContext;

import java.util.List;

public class MethodExtractSingleResult extends MethodExtract {

    public MethodExtractSingleResult(ContextConfiguration cc) {
        super(cc);
    }

    public boolean detect() throws Exception {

        if (validDataFlowAnalyzer()) {
            _analyzer.start();
            List<String> vfo = _analyzer.variablesForOutput();

            if (vfo.size() == 1) {
                parameters.addResultName(vfo.get(0));
            }
        }

        return !parameters.getCollection().isEmpty();
    }

    @Override
    public CodeContext.CodeContextEnum getType() {
        return CodeContext.CodeContextEnum.MethodExtractSingleResult;
    }

}
