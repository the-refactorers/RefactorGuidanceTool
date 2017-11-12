package analysis;

import analysis.ClassMethodFinder;
import com.github.javaparser.ast.CompilationUnit;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

public class ClassMethodFinderTest {

    private ResourceExampleClassParser _loader;
    CompilationUnit _cu;

    @Before
    public void Setup()
    {
         _loader = new ResourceExampleClassParser();
    }

    private void CreateCompilationUnitFromTestClass(String classTemplate)
    {
        _cu = _loader.Parse(classTemplate);
    }

    @Test
    public void GivenAClassRetrieveAllDefinedMethods()
    {
        CreateCompilationUnitFromTestClass("SimpleClassWith2Methods.java.txt");

        ClassMethodFinder cmf = new ClassMethodFinder(_cu, "TwoMethodClass");
        List<String> allMethods = cmf.getAllDefinedMethods();
        Assert.assertEquals(2, allMethods.size());
    }

    @Test
    public void GivenALocationInAClassDetermineIfLocatedInMethod()
    {
        CreateCompilationUnitFromTestClass("SimpleClassWith2Methods.java.txt");

        ClassMethodFinder cmf = new ClassMethodFinder(_cu, "TwoMethodClass");

        // Based on the comment lines in SimpleClassWith2Methods, saying in or outside class check if it meets
        // Lines outside method
        Assert.assertFalse(cmf.isLocationInMethod(3));
        Assert.assertFalse(cmf.isLocationInMethod(9));
        // Lines inside method
        Assert.assertTrue(cmf.isLocationInMethod(4));
        Assert.assertTrue(cmf.isLocationInMethod(6));
    }

    @Test
    public void GivenALocationInsideAMethodReturnNameOfMethod()
    {
        CreateCompilationUnitFromTestClass("SimpleClassWith2Methods.java.txt");
        ClassMethodFinder cmf = new ClassMethodFinder(_cu, "TwoMethodClass");

        Assert.assertEquals("MethodOne", cmf.getMethodNameForLocation(6));
        Assert.assertEquals("MethodTwo", cmf.getMethodNameForLocation(12));
    }

    @Test
    public void GivenALocationOutsideMethodScopeReturnsEmptyString()
    {
        CreateCompilationUnitFromTestClass("SimpleClassWith2Methods.java.txt");
        ClassMethodFinder cmf = new ClassMethodFinder(_cu, "TwoMethodClass");

        Assert.assertEquals("", cmf.getMethodNameForLocation(14));
    }

    @Test
    public void GivenAnExtendedClassQueringMethodDefintionsShouldReturnOfParentChild()
    {
        CreateCompilationUnitFromTestClass("ExtendedClassA_BWith2Methods.java.txt");
        ClassMethodFinder cmf = new ClassMethodFinder(_cu, "A");

        Assert.assertEquals("", cmf.getAllDefinedMethods());
    }

    @Test
    public void GivenMethodIfDefinedInInterfaceIsDetectedWhenAskedFor()
    {
        CreateCompilationUnitFromTestClass("ExtendedClassA_BWith2Methods.java.txt");
        ClassMethodFinder cmf = new ClassMethodFinder(_cu, "A");

        Assert.assertTrue(cmf.isMethodDefinedInInterface("MethodOne"));
    }
}
