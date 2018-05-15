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
public class ContextDetectorBuilder {

    AdaptiveInstructionTree _ait = null;
    List<IContextDetector> _contextDetectors = new ArrayList<IContextDetector>();
    List<ICodeAnalyzer> _analyzers = new ArrayList<ICodeAnalyzer>();

    public void SetAIT() {

    }

    public void SetAIT(AdaptiveInstructionTree ait)  {
        setAIT(ait);
    }

    public void setAIT(AdaptiveInstructionTree ait) {
        this._ait = ait;
    }

    public List<IContextDetector> getContextDetectors()
    {
        EnumSet<CodeContext.CodeContextEnum> completeCodeContext = this._ait.allUniqueCodeContextInTree();
        String refactoringProcessName = this._ait.getRefactorMechanic();

        if(refactoringProcessName.contentEquals("Rename Method"))
        {
            BuildRenameContextDetectors(completeCodeContext);
        }

        return this._contextDetectors;
    }

    private void BuildRenameContextDetectors(EnumSet<CodeContext.CodeContextEnum> completeCodeContext)
    {
            if (ContextDetectorForAllContextDecisions(completeCodeContext)) {
                try {

                for (CodeContext.CodeContextEnum context : completeCodeContext) {

                    if (!context.toString().contentEquals(CodeContext.CodeContextEnum.always_true.toString())) {

                        Class<?> classCtxt = Class.forName("analysis.context." + context.name());
                        Class<?> classAnalyzer = Class.forName("analysis.MethodAnalyzer.ClassMethodFinder");

                        Constructor<?> constructor = classCtxt.getConstructor(classAnalyzer, String.class);

                        IContextDetector instance =
                                (IContextDetector) constructor.newInstance(classAnalyzer.newInstance(), "methodA");

                        // parameterinput should be retrieved from a configuration object
                        // By placing it in a seperate object we can instantiate all context detectors from a configuration description
                        // When running the actual algorithm for a specific refactoring
                        // 1. set-up specific input object, with specific info methods for context detectors (known by them)
                        // 2. analyze code
                        // 3. run context detection
                        // 4. generate

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
}
