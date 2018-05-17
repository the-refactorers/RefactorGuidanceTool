package context;

import analysis.JavaParserTestSetup;
import analysis.MethodAnalyzer.ClassMethodFinder;
import analysis.context.ContextConfiguration;
import analysis.context.MethodInterfaceDeclaration;
import analysis.context.MethodMultipleDeclarations;
import analysis.context.MethodSingleDeclaration;
import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.fail;

/**
 *
 */
public class MethodDeclContextTests extends JavaParserTestSetup {

    @Test(expected = Exception.class)
    public void detectionOfNoneExistingMethodsFails() throws Exception {
        CreateCompilationUnitFromTestClass("ExtendedClassA_BWith2Methods.java.txt");

        ClassMethodFinder cmf = new ClassMethodFinder();
        cmf.initialize(_cu, "A");

        ContextConfiguration cc = new ContextConfiguration();
        cc.setCMFAnalyzer(cmf);
        cc.setMethodName("A");

        MethodSingleDeclaration msd = new MethodSingleDeclaration(cc);
        msd.detect();
    }

    @Test
    public void detectMethodOnlyDeclaredOnce()
    {
        CreateCompilationUnitFromTestClass("ExtendedClassA_BWith2Methods.java.txt");

        ClassMethodFinder cmf = new ClassMethodFinder();
        cmf.initialize(_cu, "A");

        ContextConfiguration cc = new ContextConfiguration();
        cc.setCMFAnalyzer(cmf);
        cc.setMethodName("MethodTwo");

        MethodSingleDeclaration msd = new MethodSingleDeclaration(cc);

        try {
            Assert.assertTrue(msd.detect());
        }
        catch(Exception e)
        {
            fail(e.getMessage());
        }
    }

    @Test
    public void parametersMethodOnlyDeclaredOnce()
    {
        //@todo: specifically public interface, this might lead to exposure to packages with unseen behavior
        CreateCompilationUnitFromTestClass("ExtendedClassA_BWith2Methods.java.txt");

        ClassMethodFinder cmf = new ClassMethodFinder();
        cmf.initialize(_cu, "A");
        MethodSingleDeclaration msd = new MethodSingleDeclaration(cmf, "MethodTwo");

        try {
            msd.detect();

            Assert.assertEquals(2, msd.getParameterMap().size());
            Assert.assertTrue(msd.getParameterMap().containsKey("$method"));
            Assert.assertTrue(msd.getParameterMap().containsKey("$class"));
        }
        catch(Exception e)
        {
            fail(e.getMessage());
        }
    }

    @Test
    public void detectMethodDeclaredMultipleTimes()
    {
        CreateCompilationUnitFromTestClass("ExtendedClassA_BWith2Methods.java.txt");

        ClassMethodFinder cmf = new ClassMethodFinder();
        cmf.initialize(_cu, "A");
        MethodMultipleDeclarations msd = new MethodMultipleDeclarations(cmf, "MethodOne");

        try {
            Assert.assertTrue(msd.detect());
        }
        catch(Exception e)
        {
            fail(e.getMessage());
        }
    }

    @Test
    public void parametersMethodDeclaredMultipleTimes()
    {
        //@todo: specifically public interface, this might lead to exposure to packages with unseen behavior
        CreateCompilationUnitFromTestClass("ExtendedClassA_BWith2Methods.java.txt");

        ClassMethodFinder cmf = new ClassMethodFinder();
        cmf.initialize(_cu, "A");
        MethodMultipleDeclarations mmd = new MethodMultipleDeclarations(cmf, "MethodOne");

        try {
            mmd.detect();

            Assert.assertEquals(2, mmd.getParameterMap().size());
            Assert.assertTrue(mmd.getParameterMap().containsKey("$method"));
            Assert.assertTrue(mmd.getParameterMap().containsKey("$class"));
        }
        catch(Exception e)
        {
            fail(e.getMessage());
        }
    }

    @Test
    public void detectMethodDeclaredInInterface()
    {
        //@todo: specifically public interface, this might lead to exposure to packages with unseen behavior
        CreateCompilationUnitFromTestClass("ExtendedClassA_BWith2Methods.java.txt");

        ClassMethodFinder cmf = new ClassMethodFinder();
        cmf.initialize(_cu, "A");
        MethodInterfaceDeclaration msd = new MethodInterfaceDeclaration(cmf, "MethodOne");

        try {
            Assert.assertTrue(msd.detect());
        }
        catch(Exception e)
        {
            fail(e.getMessage());
        }
    }

    @Test
    public void parametersMethodDeclaredInInterface()
    {
        //@todo: specifically public interface, this might lead to exposure to packages with unseen behavior
        CreateCompilationUnitFromTestClass("ExtendedClassA_BWith2Methods.java.txt");

        ClassMethodFinder cmf = new ClassMethodFinder();
        cmf.initialize(_cu, "A");
        MethodInterfaceDeclaration msd = new MethodInterfaceDeclaration(cmf, "MethodOne");

        try {
            msd.detect();

            Assert.assertEquals(1, msd.getParameterMap().size());
            Assert.assertTrue(msd.getParameterMap().containsKey("$interface"));
        }
        catch(Exception e)
        {
            fail(e.getMessage());
        }
    }

}
