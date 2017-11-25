package ait;

import analysis.ClassMethodFinder;
import analysis.ClassMethodFinder;
import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.InputStream;
import java.net.URL;
import java.util.*;

public class UIController {
    public JPanel mainPanel;
    private JTextArea hereTheGeneratedTextTextPane;
    private JButton generateInstructionsButton;
    private JRadioButton renameRadioButton;
    private JRadioButton largeClassRadioButton;
    private JTextField inputJavaFile;
    private JButton browseJavaFileButton;
    private JButton projectDirButton;
    private JTextField inputProjectDir;
    private JTextField inputCodeLineSmell;
    private JComboBox prefabExamplesSelection;

    private EnumSet<CodeContext.CodeContextEnum> AnalyzeContextForRenaming(ClassMethodFinder cmf, String methodName)
    {
        EnumSet<CodeContext.CodeContextEnum> codeContext = EnumSet.noneOf(CodeContext.CodeContextEnum.class);

        if (!cmf.contextMultipleDeclarations(methodName)) {
            codeContext.add(CodeContext.CodeContextEnum.method_single_declaration);
        }
        else
        {
            codeContext.add(CodeContext.CodeContextEnum.method_multiple_declares);
        }

        if (cmf.contextDeclaredInInterface(methodName))
        {
            codeContext.add(CodeContext.CodeContextEnum.method_defined_in_interface);
        }
        if (cmf.contextDeclaredInSuperClass(methodName))
        {
            codeContext.add(CodeContext.CodeContextEnum.method_override);
        }

        return codeContext;
    }

    private void generateInstructions(String testResource, int lineNumber)
    {
        InputStream parseStream = this.getClass().getClassLoader().getResourceAsStream(testResource);

        if (parseStream == null) {
            throw new RuntimeException("Unable to find sample " + testResource);
        }

        CompilationUnit cu = JavaParser.parse(parseStream);

        String className = "MyMethod";
        ClassMethodFinder cmf = new ClassMethodFinder(cu, className);

        // Determine name based on location
        String methodName = cmf.getMethodNameForLocation(lineNumber);

        // Instruction in template Parameter fill test: Dummy method $method is located in dummy $class
        Map<String, String> parameterMap = new HashMap<>();
        parameterMap.put("$method", methodName);
        parameterMap.put("$class", className);

        AdaptiveInstructionTree tree = new AIT_RenameGeneration().getAdaptiveInstructionTree();
        InstructionGenerator generator = new InstructionGenerator(tree);

        //generator.contextSet = codeContext;
        generator.setParameterMap(parameterMap);

        // Analyze context and set-up code context of generator
        generator.setContext(AnalyzeContextForRenaming(cmf, methodName));

        List<String> instructionSteps = generator.generateInstruction();

        hereTheGeneratedTextTextPane.setText("");

         for(String i : instructionSteps) {
              hereTheGeneratedTextTextPane.append(i + "\n");
        }
    }

    public UIController() {

        createUIComponents();

        generateInstructionsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                generateInstructions(inputJavaFile.getText(), Integer.parseInt(inputCodeLineSmell.getText()));
            }
        });
        prefabExamplesSelection.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (e.getSource() == prefabExamplesSelection)
                {
                    JComboBox cb = (JComboBox)e.getSource();
                    String selectedItem = (String)cb.getSelectedItem();

                    inputJavaFile.setText(selectedItem);
                }
            }
        });
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here

        prefabExamplesSelection.addItem("");
        prefabExamplesSelection.addItem("RenameMethod.java.txt");
        prefabExamplesSelection.addItem("SimpleClassWith2Methods.java.txt");

        prefabExamplesSelection.setSelectedIndex(0);
    }
}
