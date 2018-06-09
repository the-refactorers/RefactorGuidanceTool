package analysis;

import analysis.MethodAnalyzer.ClassMethodFinder;
import analysis.MethodAnalyzer.MethodDescriber;
import com.github.javaparser.ast.CompilationUnit;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static junit.framework.TestCase.fail;

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

        Assert.assertEquals("MethodOne", cmf.getMethodDescriberForLocation(6).getName());
        Assert.assertEquals("MethodTwo", cmf.getMethodDescriberForLocation(12).getName());
    }

    @Test
    public void GivenALocationOutsideMethodScopeReturnsEmptyString()
    {
        CreateCompilationUnitFromTestClass("SimpleClassWith2Methods.java.txt");
        ClassMethodFinder cmf = new ClassMethodFinder();
        cmf.initialize(_cu, "TwoMethodClass");

        Assert.assertEquals("", cmf.getMethodDescriberForLocation(14).getName());
    }

    @Test
    public void GivenClassSpecificMethodCanBeFound()
    {
        CreateCompilationUnitFromTestClass("SimpleClassWith2Methods.java.txt");
        ClassMethodFinder cmf = new ClassMethodFinder();
        cmf.initialize(_cu, "TwoMethodClass");

        MethodDescriber method = new MethodDescriber("void","MethodTwo","()");

        Assert.assertEquals(true, cmf.hasMethodDefined(method));
    }

    @Test
    public void GivenInterfaceSpecificMethodCanBeFound()
    {
        CreateCompilationUnitFromTestClass("ExtendedClassA_BWith2Methods.java.txt");
        ClassMethodFinder cmf = new ClassMethodFinder();
        cmf.initialize(_cu, "D");

        MethodDescriber method = new MethodDescriber("void","MethodFour","()");
        Assert.assertEquals(true, cmf.hasMethodDefined(method));
    }

    @Test
    public void GivenMethodIfDefinedInInterfaceIsDetectedWhenAskedFor()
    {
        CreateCompilationUnitFromTestClass("ExtendedClassA_BWith2Methods.java.txt");
        ClassMethodFinder cmf = new ClassMethodFinder();
        cmf.initialize(_cu, "A");

        try {
            MethodDescriber method = new MethodDescriber("void","MethodOne","()");
            Assert.assertTrue(cmf.isMethodDeclaredFirstTimeInInterface(method));
        }
        catch(Exception e)
        {
            fail(e.getMessage());
        }
    }

    @Test
    public void GivenMethodIsDetectedIfDefinedEarlierInSuperclass()
    {
        CreateCompilationUnitFromTestClass("ExtendedClassA_BWith2Methods.java.txt");
        ClassMethodFinder cmf = new ClassMethodFinder();
        cmf.initialize(_cu, "A");

        try
        {
            MethodDescriber method = new MethodDescriber("void","MethodOne","()");
            Assert.assertTrue(cmf.isMethodDefinedInSuperClass(method));
        }
        catch(Exception e)
        {
            fail(e.getMessage());
        }
    }

    @Test
    public void GivenMethodDeclaredLocalIsNotDetectedWhenTryingToFindInInterface()
    {
        CreateCompilationUnitFromTestClass("ExtendedClassA_BWith2Methods.java.txt");
        ClassMethodFinder cmf = new ClassMethodFinder();
        cmf.initialize(_cu, "A");

        try {
            MethodDescriber method = new MethodDescriber("void","MethodTwo","()");
            Assert.assertFalse(cmf.isMethodDeclaredFirstTimeInInterface(method));
        }
        catch(Exception e)
        {
            fail(e.getMessage());
        }

    }

    @Test
    public void GivenMethodDeclaredLocalIsNotDetectedWhenTryingToFindInSuperClass()
    {
        CreateCompilationUnitFromTestClass("ExtendedClassA_BWith2Methods.java.txt");
        ClassMethodFinder cmf = new ClassMethodFinder();
        cmf.initialize(_cu, "A");

        try
        {
            MethodDescriber method = new MethodDescriber("void","MethodTwo","()");
            Assert.assertFalse(cmf.isMethodDefinedInSuperClass(method));
        }
        catch(Exception e)
        {
            fail(e.getMessage());
        }
    }

    @Test
    public void GivenMethodDlistsalldeclarations()
    {
        CreateCompilationUnitFromTestClass("ExtendedClassA_BWith2Methods.java.txt");
        ClassMethodFinder cmf = new ClassMethodFinder();
        cmf.initialize(_cu, "A");

        try
        {
            List<String> lst = cmf.getAllDefinedMethods();
            for(String item : lst)
                System.out.println(item);
            Assert.assertEquals(4, lst.size());
        }
        catch(Exception e)
        {
            fail(e.getMessage());
        }
    }
}
