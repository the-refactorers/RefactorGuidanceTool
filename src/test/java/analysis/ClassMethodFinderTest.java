package analysis;

import analysis.MethodAnalyzer.ClassMethodFinder;
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

        ClassMethodFinder cmf = new ClassMethodFinder();
        cmf.initialize(_cu, "TwoMethodClass");

        List<String> allMethods = cmf.getAllDefinedMethods();
        Assert.assertEquals(2, allMethods.size());
    }

    @Test
    public void GivenALocationInAClassDetermineIfLocatedInMethod()
    {
        CreateCompilationUnitFromTestClass("SimpleClassWith2Methods.java.txt");

        ClassMethodFinder cmf = new ClassMethodFinder();
        cmf.initialize(_cu,"TwoMethodClass");

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
        ClassMethodFinder cmf = new ClassMethodFinder();
        cmf.initialize(_cu, "TwoMethodClass");

        Assert.assertEquals("MethodOne", cmf.getMethodNameForLocation(6));
        Assert.assertEquals("MethodTwo", cmf.getMethodNameForLocation(12));
    }

    @Test
    public void GivenALocationOutsideMethodScopeReturnsEmptyString()
    {
        CreateCompilationUnitFromTestClass("SimpleClassWith2Methods.java.txt");
        ClassMethodFinder cmf = new ClassMethodFinder();
        cmf.initialize(_cu, "TwoMethodClass");

        Assert.assertEquals("", cmf.getMethodNameForLocation(14));
    }

    @Test
    public void GivenClassSpecificMethodCanBeFound()
    {
        CreateCompilationUnitFromTestClass("SimpleClassWith2Methods.java.txt");
        ClassMethodFinder cmf = new ClassMethodFinder();
        cmf.initialize(_cu, "TwoMethodClass");

        Assert.assertEquals(true, cmf.hasMethodDefined("MethodTwo"));
    }

    @Test
    public void GivenInterfaceSpecificMethodCanBeFound()
    {
        CreateCompilationUnitFromTestClass("ExtendedClassA_BWith2Methods.java.txt");
        ClassMethodFinder cmf = new ClassMethodFinder();
        cmf.initialize(_cu, "D");

        Assert.assertEquals(true, cmf.hasMethodDefined("MethodFour"));
    }

    @Test
    public void GivenMethodIfDefinedInInterfaceIsDetectedWhenAskedFor()
    {
        CreateCompilationUnitFromTestClass("ExtendedClassA_BWith2Methods.java.txt");
        ClassMethodFinder cmf = new ClassMethodFinder();
        cmf.initialize(_cu, "A");

        Assert.assertTrue(cmf.isMethodDeclaredFirstTimeInInterface("MethodOne"));
    }

    @Test
    public void GivenMethodIsDetectedIfDefinedEarlierInSuperclass()
    {
        CreateCompilationUnitFromTestClass("ExtendedClassA_BWith2Methods.java.txt");
        ClassMethodFinder cmf = new ClassMethodFinder();
        cmf.initialize(_cu, "A");

        Assert.assertTrue(cmf.isMethodDefinedInSuperClass("MethodOne"));
    }

    @Test
    public void GivenMethodDeclaredLocalIsNotDetectedWhenTryingToFindInInterface()
    {
        CreateCompilationUnitFromTestClass("ExtendedClassA_BWith2Methods.java.txt");
        ClassMethodFinder cmf = new ClassMethodFinder();
        cmf.initialize(_cu, "A");

        Assert.assertFalse(cmf.isMethodDeclaredFirstTimeInInterface("MethodTwo"));
    }

    @Test
    public void GivenMethodDeclaredLocalIsNotDetectedWhenTryingToFindInSuperClass()
    {
        CreateCompilationUnitFromTestClass("ExtendedClassA_BWith2Methods.java.txt");
        ClassMethodFinder cmf = new ClassMethodFinder();
        cmf.initialize(_cu, "A");

        Assert.assertFalse(cmf.isMethodDefinedInSuperClass("MethodTwo"));
    }
}
