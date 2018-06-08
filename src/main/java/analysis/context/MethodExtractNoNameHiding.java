package analysis.context;

import aig.CodeContext;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.symbolsolver.javaparser.Navigator;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * This class is the inverse of MethodExtractNameHiding
 */
public class MethodExtractNoNameHiding extends MethodExtract {

    MethodExtractNameHiding _menh;

    public MethodExtractNoNameHiding(ContextConfiguration cc) {
        super(cc);

        _menh = new MethodExtractNameHiding(cc);
    }

    @Override
    public boolean detect() throws Exception {

        return !_menh.detect();
    }

    @Override
    public CodeContext.CodeContextEnum getType() {
        return CodeContext.CodeContextEnum.MethodExtractNoNameHiding;
    }

}
