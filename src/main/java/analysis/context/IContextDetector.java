package analysis.context;

import aig.CodeContext;

// A General interface which every context class should implement
public interface IContextDetector {

    // Each detector should implement constructor like the one below
    // public <<detectorName>>(ContextConfiguration cc);

    /**
     * Detects if a specific context exists in code that is analyzed by detector
     * @return Returns true if context exists
     * @throws Exception
     */
    boolean detect() throws Exception;

    /**
     * When a context exists a hashmap is filled with parametrized values in form <"$param","param-value">
     *     which is contained in the ParameterCollector
     * @return ParameterCollector object which can be queried for the desired parameters
     */
    public ParameterCollector getParameters();

    /**
     * Type name of detector
     * @return
     */
    CodeContext.CodeContextEnum getType();
}
