package analysis;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.expr.NameExpr;
import com.github.javaparser.ast.expr.VariableDeclarationExpr;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import com.github.javaparser.symbolsolver.javaparser.Navigator;

import java.io.FileInputStream;

public class MethodDataFlowAnalysis {

        public static void main(String[] args) throws Exception {

            // Parse the AST based on the input of SymbolFoo.java
            //System.out.println(System.getProperty("user.dir"));
            CompilationUnit cu = JavaParser.parse(new FileInputStream("RefactorScenarios/ExtractMethod/DataFlowExample.java"));

            // Navigator is used to navigate through the AST with some basic commands
            MethodDeclaration md = Navigator.demandMethod(Navigator.demandClass(cu, "DataFlowExample"), "simpleSequence");

            // Find an variable declaration in the given node structure
//        VariableDeclarationExpr varDeclaration = Navigator.findNodeOfGivenClass(cu, VariableDeclarationExpr.class);
//        System.out.println(varDeclaration);

            IntraMethodVariableAnalysis declarationVisitor = new IntraMethodVariableAnalysis();
            declarationVisitor.setExtractMethodRange(8,10);

            declarationVisitor.visit(md, null);

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
        }

        private static class IntraMethodVariableAnalysis extends VoidVisitorAdapter<Void>
        {
            int _start = 0;
            int _end = 0;

            public void setExtractMethodRange(int start, int end)
            {
                _start = start;
                _end = end;
            }

            @Override
            public void visit(VariableDeclarationExpr vd, Void arg)
            {
                super.visit(vd, arg);
                System.out.println("Declaration:     " + vd);
            }

            @Override
            public void visit(NameExpr sn, Void arg)
            {
                super.visit(sn,arg);

                Node t = sn.getParentNode().get();
                Node t2 = t.getParentNode().get();

                int varLine = sn.getRange().get().begin.line;

                System.out.println("Variable >> " + sn + " << is used @ location " + varLine);

                if (varLine <= _end && varLine >= _start)
                {
                    System.out.println("===== This variable is within extract method section");
                }
            }
        }
    }
