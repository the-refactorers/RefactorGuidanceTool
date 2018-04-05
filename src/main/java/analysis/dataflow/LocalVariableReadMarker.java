package analysis.dataflow;

import com.github.javaparser.ast.body.MethodDeclaration;

public class LocalVariableReadMarker extends MarkVariableFlowList {

    LocalVariableReadMarker(MethodDeclaration md) {
        super(md);
    }

}
