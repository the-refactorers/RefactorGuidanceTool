package analysis.MethodAnalyzer;

import com.github.javaparser.ast.body.MethodDeclaration;

public class MethodDescriber {

    private String _name = new String();
    private String _signature = new String();
    private String _type = new String();
    private MethodDeclaration _declarationAST;

    public MethodDescriber()
    {

    }

    public MethodDescriber(String methodReturnType, String methodName, String methodParamSignature)
    {
        setName(methodName);
        setReturnType(methodReturnType);
        setSignature(methodName+methodParamSignature);
    }

    public MethodDescriber(MethodDeclaration item) {
        setReturnType(item.getType().asString());
        setName(item.getName().asString());
        setSignature(item.getSignature().asString());
        setMethodDeclaration(item);
    }

    public boolean equals(MethodDescriber md)
    {
        System.out.println(this.getType() + " " +
                this.getName() + " " + this.getSignature());
        System.out.println(md.getType() + " " +
                md.getName() + " " + md.getSignature());

        return (md.getType().contentEquals(this._type) &&
                md.getName().contentEquals(this._name) &&
                md.getSignature().contentEquals(this._signature));
    }

    private void setName(String nameAsString) {
        _name = nameAsString;
    }

    private void setSignature(String s) {
        _signature = s;
    }

    private void setReturnType(String t) {
        _type = t;
    }

    public String getName() { return _name; }
    public String getSignature() {return _signature;}
    public String getType() { return _type;}

    public String fullTypeSignature() {
        return _type+" "+_signature;
    }

    public MethodDeclaration getMethodDeclaration()
    {
        return _declarationAST;
    }

    public void setMethodDeclaration(MethodDeclaration mDecl)
    {
        _declarationAST = mDecl;
    }
}
