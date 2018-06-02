package analysis.context;

import aig.CodeContext;
import java.util.List;

public class MethodExtractNameHiding extends MethodExtract {

    public MethodExtractNameHiding(ContextConfiguration cc) {
        super(cc);
    }

    public boolean detect() throws Exception {

        if (validDataFlowAnalyzer()) {
            _analyzer.start();

            List<String> varsInExtracted = _analyzer.getVariablesUsedInExtractSection();
        }

        return !parameters.getCollection().isEmpty();
    }

    @Override
    public CodeContext.CodeContextEnum getType() {
        return CodeContext.CodeContextEnum.MethodExtractNameHiding;
    }
}
