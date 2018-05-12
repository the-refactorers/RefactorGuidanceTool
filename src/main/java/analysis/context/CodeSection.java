package analysis.context;

public class CodeSection {

    private int _start, _end;

    public CodeSection(int start, int end)
    {
        this._start = start;
        this._end = end;
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
