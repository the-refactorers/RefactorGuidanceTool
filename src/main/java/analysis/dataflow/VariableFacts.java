package analysis.dataflow;


import javassist.compiler.ast.Variable;

import java.util.ArrayList;
import java.util.List;

public class VariableFacts {

        public boolean write;
        public List<Loc> written_at = new ArrayList<Loc>();

        public boolean read;
        public List<Loc> read_at = new ArrayList<Loc>();

        public boolean cond_write;

        public boolean live; // A variable is written which before only has been read in the section

        public boolean areAllFactsFalse() { return !write && !read && !cond_write && !live;}

        public class Loc {
                public int lineNumber;
                public int column;
                public int statementIndex;

                int getLineNumber(){ return lineNumber;}
        }
}
