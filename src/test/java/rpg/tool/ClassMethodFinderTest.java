package rpg.tool;

import com.github.javaparser.ast.CompilationUnit;
import org.junit.Assert;
import org.junit.Test;

public class ClassMethodFinderTest {

    @Test
    public void GivenAClassRetrieveAllMethods()
    {
        ResourceExampleClassParser loader = new ResourceExampleClassParser();
        CompilationUnit cu = loader.Parse("SimpleClassWith2Methods.java.txt");

        ClassMethodFinder cmf = new ClassMethodFinder(cu);
        cmf.ListAllMethods();
    }
}