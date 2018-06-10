package analysis.context;
import aig.CodeContext;

/**
 * This class is the inverse of MethodExtractNameHiding
 */
public class MethodExtractNoControlReturn extends MethodExtract {

    MethodExtractControlReturn _mecr;

    public MethodExtractNoControlReturn(ContextConfiguration cc) {
        super(cc);

        _mecr = new MethodExtractControlReturn(cc);
    }

    @Override
    public boolean detect() throws Exception {

        return !_mecr.detect();
    }

    @Override
    public CodeContext.CodeContextEnum getType() {
        return CodeContext.CodeContextEnum.MethodExtractNoControlReturn;
    }

}
