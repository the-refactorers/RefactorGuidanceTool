package analysis.context;

import ait.AdaptiveInstructionTree;
import ait.CodeContext;
import analysis.ICodeAnalyzer;
import analysis.MethodAnalyzer.ClassMethodFinder;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

/**
 *  Class builds up a set of context detectors that are needed to detect contextual situations
 *  in a given adaptive instruction tree. The list of detectors can be provided to the procedural
 *  guidance generator
 */
public class ContextDetectorSetBuilder {

    private AdaptiveInstructionTree _ait = null;

    private ContextConfiguration _analyzerConfig = null;

    private List<IContextDetector> _contextDetectors = new ArrayList<IContextDetector>();
    private List<ICodeAnalyzer> _analyzers = new ArrayList<ICodeAnalyzer>();

    public void SetAIT() {
    }

    public void SetAIT(AdaptiveInstructionTree ait)  {
        setAIT(ait);
    }

    public void setAIT(AdaptiveInstructionTree ait) {
        this._ait = ait;
    }

    public List<IContextDetector> getContextDetectors() throws Exception {
        if(_analyzerConfig == null) {
            throw new Exception("No ContextConfiguration object was defined. call setContextAnalyzerConfiguration(...) first");
        }

        EnumSet<CodeContext.CodeContextEnum> completeCodeContext = this._ait.allUniqueCodeContextInTree();
        String refactoringProcessName = this._ait.getRefactorMechanic();

        if(refactoringProcessName.contentEquals("Rename Method"))
        {
            // Maak de analyzers 1x aan in, om geen dubbele instanties te krijgen
            // They can be directly initialized if contextanalyzerconfiguration object is present

            // voor deze specifieke method
            // uitlezen cu + methodname of line numers
            // initieren ClassMethodFinder analyzer (en evt. andere analyzers)
            // instantieren detectors, waarbij in het config object de juiste analyzers staan, die een detector
            // intern weet op te vragen.

            // Voeg de analuzers toe aan ContextAnalyzerCOnfiguration
            BuildRenameContextDetectors(completeCodeContext); // provides possible analyzers + input
        }

        return this._contextDetectors;
    }

    /**
     * Through the returned configuration object, the created analyzers can be accessed
     * - to initialize from the outside
     * - to be used by the detectors to access necessary information
     * @return
     */
    public ContextConfiguration getContextAnalyzerConfiguration()
    {
        return _analyzerConfig;
    }

    public void setContextConfiguration(ContextConfiguration config)
    {
        _analyzerConfig = config;
    }

    //@Todo Move each specifc initiation of objects needed in a refactoring generator to a seperate Factory
    //
    private void BuildRenameContextDetectors(EnumSet<CodeContext.CodeContextEnum> completeCodeContext)
    {
        // 1. setup the analyzers and add them to the generic config object.
        _analyzerConfig.setCMFAnalyzer(new ClassMethodFinder());
        _analyzerConfig.getCMFAnalyzer().initialize(_analyzerConfig.getCompilationUnit(), _analyzerConfig.getClassName());

        // 2. Create the relevant detectors and provided them with the generic config object
        UniversalBuildContextDetectors(completeCodeContext, _analyzerConfig);
    }

    //@Todo Move each specifc initiation of objects needed in a refactoring generator to a seperate Factory
    //
    private void BuildExtractMethodContextDetectors(EnumSet<CodeContext.CodeContextEnum> completeCodeContext)
    {
        // 1. setup the analyzers and add them to the generic config object.
        _analyzerConfig.setCMFAnalyzer(new ClassMethodFinder());
        _analyzerConfig.getCMFAnalyzer().initialize(_analyzerConfig.getCompilationUnit(), _analyzerConfig.getClassName());

        // Intra-method detectors need:
        // MethodDescriptor AST Javaparser, identifying method from which to extract
        // Code section to extract (this is sufficent to extract MethodDescirptor)
        // Class name where method is defined


        // 2. Create the relevant detectors and provided them with the generic config object
        UniversalBuildContextDetectors(completeCodeContext, _analyzerConfig);
    }

    /**
     * A set of context detectors is build based on the context set that is provided.
     * The @ContextConfiguration object is used to provide detectors with necessary input in a generic way
     *
     * @param completeCodeContext Context detectors that should be instantiated
     * @param analyzerConfig      Necessary input to properly initialize detectors and possible analyzers
     */
    private void UniversalBuildContextDetectors(EnumSet<CodeContext.CodeContextEnum> completeCodeContext,
                                                ContextConfiguration analyzerConfig)
    {
            if (ContextDetectorForAllContextDecisions(completeCodeContext)) {
                try {
                    for (CodeContext.CodeContextEnum context : completeCodeContext) {

                        if (!context.toString().contentEquals(CodeContext.CodeContextEnum.always_true.toString())) {

                            Class<?> classCtxt = Class.forName("analysis.context." + context.name());
                            Class<?> classConfig = Class.forName("analysis.context.ContextConfiguration");

                            Constructor<?> constructor = classCtxt.getConstructor(classConfig);

                            IContextDetector instance =
                                    (IContextDetector) constructor.newInstance(analyzerConfig);

                            _contextDetectors.add(instance);
                        }
                    }
                }
                catch(ClassNotFoundException cnfe)
                {
                    System.out.println(cnfe.getMessage());
                }
                catch(Exception e)
                {
                    System.out.println(e.getMessage());
                }
            }
        }


    /**
     * Internally used to validate if all requested detectors in the context set are actually
     * present as classes in our java project
     *
     * @param completeCodeContext
     * @return True, when all necessary classes could be found
     */
    private boolean ContextDetectorForAllContextDecisions(EnumSet<CodeContext.CodeContextEnum> completeCodeContext) {

        boolean allDefined = true;

        try {
            for (CodeContext.CodeContextEnum context : completeCodeContext) {
                if(!context.toString().contentEquals(CodeContext.CodeContextEnum.always_true.toString()))
                    Class.forName("analysis.context." + context.name());
            }
        }
        catch(ClassNotFoundException cnfe)
        {
            System.out.println("class " + cnfe.getMessage() + " not defined");
            allDefined = false;
        }

        return allDefined;
    }

    public void setConfiguration(ContextConfiguration configuration) {
        this._analyzerConfig = configuration;
    }
}
