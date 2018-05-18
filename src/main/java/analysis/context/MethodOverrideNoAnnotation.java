package analysis.context;

import ait.CodeContext;

public class MethodOverrideNoAnnotation extends MethodOverride {

    @Override
    public boolean detect() throws Exception {
        return false;
    }

    @Override
    public CodeContext.CodeContextEnum getType() {
        return CodeContext.CodeContextEnum.MethodOverrideNoAnnotation;
    }
}
