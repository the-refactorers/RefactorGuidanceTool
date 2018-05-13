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
        method_multiple_declares,
        method_defined_in_interface,
        method_overload_declaration,
        intramethod_extract_none_local_dependencies,
        intramethod_extract_no_argument,
        intramethod_extract_single_argument,
        intramethod_extract_multi_argument,
        intramethod_extract_no_result,
        intramethod_extract_multi_result,
        intramethod_extract_single_result,
        intramethod_extract_double_name_binding, // variable name is binded on different scopes
        intramethod_extract_flow_break,
        intramethod_extract_flow_return,
        always_true     // used to link multiple actions together, where no decision is required
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
        contextMap.put(CodeContextEnum.intramethod_extract_none_local_dependencies, "intramethod_extract_none_local_dependencies");
        contextMap.put(CodeContextEnum.intramethod_extract_single_argument,"intramethod_extract_single_argument");
        contextMap.put(CodeContextEnum.intramethod_extract_multi_argument, "intramethod_extract_multi_argument");
        contextMap.put(CodeContextEnum.intramethod_extract_no_argument,"intramethod_extract_no_argument");
        contextMap.put(CodeContextEnum.intramethod_extract_no_result,"intramethod_extract_no_result");
        contextMap.put(CodeContextEnum.intramethod_extract_single_result, "intramethod_extract_single_result");
        contextMap.put(CodeContextEnum.intramethod_extract_multi_result,"intramethod_extract_multi_result");
        contextMap.put(CodeContextEnum.intramethod_extract_double_name_binding, "intramethod_extract_double_name_binding");
        contextMap.put(CodeContextEnum.intramethod_extract_flow_break, "intramethod_extract_flow_break");
        contextMap.put(CodeContextEnum.intramethod_extract_flow_return, "intramethod_extract_flow_return");
    }
}
