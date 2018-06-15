/**
 *  This file is part of RefactorGuidance project. Which explores possibilities to generate context based
 *  instructions on how to refactor a piece of Java code. This applied in an education setting (bachelor SE students)
 *
 *      Copyright (C) 2018, Patrick de Beer, p.debeer@fontys.nl
 *
 *          This program is free software: you can redistribute it and/or modify
 *          it under the terms of the GNU General Public License as published by
 *          the Free Software Foundation, either version 3 of the License, or
 *          (at your option) any later version.
 *
 *          This program is distributed in the hope that it will be useful,
 *          but WITHOUT ANY WARRANTY; without even the implied warranty of
 *          MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *          GNU General Public License for more details.
 *
 *          You should have received a copy of the GNU General Public License
 *          along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
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

    // TODO: 11-5-2018  - refactor this code
    public void MarkFlowTable(VariableFlowTable flowTable, E_ACTION action, VariableFacts.Loc location)
    {
        if (rangeNotSet())
        {
            if(action == E_ACTION.write)
            {
                setWithinWrite(flowTable, location);
            }
            else
            {
                flowTable.within_region.read = true;
                flowTable.within_region.read_at.add(location);
            }
        }
        else
        if(isLocationBeforeExtractedSection(location.lineNumber))
        {
            if(action == E_ACTION.write)
            {
                flowTable.before_region.write = true;
                flowTable.before_region.written_at.add(location);
            }
            else
            {
                flowTable.before_region.read = true;
                flowTable.before_region.read_at.add(location);
            }
        }
        else if (isLocationAfterExtractedSection(location.lineNumber))
        {
            if(action == E_ACTION.write)
            {
                flowTable.after_region.write = true;
                flowTable.after_region.written_at.add(location);
            }
            else
            {
                flowTable.after_region.read = true;
                flowTable.after_region.read_at.add(location);
            }
        }
        else if (isLocationInExtractedSection(location.lineNumber))
        {
            if(action == E_ACTION.write)
            {
                setWithinWrite(flowTable, location);
            }
            else
            {
                flowTable.within_region.read = true;
                flowTable.within_region.read_at.add(location);
            }
        }
    }

    private void setWithinWrite(VariableFlowTable flowTable, VariableFacts.Loc location) {
        flowTable.within_region.write = true;
        flowTable.within_region.written_at.add(location);
    }



    public void setVariableFlowList(VariableFlowSet lst) {
        this._lst = lst;
    }

    public VariableFlowSet getVariableFlowList() {
        return this._lst;
    }

    protected VariableFacts.Loc toLoc(Optional<Range> r) {
        VariableFacts fact = new VariableFacts();
        VariableFacts.Loc locationInfo = fact.new Loc();
        locationInfo.lineNumber = r.get().begin.line;
        locationInfo.column = r.get().begin.column;

        return locationInfo;
    }

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
