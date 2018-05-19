package analysis.context;

import ait.CodeContext;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.expr.AnnotationExpr;
import com.github.javaparser.symbolsolver.javaparser.Navigator;

import java.util.List;

public class MethodOverrideNoAnnotation extends MethodOverride {

    public MethodOverrideNoAnnotation(ContextConfiguration cc) {
        super(cc);
    }

    @Override
    public boolean detect() throws Exception {

        boolean no_annotation = false;

        if(super.detect())
        {
            ClassOrInterfaceDeclaration class4Analysis = Navigator.demandClassOrInterface(
                    _analyzer.getCompilationUnit(),
                    _analyzer.getQualifiedClassName());

            List<MethodDeclaration> md = class4Analysis.getMethods();
            for(MethodDeclaration item : md)
            {
                if (item.getName().toString().contentEquals(_methodName))
                {
                    NodeList<AnnotationExpr> annotations = item.getAnnotations();
                    if (!annotations.isEmpty())
                    {
                        for(AnnotationExpr ea : annotations)
                        {
                            System.out.println(ea.getName().asString());
                        }
                    }
                }
            }

            NodeList<AnnotationExpr> ae =  md.get(0).getAnnotations();
            if(ae.isEmpty()) no_annotation = true;
        }

        return no_annotation;
    }

    @Override
    public CodeContext.CodeContextEnum getType() {
        return CodeContext.CodeContextEnum.MethodOverrideNoAnnotation;
    }
}
