package analysis.dataflow;

import com.github.javaparser.Range;
import com.github.javaparser.ast.body.MethodDeclaration;

import java.util.Optional;

public class LocalVariableLiveMarker extends MarkVariableFlowList {

    LocalVariableLiveMarker(MethodDeclaration md) {
        super(md);
    }

    public LocalVariableLiveMarker(MethodDeclaration md, VariableFlowSet variableFlowSet) {
        super(md, variableFlowSet);
    }

    @Override
    public void mark()
    {
        // Live marking will mark those variables that were read for the first time before any write.
        // For this it is using the read and written sections already present in the variableFlowSet

        // When all fields false in a section, no need to proceed (it is neither read/written or maybe no
        // read/write analysis has been performed


    }
}