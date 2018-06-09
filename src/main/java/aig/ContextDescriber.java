package aig;

public class ContextDescriber {
     public final CodeContext.CodeContextEnum decisionContext;
     public final String riskDescription;

    ContextDescriber(CodeContext.CodeContextEnum decisionCtxt, String riskDescription)
    {
        this.decisionContext = decisionCtxt;
        this.riskDescription = riskDescription;
    }

    ContextDescriber(CodeContext.CodeContextEnum decisionCtxt)
    {
        this.decisionContext = decisionCtxt;
        this.riskDescription = "";
    }
}
