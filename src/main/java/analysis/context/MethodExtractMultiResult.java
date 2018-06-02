package analysis.context;

import aig.CodeContext;

import java.util.List;

public class MethodExtractMultiResult extends MethodExtract {

    public MethodExtractMultiResult(ContextConfiguration cc) {
        super(cc);
    }

    public boolean detect() throws Exception {

        if (validDataFlowAnalyzer()) {
            _analyzer.start();
            List<String> vfo = _analyzer.variablesForOutput();

            if (vfo.size() > 1) {
                vfo.forEach(result ->
                        parameters.addResultName(result));
            }
        }

        return !parameters.getCollection().isEmpty();
    }

    @Override
    public CodeContext.CodeContextEnum getType() {
        return CodeContext.CodeContextEnum.MethodExtractMultiResult;
    }
}


