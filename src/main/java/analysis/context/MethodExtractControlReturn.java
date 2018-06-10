package analysis.context;

import aig.CodeContext;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.expr.NameExpr;
import com.github.javaparser.ast.stmt.ReturnStmt;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import helpers.Checker;

public class MethodExtractControlReturn extends MethodExtract {

    private final String _className;
    private final CompilationUnit _cu;
    private final ContextConfiguration _cc;

    public MethodExtractControlReturn(ContextConfiguration cc) {
        super(cc);

        _className = cc.getClassName();
        this._cu = cc.getCompilationUnit();
        this._cc = cc;
    }

    @Override
    public boolean detect() throws Exception {

        ReturnFinder rf = new ReturnFinder(_cc.getCodeSection());
        MethodDeclaration md = _cc.getCMFAnalyzer().getMethodDeclarationForLocation(_cc.getCodeSection().begin());
        rf.visit(md, null);

        return rf.getResult();
    }

    @Override
    public CodeContext.CodeContextEnum getType() {
        return CodeContext.CodeContextEnum.MethodExtractControlReturn;
    }

    /**
     * Visitor scans provided method for presence of return statement in extracted code section
     *
     */
    private class ReturnFinder extends VoidVisitorAdapter<Void>
    {
        boolean returnDetected = false;
        CodeSection _cs;

        public ReturnFinder(CodeSection cs)
        {
            this._cs = cs;
        }

        public boolean getResult()
        {
            return returnDetected;
        }

        @Override
        public void visit(ReturnStmt rs, Void args)
        {
            if(Checker.inRangeInclusive(_cs.begin(), _cs.end(), rs.getBegin().get().line))
                returnDetected = true;
        }
    }
}
