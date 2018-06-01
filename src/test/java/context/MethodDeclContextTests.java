package context;

import analysis.JavaParserTestSetup;
import analysis.MethodAnalyzer.ClassMethodFinder;
import analysis.MethodAnalyzer.MethodDescriber;
import analysis.context.*;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import java.util.List;
import java.util.Map;

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

        MethodDescriber method = new MethodDescriber("void","A","()");
        cc.setMethodDescriber(method);

        MethodSingleDeclaration msd = new MethodSingleDeclaration(cc);
        msd.detect();
    }

    @Test
    public void detectMethodOnlyDeclaredOnce()
    {
        CreateCompilationUnitFromTestClass("ExtendedClassA_BWith2Methods.java.txt");

        ClassMethodFinder cmf = new ClassMethodFinder();
        cmf.initialize(_cu, "B");

        ContextConfiguration cc = new ContextConfiguration();
        cc.setCMFAnalyzer(cmf);
        MethodDescriber method = new MethodDescriber("int","MethodFour","(boolean)");
        cc.setMethodDescriber(method);

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
        MethodDescriber method = new MethodDescriber("void","MethodTwo","()");
        MethodSingleDeclaration msd = new MethodSingleDeclaration(cmf, method);

        try {
            msd.detect();

            Assert.assertEquals(2, msd.getParameterMap().size());
            Assert.assertTrue(msd.getParameterMap().containsKey("#method"));
            Assert.assertTrue(msd.getParameterMap().containsKey("#class"));
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

        MethodDescriber method = new MethodDescriber("void","MethodOne","()");
        MethodMultipleDeclarations msd = new MethodMultipleDeclarations(cmf, method);

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
        MethodDescriber method = new MethodDescriber("void","MethodOne","()");
        MethodMultipleDeclarations mmd = new MethodMultipleDeclarations(cmf, method);

        try {
            mmd.detect();

            Assert.assertEquals(2, mmd.getParameterMap().size());
            Assert.assertTrue(mmd.getParameterMap().containsKey("#method"));
            Assert.assertTrue(mmd.getParameterMap().containsKey("#class"));
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
        MethodDescriber method = new MethodDescriber("void","MethodOne","()");
        MethodInterfaceDeclaration msd = new MethodInterfaceDeclaration(cmf, method);

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
        MethodDescriber method = new MethodDescriber("void","MethodOne","()");
        MethodInterfaceDeclaration msd = new MethodInterfaceDeclaration(cmf, method);

        try {
            msd.detect();

            Assert.assertEquals(1, msd.getParameterMap().size());
            Assert.assertTrue(msd.getParameterMap().containsKey("#interface"));
        }
        catch(Exception e)
        {
            fail(e.getMessage());
        }
    }

    @Test
    public void detectOverrideMethods()
    {
        CreateCompilationUnitFromTestClass("ExtendedClassA_BWith2Methods.java.txt");

        ClassMethodFinder cmf = new ClassMethodFinder();
        cmf.initialize(_cu, "A");

        ContextConfiguration cc = new ContextConfiguration();
        MethodDescriber method = new MethodDescriber("void","MethodFour","()");
        cc.setMethodDescriber(method);

        cc.setCMFAnalyzer(cmf);
        MethodOverride mod = new MethodOverride(cc);

        try {
            Assert.assertTrue("No override detected", mod.detect());
        }
        catch(Exception e)
        {
            fail(e.getMessage());
        }
    }

    @Test
    public void parametersOverrideMethods()
    {
        CreateCompilationUnitFromTestClass("ExtendedClassA_BWith2Methods.java.txt");

        ClassMethodFinder cmf = new ClassMethodFinder();
        cmf.initialize(_cu, "A");

        ContextConfiguration cc = new ContextConfiguration();

        MethodDescriber md = new MethodDescriber("void", "MethodFour","()");

        cc.setMethodDescriber(md);
        cc.setCMFAnalyzer(cmf);

        MethodOverride overrideDetector = new MethodOverride(cc);

        try {
            overrideDetector.detect();

            Map<String,List<String>> paramValues = overrideDetector.getParameterMap();

            //  classes should be returned (B and E)
            Assert.assertEquals(2, paramValues.get("#class-list").size());
            Assert.assertTrue("B not found", paramValues.get("#class-list").contains("B"));
            Assert.assertTrue("E not found", paramValues.get("#class-list").contains("E"));
        }
        catch(Exception e)
        {
            fail(e.getMessage());
        }
    }

    @Test
    public void detectOverrideMethodsWithNoAnnotationLocal()
    {
        CreateCompilationUnitFromTestClass("ExtendedClassA_BWith2Methods.java.txt");

        ClassMethodFinder cmf = new ClassMethodFinder();
        cmf.initialize(_cu, "B");

        ContextConfiguration cc = new ContextConfiguration();
        MethodDescriber md = new MethodDescriber("void","MethodFour","()");
        cc.setMethodDescriber(md);
        cc.setCMFAnalyzer(cmf);
        MethodOverrideWithoutNoAnnotation mod = new MethodOverrideWithoutNoAnnotation(cc);

        try {
            // Detector should indicate that class B has methods that do not contain an @Override marker
            Assert.assertTrue(mod.detect());
            Assert.assertTrue(mod.getParameters().getCollection().get("#method-list").contains("B :: void MethodFour()"));
            Assert.assertTrue(mod.getParameters().getCollection().get("#method-list").contains("E :: void MethodFour()"));
        }
        catch(Exception e)
        {
            fail(e.getMessage());
        }
    }

    @Test
    public void detectOverrideMethodsWithNoAnnotationInOverride()
    {
        CreateCompilationUnitFromTestClass("ExtendedClassA_BWith2Methods.java.txt");

        ClassMethodFinder cmf = new ClassMethodFinder();
        cmf.initialize(_cu, "A");

        ContextConfiguration cc = new ContextConfiguration();
        MethodDescriber md = new MethodDescriber("void","MethodOne","()");
        cc.setMethodDescriber(md);
        cc.setCMFAnalyzer(cmf);
        MethodOverrideWithoutNoAnnotation mod = new MethodOverrideWithoutNoAnnotation(cc);

        try {
            Assert.assertTrue(mod.detect());
            Assert.assertFalse(mod.getParameterMap().get("#method-list").contains("A :: void MethodOne()"));
            Assert.assertTrue(mod.getParameterMap().get("#method-list").contains("B :: void MethodOne()"));
        }
        catch(Exception e)
        {
            fail(e.getMessage());
        }
    }

    @Test
    public void detectNoOverrideOnMethodsWithDifferentSignature()
    {
        CreateCompilationUnitFromTestClass("ExtendedClassA_BWith2Methods.java.txt");

        ClassMethodFinder cmf = new ClassMethodFinder();
        cmf.initialize(_cu, "B");

        ContextConfiguration cc = new ContextConfiguration();

        MethodDescriber md = new MethodDescriber("int", "MethodFour","(boolean)");

        cc.setMethodDescriber(md);
        cc.setCMFAnalyzer(cmf);

        MethodOverride overrideDetector = new MethodOverride(cc);

        try {
            boolean needsOverride = overrideDetector.detect();

            // Expect there is no #class-list key defined, while this is a unique method
            Assert.assertFalse(overrideDetector.getParameterMap().containsKey("#class-list"));
            Assert.assertFalse(needsOverride);
        }
        catch(Exception e)
        {
            fail(e.getMessage());
        }
    }

    @Test
    public void detectOverloadedMethods()
    {
        CreateCompilationUnitFromTestClass("ExtendedClassA_BWith2Methods.java.txt");

        ClassMethodFinder cmf = new ClassMethodFinder();
        cmf.initialize(_cu, "A");

        ContextConfiguration cc = new ContextConfiguration();
        MethodDescriber md = new MethodDescriber("int","MethodEight","(boolean)");
        cc.setMethodDescriber(md);
        cc.setCMFAnalyzer(cmf);
        MethodOverload mod = new MethodOverload(cc);

        try {
            Assert.assertTrue(mod.detect());
            Assert.assertTrue(mod.getParameters().getCollection().get("#method-list").contains("B :: void MethodEight(boolean)"));
            Assert.assertTrue(mod.getParameters().getCollection().get("#method-list").contains("E :: boolean MethodEight(boolean)"));
        }
        catch(Exception e)
        {
            fail(e.getMessage());
        }
    }

    @Test
    public void detectMethodNotOverloaded()
    {
        CreateCompilationUnitFromTestClass("ExtendedClassA_BWith2Methods.java.txt");

        ClassMethodFinder cmf = new ClassMethodFinder();
        cmf.initialize(_cu, "A");

        ContextConfiguration cc = new ContextConfiguration();
        MethodDescriber md = new MethodDescriber("void","MethodTwo","()");
        cc.setMethodDescriber(md);
        cc.setCMFAnalyzer(cmf);
        MethodOverload mod = new MethodOverload(cc);

        try {
            Assert.assertFalse(mod.detect());
            Assert.assertTrue(mod.getParameters().getCollection().isEmpty());
        }
        catch(Exception e)
        {
            fail(e.getMessage());
        }
    }

}
