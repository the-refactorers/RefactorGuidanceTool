package analysis.context;

import aig.CodeContext;
import com.github.javaparser.ast.CompilationUnit;

public class MethodExtractControlReturn extends MethodExtract {

    private final String _className;
    private final CompilationUnit _cu;

    public MethodExtractControlReturn(ContextConfiguration cc) {
        super(cc);

        _className = cc.getClassName();
        _cu = cc.getCompilationUnit();
    }

    @Override
    public boolean detect() throws Exception {
        return false;
    }

    @Override
    public CodeContext.CodeContextEnum getType() {
        return CodeContext.CodeContextEnum.MethodExtractControlReturn;
    }
}
