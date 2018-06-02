package analysis.context;

import aig.CodeContext;

import java.util.List;

public class MethodExtractMultiArgument extends MethodExtract {

        public MethodExtractMultiArgument(ContextConfiguration cc) {
            super(cc);
        }

        public boolean detect() throws Exception {

            if (validDataFlowAnalyzer()) {
                _analyzer.start();
                List<String> vfi = _analyzer.variablesForInput();

                if (vfi.size() > 1) {
                    vfi.forEach(argument ->
                    parameters.addArgumentName(argument));
                }
            }

            return !parameters.getCollection().isEmpty();
        }

        @Override
        public CodeContext.CodeContextEnum getType() {
            return CodeContext.CodeContextEnum.MethodExtractMultiArgument;
        }
}


