package analysis.dataflow;

import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Responsibility of this class is to find local declarations of variables in
 * a provided MethodDeclaration object. declarations are those items in the AST
 * which are marked as: VariableDeclarators
 */
public class LocalDeclaredVarsFinder extends VoidVisitorAdapter<Void> {

    private final MethodDeclaration _method;
    private List<String> _localVars = new ArrayList<String>();
    
    LocalDeclaredVarsFinder(MethodDeclaration md)
    {
        this._method = md;
    }

    public void find()
    {
        this.visit(_method, null);
    }

    @Override
    public void visit(VariableDeclarator vd, Void arg) {
        super.visit(vd, arg);
        System.out.println("LDVF Declaration:     " + vd.getName());

        this._localVars.add(vd.getName().toString());
    }
    
    public List<String> getLocalVars()
    {
        return this._localVars;
    }
}
