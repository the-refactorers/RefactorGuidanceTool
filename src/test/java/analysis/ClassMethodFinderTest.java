package analysis;

import analysis.ClassMethodFinder;
import com.github.javaparser.ast.CompilationUnit;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

public class ClassMethodFinderTest {

    @Test
    public void GivenAClassRetrieveAllDefinedMethods()
    {
        ResourceExampleClassParser loader = new ResourceExampleClassParser();
        CompilationUnit cu = loader.Parse("SimpleClassWith2Methods.java.txt");

        ClassMethodFinder cmf = new ClassMethodFinder(cu, "TwoMethodClass");
        List<String> allMethods = cmf.getAllDefinedMethods();
        Assert.assertEquals(2, allMethods.size());
    }

    @Test
    public void GivenALocationInAClassDetermineIfLocatedInMethod()
    {
        ResourceExampleClassParser loader = new ResourceExampleClassParser();
        CompilationUnit cu = loader.Parse("SimpleClassWith2Methods.java.txt");

        ClassMethodFinder cmf = new ClassMethodFinder(cu, "TwoMethodClass");

        // Based on the comment lines in SimpleClassWith2Methods, saying in or outside class check if it meets
        // Lines outside method
        Assert.assertFalse(cmf.isLocationInMethod(3));
        Assert.assertFalse(cmf.isLocationInMethod(9));
        // Lines inside method
        Assert.assertFalse(cmf.isLocationInMethod(4));
        Assert.assertFalse(cmf.isLocationInMethod(6));
    }

    @Test
    public void GivenALocationInsideAMethodReturnNameOfMethod()
    {
        ResourceExampleClassParser loader = new ResourceExampleClassParser();
        CompilationUnit cu = loader.Parse("SimpleClassWith2Methods.java.txt");

        ClassMethodFinder cmf = new ClassMethodFinder(cu, "TwoMethodClass");

        Assert.assertEquals("MethodOne", cmf.getMethodNameForLocation(6));
        Assert.assertEquals("MethodTwo", cmf.getMethodNameForLocation(12));
    }
}
