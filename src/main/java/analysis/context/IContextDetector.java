package analysis.context;

import ait.CodeContext;

// A General interface which every context class should implement
public interface IContextDetector {

    // When analyzes has detected specific context, it will hold internally a set of relevant parameters
    // to this context definition. getParameters extends a set of ParamCOllection, which can be used later in the
    // generation of instructions by parsing the AIT and filling in parameters present in this tree
    //ParamCollection getParameters(ParamCollection setOfParameters);

    // pass on object with specific configuration of analysis parameters
    // e.g. method name, sections to act on
    //void setupAnalysis(ContextAnalysisConfiguration config);

    // Analyse code to see if specific context is present in code
    // Built up relevant parameter set, which can be retrieved later
    boolean detect() throws Exception;

    CodeContext.CodeContextEnum getType();
}