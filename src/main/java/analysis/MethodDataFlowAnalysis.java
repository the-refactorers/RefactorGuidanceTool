/**
Copyright (C) 2018, Patrick de Beer

        This program is free software: you can redistribute it and/or modify
        it under the terms of the GNU General Public License as published by
        the Free Software Foundation, either version 3 of the License, or
        (at your option) any later version.

        This program is distributed in the hope that it will be useful,
        but WITHOUT ANY WARRANTY; without even the implied warranty of
        MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
        GNU General Public License for more details.

        You should have received a copy of the GNU General Public License
        along with this program.  If not, see <http://www.gnu.org/licenses/>.
 **/
package analysis;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.expr.NameExpr;
import com.github.javaparser.ast.expr.UnaryExpr;
import com.github.javaparser.ast.expr.VariableDeclarationExpr;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import com.github.javaparser.symbolsolver.javaparser.Navigator;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

public class MethodDataFlowAnalysis {

        public  static void main(String[] args) throws Exception {

            // Parse the AST based on the input of SymbolFoo.java
            //System.out.println(System.getProperty("user.dir"));
            CompilationUnit cu = JavaParser.parse(new FileInputStream("RefactorScenarios/ExtractMethod/DataflowPreservation/DataFlowExample.java"));

            // Navigator is used to navigate through the AST with some basic commands
            MethodDeclaration md = Navigator.demandMethod(Navigator.demandClass(cu, "DataFlowExample"), "simpleSequence");

            // Find an variable declaration in the given node structure
//        VariableDeclarationExpr varDeclaration = Navigator.findNodeOfGivenClass(cu, VariableDeclarationExpr.class);
//        System.out.println(varDeclaration);

            IntraMethodVariableAnalysis declarationVisitor;
            declarationVisitor = new IntraMethodVariableAnalysis();
            declarationVisitor.setExtractMethodRange(40,45);

            declarationVisitor.visit(md, null);

            LocalDeclaredVarsFinder localVars = new LocalDeclaredVarsFinder(md);
            localVars.find();

           // UnaryExpr;

            /**
             * For our first sequential simpel case the strategy can be
             *
             * 1. Find all VariableDeclarionExpr. For each expression determine @ which location in the code it is used
             *    and if the variable is read or written
             * 2. Cycle through list of variables and mark those that are within/before/after the extract method sections
             * 3. Perform logic to validate if parameters in section should be passed on or returned
             * 4. If more than one returned; mark this special case
             *
             * When this part works, we can have a look by introducing new statemtns as
             * 1. if-then-else; break and return statements; try...catch; continue;
             */

            System.out.println("Vars used BEFORE section to be extracted:");

            for(VariableFlowTable vi : IntraMethodVariableAnalysis.dataFlowMethodVariables)
            {
                VariableFacts beforeFacts = vi.before_region;

                if(beforeFacts.cond_write || beforeFacts.live || beforeFacts.read || beforeFacts.write)
                    System.out.println("Variable: "+ vi.name);
            }

            System.out.println("Vars used WITHIN section to be extracted:");

            for(VariableFlowTable vi : IntraMethodVariableAnalysis.dataFlowMethodVariables)
            {
                VariableFacts withinFacts = vi.within_region;

                if(withinFacts.cond_write || withinFacts.live || withinFacts.read || withinFacts.write)
                        System.out.println("Variable: "+ vi.name);
            }

            System.out.println("Vars used AFTER section to be extracted:");

            for(VariableFlowTable vi : IntraMethodVariableAnalysis.dataFlowMethodVariables)
            {
                VariableFacts withinFacts = vi.after_region;

                if(withinFacts.cond_write || withinFacts.live || withinFacts.read || withinFacts.write)
                    System.out.println("Variable: "+ vi.name);
            }
        }

        private  static class IntraMethodVariableAnalysis extends VoidVisitorAdapter<Void>
        {
           int _start = 0;
            int _end = 0;

             static List<VariableFlowTable> dataFlowMethodVariables  = new ArrayList<VariableFlowTable>();;

            public void setExtractMethodRange(int start, int end)
            {
                _start = start;
                _end = end;
            }

            /**
             * Visit those nodes where a variable is declared for the first time.
             * We can see this as place where a value is 'written' to the variable
             *
             * @param vd
             * @param arg
             */
            @Override
            public void visit(VariableDeclarator vd, Void arg)
            {
                super.visit(vd, arg);
                System.out.println("Declaration:     " + vd);

                int varLocation = vd.getRange().get().begin.line;

                if(isLocationBeforeExtractedSection(varLocation))
                {
                    System.out.println("===== This variable is BEFORE extract method section");
                    addNewVariablesToVariableFLowList(vd.getNameAsString(), dataFlowMethodVariables);
                    markVariableFact(vd.getNameAsString(), dataFlowMethodVariables, 0, 1);
                }

                if (isLocationInExtractedSection(varLocation))
                {
                    System.out.println("===== This variable is WITHIN extract method section");
                    addNewVariablesToVariableFLowList(vd.getNameAsString(), dataFlowMethodVariables);
                    markVariableFact(vd.getNameAsString(), dataFlowMethodVariables, 1, 1);
                }

                if(isLocationAfterExtractedSection(varLocation))
                {
                    System.out.println("===== This variable is AFTER extract method section");
                    addNewVariablesToVariableFLowList(vd.getNameAsString(), dataFlowMethodVariables);
                    markVariableFact(vd.getNameAsString(), dataFlowMethodVariables, 2, 1);
                }
            }

            @Override
            public void visit(NameExpr sn, Void arg)
            {
                super.visit(sn,arg);

                Node t = sn.getParentNode().get();
                Node t2 = t.getParentNode().get();

                int varLocation = sn.getRange().get().begin.line;

                System.out.println("Variable >> " + sn + " << is used @ location " + varLocation);

//                if(isLocationBeforeExtractedSection(varLocation))
//                {
//                    System.out.println("===== This variable is BEFORE extract method section");
//                    addNewVariablesToVariableFLowList(sn.getNameAsString(), before);
//                }
//
//                if (isLocationInExtractedSection(varLocation))
//                {
//                    System.out.println("===== This variable is WITHIN extract method section");
//                    addNewVariablesToVariableFLowList(sn.getNameAsString(), within);
//                }
//
//                if(isLocationAfterExtractedSection(varLocation))
//                {
//                    System.out.println("===== This variable is AFTER extract method section");
//                    addNewVariablesToVariableFLowList(sn.getNameAsString(), after);
//                }
            }

            private void addNewVariablesToVariableFLowList(String varName, List<VariableFlowTable> lst) {
                boolean variableAlreadyAdded = isVariableAlreadyAdded(varName, lst);

                if (!variableAlreadyAdded)
                {
                    lst.add(new VariableFlowTable(varName));
                }
            }

            // region: 0=before;1=within;2=after
            // fact: 0=read;1=write;2=cond. write;3=live
            private void markVariableFact(String varName, List<VariableFlowTable> lst, int region, int fact)
            {
                if(region == 0)
                {
                    for (VariableFlowTable vi : lst) {
                        if(vi.name.contains(varName)) {
                            VariableFacts withinFacts = vi.before_region;
                            withinFacts.write = true;
                            vi.before_region = withinFacts;
                            break;
                        }
                    }
                }
            }

            private boolean isVariableAlreadyAdded(String varName, List<VariableFlowTable> lst) {
                boolean variableAlreadyAdded = false;
                for (VariableFlowTable vi : lst) {
                    if(vi.name.contains(varName)) {
                        variableAlreadyAdded = true;
                        break;
                    }
                }
                return variableAlreadyAdded;
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
        }
    }
