package analysis.dataflow;

import com.github.javaparser.ast.body.MethodDeclaration;

public class LocalVariableLiveMarker extends MarkVariableFlowList {

    LocalVariableLiveMarker(MethodDeclaration md) {
        super(md);
    }
}