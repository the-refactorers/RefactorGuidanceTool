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