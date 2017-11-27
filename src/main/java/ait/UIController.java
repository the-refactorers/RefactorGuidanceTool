package ait;

import analysis.ClassMethodFinder;
import analysis.ClassMethodFinder;
import analysis.RenameMethodAnalyzer;
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


    public UIController() {

        createUIComponents();

        generateInstructionsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                RenameMethodAnalyzer analyzer = new RenameMethodAnalyzer();
                List<String> instructions = analyzer.generateInstructions(inputJavaFile.getText(), Integer.parseInt(inputCodeLineSmell.getText()));

                hereTheGeneratedTextTextPane.setText("");
                for(String s : instructions)
                {
                    hereTheGeneratedTextTextPane.append(s + "\n");
                }
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
