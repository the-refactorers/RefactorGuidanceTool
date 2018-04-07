package analysis.dataflow;

import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.Parameter;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.nodeTypes.NodeWithSimpleName;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Responsibility of this class is to determine which variables are declared local in
 * a provided MethodDeclaration object. declarations are those items in the AST
 * which have as property : VariableDeclarator
 */
public class LocalDeclaredVarsFinder extends VoidVisitorAdapter<Void> {

    private final MethodDeclaration _method;
    private List<String> _localVars = new ArrayList<String>();
    
    public LocalDeclaredVarsFinder(MethodDeclaration md)
    {
        this._method = md;
    }

    public void find()
    {
        this.visit(_method, null);
    }

    private void addToLocalVars(NodeWithSimpleName<Node> n)
    {
        this._localVars.add(n.getName().toString());
    }

    @Override
    public void visit(VariableDeclarator vd, Void arg) {
        super.visit(vd, arg);
        addToLocalVars((NodeWithSimpleName)vd);
    }

    @Override
    public void visit(Parameter p, Void arg) {
        super.visit(p, arg);
        addToLocalVars((NodeWithSimpleName)p);
    }
    
    public List<String> getLocalVars()
    {
        return this._localVars;
    }
}
