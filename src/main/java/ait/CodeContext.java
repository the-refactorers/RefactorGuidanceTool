package ait;

import java.util.HashMap;
import java.util.Map;

public class CodeContext {

    public enum contextAction {
        listSameName
    }

    public enum CodeContextEnum {
        // Rename refactoring defined context
        MethodSingleDeclaration,
        MethodInterfaceDeclaration,
        MethodMultipleDeclarations,
        MethodOverload,
        MethodOverride,
        MethodOverrideNoAnnotation,

        // Extract method
        MethodExtractNoneLocalDependencies,
        MethodExtractSingleArgument,
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
}

