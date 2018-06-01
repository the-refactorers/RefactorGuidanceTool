package analysis.context;

public class CodeSection {

    private int _start = -1;
    private int _end = -1;

    public CodeSection(int start, int end)
    {
        this._start = start;
        this._end = end;
    }

    public boolean notDefined() {
        return ((_start == -1) || (_end == -1));
    }

    public int begin()
    {
        return _start;
    }

    public int end()
    {
        return _end;
    }
}
