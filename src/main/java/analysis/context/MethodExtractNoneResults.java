package analysis.context;

import aig.CodeContext;

public class MethodExtractNoneResults extends MethodExtractNoneLocalDependencies {

    public MethodExtractNoneResults(ContextConfiguration cc) {
        super(cc);
    }

    public boolean detect() throws Exception {
        super.detect();
        return _analyzer.variablesForOutput().isEmpty();
    }

    @Override
    public CodeContext.CodeContextEnum getType() {
        return CodeContext.CodeContextEnum.MethodExtractNoneResults;
    }
}
