package analysis.context;

import aig.CodeContext;

import java.util.List;

public class MethodExtractNoneArguments extends MethodExtractNoneLocalDependencies {

    public MethodExtractNoneArguments(ContextConfiguration cc) {
        super(cc);
    }

    public boolean detect() throws Exception {

        super.detect();
        return _analyzer.variablesForInput().isEmpty();

    }

    @Override
    public CodeContext.CodeContextEnum getType() {
        return CodeContext.CodeContextEnum.MethodExtractNoneArguments;
    }
}
