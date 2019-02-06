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

import com.github.javaparser.ast.body.MethodDeclaration;

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
        if(!getVariableFlowList().areAllSectionsInTableSetFalse())
        {
            getVariableFlowList().getListOfVariableFlowTables().forEach(flowTable ->
            {
                if(!flowTable.allFactsInRegionMarkedFalse())
                {
                    detectLiveVariables(flowTable.before_region);
                    detectLiveVariables(flowTable.within_region);
                    detectLiveVariables(flowTable.after_region);
                }
            });
        }
    }

    private void detectLiveVariables(VariableFacts regionFacts) {

        // Only possible if a write has been performed on variable in region, otherwise it is treated live
        // because when being read
        if(regionFacts.write && regionFacts.read)
        {
            int firstReadAt = regionFacts.read_at.get(0).lineNumber;
            for(VariableFacts.Loc location : regionFacts.read_at)
            {
                if (location.lineNumber < firstReadAt)
                    firstReadAt = location.lineNumber;
            }

            int firstWriteAt = regionFacts.written_at.get(0).lineNumber;
            for(VariableFacts.Loc location : regionFacts.written_at)
            {
                if (location.lineNumber < firstWriteAt)
                    firstWriteAt = location.lineNumber;
            }

            if(firstWriteAt >= firstReadAt)
                regionFacts.live = true;
        }
        else if(regionFacts.read && !regionFacts.write)
        {
            regionFacts.live = true;
        }
    }
}