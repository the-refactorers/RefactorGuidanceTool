package analysis.context;

import ait.CodeContext;

import java.util.List;
import java.util.Map;

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

    /**
     * Detects if a specific context exists in code that is analyzed by detector
     * @return Returns true if context exists
     * @throws Exception
     */
    boolean detect() throws Exception;

    /**
     * When a context exists a hashmap is filled with parametrized values in form <"$param","param-value">
     * @return Hashmap with concrete values for parameters
     */
    Map<String, List<String>> getParameterMap();

    /**
     * Type name of detector
     * @return
     */
    CodeContext.CodeContextEnum getType();
}
