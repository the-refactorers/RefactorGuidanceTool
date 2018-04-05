package analysis.dataflow;

import com.github.javaparser.ast.body.MethodDeclaration;

public class LocalVariableWrittenMarker extends MarkVariableFlowList {

    LocalVariableWrittenMarker(MethodDeclaration md) {
        super(md);
    }
}
