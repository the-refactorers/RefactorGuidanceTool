package analysis.context;

import analysis.dataflow.MethodDataFlowAnalyzer;

public class MethodExtract extends ContextDetector {

    protected MethodDataFlowAnalyzer _analyzer = null;

    public MethodExtract() {}

    public MethodExtract(ContextConfiguration cc) {
        this._analyzer = cc.getMethodDataFlowAnalyzer();
    }

    protected boolean validDataFlowAnalyzer() throws Exception {
        if (_analyzer!=null)
        {
            return true;
        }
        else {
            throw new Exception("Valid MethodDataFlowAnalyzer should be provided in ContextConfiguration");
        }
    }
}
