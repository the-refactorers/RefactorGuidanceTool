package analysis;

import analysis.dataflow.LocalDeclaredVarsFinder;
import analysis.dataflow.LocalVariableWrittenMarker;
import analysis.dataflow.VariableFlowSet;
import analysis.dataflow.VariableFlowTable;
import com.github.javaparser.ast.body.MethodDeclaration;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

/**
 * Some tests to check if intra procedural dataflow algorithm detects read, write actions to variable
 * correct
 */
public class DataFlowMarkerTests extends JavaParserTestSetup {

    @Test
    public void variableFlowTableCreation()
    {
        CreateCompilationUnitFromTestClass("ExtractMethodZeroInZeroOut.java.txt");
        MethodDeclaration md = findMethodDeclarationInClass("ExtractMethodZeroInZeroOut", "MethodOneLocalDeclared");

        LocalDeclaredVarsFinder localVars = new LocalDeclaredVarsFinder(md);

        localVars.find();
        VariableFlowSet variableFlowTables = new VariableFlowSet(localVars.getLocalVars());

        List<VariableFlowTable> allVarFlowTables = variableFlowTables.getListOfVariableFlowTables();

        Assert.assertEquals(1, allVarFlowTables.size());
        if (allVarFlowTables.size() == 1)
            Assert.assertEquals(allVarFlowTables.get(0).name ,"a");
    }

    @Test
    public void unaryAssignmentTest()
    {
        // In the example test variable c is increased by c++

        CreateCompilationUnitFromTestClass("ExtractMethodMarkerCases.java.txt");
        MethodDeclaration md = findMethodDeclarationInClass("ExtractMethodMarkerCases", "WriteMarkers");

        LocalDeclaredVarsFinder localVars = new LocalDeclaredVarsFinder(md);

        localVars.find();
        VariableFlowSet varFlowTables = new VariableFlowSet(localVars.getLocalVars());

        LocalVariableWrittenMarker wMark = new LocalVariableWrittenMarker(md, varFlowTables);
        wMark.mark();

        VariableFlowSet dataFlowSet = wMark.getVariableFlowList();

        dataFlowSet.getListOfVariableFlowTables().forEach(flowTable ->
                {
                    if (flowTable.before_region.write)
                    {
                        System.out.println("Variable " + flowTable.name + " is WRITTEN");
                    }
                    else
                    {
                        System.out.println(flowTable.name);
                    }
                }
        );
    }

    @Test
    public void AssignmentTest()
    {

    }
}
