package analysis.dataflow;

import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

import java.util.List;

/**
 * Responsibility of this class is to offer base functionality in marking
 * in what way variables are used in a provided AST method definition.
 */
public class MarkVariableFlowList extends VoidVisitorAdapter<Void>
    implements IVariableFlowInfoMarking
{
    private final MethodDeclaration _method;
    protected VariableFlowSet _lst = null;
    private int _start;
    private int _end;

    MarkVariableFlowList (MethodDeclaration md)
    {
        this._method = md;
    }

    MarkVariableFlowList (MethodDeclaration md, VariableFlowSet set)
    {

        this._method = md;
        this._lst = set;
    }

    public void mark()
    {
        this.visit(_method, null);
    }

    // Marking of the variables is done based on the location where a variable is involved.
    // This can be BEFORE, WITHIN or AFTER the piece of code that a user wants to extract.
    public boolean setExtractMethodRegion(int start, int end){

        boolean suc = false;

        if (end>start && start>=0 && end >= 1) {
            this._start = start;
            this._end = end;
            suc = true;
        }

        return suc;
    };

    public void setVariableFlowList(VariableFlowSet lst) {
        this._lst = lst;
    }

    public VariableFlowSet getVariableFlowList() {
        return this._lst;
    }
}
