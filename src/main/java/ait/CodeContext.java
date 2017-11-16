package ait;

import java.util.HashMap;
import java.util.Map;

public class CodeContext {

    public enum contextAction
    {
        listSameName
    }

    public enum CodeContextEnum {
        method_single_declaration,
        method_override,
        method_has_limited_references,
        method_has_many_references,
        method_multiple_declares, method_defined_in_interface
    }

    public static final Map<CodeContextEnum, String> contextMap;

    static
    {
        contextMap = new HashMap<CodeContextEnum, String>();
        contextMap.put(CodeContextEnum.method_defined_in_interface, "method-defined-in-interface");
        contextMap.put(CodeContextEnum.method_single_declaration, "method_single_declaration");
        contextMap.put(CodeContextEnum.method_override, "method_override");
        contextMap.put(CodeContextEnum.method_has_many_references, "method_has_many_references");
        contextMap.put(CodeContextEnum.method_has_limited_references, "method_has_limited_references");
    }

}
