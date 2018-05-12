package analysis.context;

public class CodeSection {

    private int _start, _end;

    public CodeSection(int start, int end)
    {
        this._start = start;
        this._end = end;
    }

    int begin()
    {
        return _start;
    }

    int end()
    {
        return _end;
    }
}
