package analysis.dataflow;

import com.github.javaparser.Range;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

import java.util.List;
import java.util.Optional;

/**
 * Responsibility of this class is to offer base functionality in marking
 * in what way variables are used in a provided AST method definition.
 */
public class MarkVariableFlowList extends VoidVisitorAdapter<Void>
    implements IVariableFlowInfoMarking
{
    private final MethodDeclaration _method;
    protected VariableFlowSet _lst = null;

    private int _start = -1;
    private int _end = -1;

    public enum E_ACTION
    {
        write,
        read
    }

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

        if (isValidExtractionRegion(start, end)) {
            this._start = start;
            this._end = end;
            suc = true;
        }
        else
        {
            this._start = -1;
            this._end = -1;
        }

        return suc;
    }

    public void MarkFlowTable(VariableFlowTable flowTable, E_ACTION action, int location)
    {
        if (rangeNotSet())
        {
            if(action == E_ACTION.write)
            {
                flowTable.within_region.write = true;
            }
            else
            {
                flowTable.within_region.read = true;
            }
        }

        if(isLocationBeforeExtractedSection(location))
        {
            if(action == E_ACTION.write)
            {
                flowTable.before_region.write = true;
            }
            else
            {
                flowTable.before_region.read = true;
            }
        }
        else if (isLocationAfterExtractedSection(location))
        {
            if(action == E_ACTION.write)
            {
                flowTable.after_region.write = true;
            }
            else
            {
                flowTable.after_region.read = true;
            }
        }
        else if (isLocationInExtractedSection(location))
        {
            if(action == E_ACTION.write)
            {
                flowTable.within_region.write = true;
            }
            else
            {
                flowTable.within_region.read = true;
            }
        }
    }

    public void setVariableFlowList(VariableFlowSet lst) {
        this._lst = lst;
    }

    public VariableFlowSet getVariableFlowList() {
        return this._lst;
    }

    protected int startLine(Optional<Range> r) { return r.get().begin.line; }

    private boolean isLocationBeforeExtractedSection(int varLocation) {
        return varLocation < _start;
    }

    private boolean isLocationAfterExtractedSection(int varLocation) {
        return varLocation > _end;
    }

    private boolean isLocationInExtractedSection(int varLine) {
        return varLine <= _end && varLine >= _start;
    }

    private boolean rangeNotSet() {
        return (this._start == -1 && this._end == -1);
    }

    private boolean isValidExtractionRegion(int start, int end) {
        return end>start && start>=0 && end >= 1;
    }
}
