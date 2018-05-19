package analysis.MethodAnalyzer;

public class MethodDescriber {

    private String _name = new String();
    private String _signature = new String();
    private String _type = new String();

    public MethodDescriber()
    {

    }

    public MethodDescriber(String methodReturnType, String methodName, String methodParamSignature)
    {
        setName(methodName);
        setReturnType(methodReturnType);
        setSignature(methodName+methodParamSignature);
    }

    public void setName(String nameAsString) {
        _name = nameAsString;
    }

    public void setSignature(String s) {
        _signature = s;
    }

    public void setReturnType(String t) {
        _type = t;
    }

    public String getName() { return _name; }
    public String getSignature() {return _signature;}
    public String getType() { return _type;}

    public String fullTypeSignature() {
        return _type+" "+_signature;
    }
}
