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
            BuildRenameContextDetectors();
        }

        return this._contextDetectors;
    }

    private void BuildRenameContextDetectors() {
        try {
            Class<?> clazz = Class.forName("analysis.context.MethodSingleDeclaration");
            Class<?> ctor_p1 = Class.forName("analysis.MethodAnalyzer.ClassMethodFinder");

            Constructor<?> constructor = clazz.getConstructor(ctor_p1, String.class);

            ClassMethodFinder cmf = new ClassMethodFinder();
            IContextDetector instance = (IContextDetector) constructor.newInstance(ctor_p1.newInstance(), "methodA");

            // parameterinput should be retrieved from a configuration object
            // By placing it in a seperate object we can instantiate all context detectors from a configuration description
            // When running the actual algorithm for a specific refactoring
            // 1. set-up specific input object, with specific info methods for context detectors (known by them)
            // 2. analyze code
            // 3. run context detection
            // 4. generate

            _contextDetectors.add(instance);
        }
        catch(Exception E)
        {

        }
        finally{
        }
    }
}
