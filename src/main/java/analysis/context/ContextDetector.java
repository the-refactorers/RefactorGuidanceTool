package analysis.context;

import aig.CodeContext;

public class ContextDetector implements IContextDetector {

    protected ParameterCollector parameters = new ParameterCollector();

    public ContextDetector() {

    }

    public ContextDetector(ContextConfiguration cc)    {

    }

    @Override
    public boolean detect() throws Exception {
        return false;
    }

    @Override
    public ParameterCollector getParameters() {

        return parameters;
    }

    @Override
    public CodeContext.CodeContextEnum getType() {
        return null;
    }

}
