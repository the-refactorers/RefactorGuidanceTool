package context;

import analysis.JavaParserTestSetup;
import analysis.MethodAnalyzer.ClassMethodFinder;
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

    @Test
    public void detectMethodOnlyDeclaredOnce()
    {
        CreateCompilationUnitFromTestClass("ExtendedClassA_BWith2Methods.java.txt");

        ClassMethodFinder cmf = new ClassMethodFinder();
        cmf.initialize(_cu, "A");
        MethodSingleDeclaration msd = new MethodSingleDeclaration(cmf, "MethodTwo");

        try {
            Assert.assertTrue(msd.detect());
        }
        catch(Exception e)
        {
            fail();
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
            fail();
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
            fail();
        }
    }
}
