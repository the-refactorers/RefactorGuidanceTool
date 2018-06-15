/**
 *  This file is part of RefactorGuidance project. Which explores possibilities to generate context based
 *  instructions on how to refactor a piece of Java code. This applied in an education setting (bachelor SE students)
 *
 *      Copyright (C) 2018, Patrick de Beer, p.debeer@fontys.nl
 *
 *          This program is free software: you can redistribute it and/or modify
 *          it under the terms of the GNU General Public License as published by
 *          the Free Software Foundation, either version 3 of the License, or
 *          (at your option) any later version.
 *
 *          This program is distributed in the hope that it will be useful,
 *          but WITHOUT ANY WARRANTY; without even the implied warranty of
 *          MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *          GNU General Public License for more details.
 *
 *          You should have received a copy of the GNU General Public License
 *          along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package analysis;

import analysis.MethodAnalyzer.ClassMethodFinder;
import analysis.MethodAnalyzer.MethodDescriber;
import analysis.ResourceExampleClassParser;
import analysis.context.CodeSection;
import analysis.context.ContextConfiguration;
import analysis.context.IContextDetector;
import analysis.context.ParameterCollector;
import analysis.dataflow.MethodDataFlowAnalyzer;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.symbolsolver.javaparser.Navigator;
import org.junit.Before;

import java.util.List;
import java.util.Map;

public class JavaParserTestSetup {

    protected CompilationUnit _cu;
    protected ResourceExampleClassParser _loader;

    protected ParameterCollector _pc;
    protected Map<String, List<String>> _params;

    protected ClassMethodFinder _cmf;
    protected MethodDeclaration _selectedMethod;
    protected CodeSection _extractRegion = new CodeSection(-1, -1);
    protected MethodDataFlowAnalyzer _mdfaAna;

    protected MethodDeclaration findMethodDeclarationInClass(String className, String methodName) {
        return Navigator.demandMethod(Navigator.demandClass(_cu, className), methodName);
    }

    protected void CreateCompilationUnitFromTestClass(String classTemplate)
    {
        _cu = _loader.Parse(classTemplate);
    }

    protected void setupTestClass(String className, String methodName) {

        CreateCompilationUnitFromTestClass(className + ".java.txt");

        _cmf = new ClassMethodFinder();
        _cmf.initialize(_cu, className);

        // Determine name based on location
        if(_extractRegion.notDefined())
        {
            _selectedMethod = findMethodDeclarationInClass(className, methodName);
        }
        else {
            _selectedMethod = _cmf.getMethodDeclarationForLocation(_extractRegion.begin());
        }
    }

    protected void extractRegion(int start, int end)
    {
        _extractRegion = new CodeSection(start, end);
    }

    protected CompilationUnit CreateCompilationUnitFromTestClass(ResourceExampleClassParser resourceLoader, String classTemplate)
    {
        return resourceLoader.Parse(classTemplate);
    }

    protected void retrieveParams(IContextDetector detector) {
        _pc = detector.getParameters();
        _params = _pc.getCollection();
    }

    protected ContextConfiguration mdfaAnalysis()
    {
        _mdfaAna = new MethodDataFlowAnalyzer();
        _mdfaAna.initialize(_selectedMethod, _extractRegion);
        _mdfaAna.start();

        ContextConfiguration cc = new ContextConfiguration();
        cc.setMethodDataFlowAnalyzer(_mdfaAna);

        return cc;
    }

    @Before
    public void Setup()
    {
        _loader = new ResourceExampleClassParser();
    }
}
