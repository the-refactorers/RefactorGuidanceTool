package analysis.context;

import ait.CodeContext;
import com.github.javaparser.symbolsolver.model.declarations.ReferenceTypeDeclaration;
import com.github.javaparser.symbolsolver.model.typesystem.ReferenceType;

import java.util.List;

public class MethodOverrideNoAnnotation extends MethodOverride {

    @Override
    public boolean detect() throws Exception {

        boolean ovveride_exists = false;

        // Determine all classes/interfaces that are superseeding the class being analyzed
        ReferenceTypeDeclaration rtd = _analyzer.getReferenceTypeDeclarationOfClass();
        List<ReferenceType> rt = rtd.getAllAncestors();

        rt.forEach( ancestor ->
        {
            ReferenceTypeDeclaration rtd_ancestor = ancestor.getTypeDeclaration();

            if (!_analyzer.isIgnoredPackage(rtd_ancestor) && !ancestor.getTypeDeclaration().isInterface()) {
                // when ancestor is not a interface declaration, check if any method in super classes equals name of
                // provided method name
                rtd_ancestor.getDeclaredMethods().forEach(m ->
                {
                    if (m.getName().contentEquals(_methodName)) {
                        m.declaringType().getClass().getAnnotations();
                        addClassNameToClassList(m.declaringType().getQualifiedName());
                    }
                });
            }

        });

        return !classesFound.isEmpty();
    }

    @Override
    public CodeContext.CodeContextEnum getType() {
        return CodeContext.CodeContextEnum.MethodOverrideNoAnnotation;
    }
}
