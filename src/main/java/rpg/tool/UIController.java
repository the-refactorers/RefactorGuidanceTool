package rpg.tool;

import analysis.RenameMethodAnalyzer;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;

public class UIController {
    public JPanel mainPanel;
    private JTextArea hereTheGeneratedTextTextPane;
    private JButton generateInstructionsButton;
    private JRadioButton renameRadioButton;
    private JRadioButton extractMethodRadioButton;
    private JTextField inputJavaFile;
    private JButton browseJavaFileButton;
    private JButton projectDirButton;
    private JTextField inputProjectDir;
    private JTextField startLine;
    private JComboBox prefabExamplesSelection;
    private JTextField newMethodName;
    private JTextField endLine;


    public UIController() {

        createUIComponents();

        generateInstructionsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                RenameMethodAnalyzer analyzer = new RenameMethodAnalyzer();
                List<String> instructions = new ArrayList<>();

                int startLine = Integer.parseInt(UIController.this.startLine.getText());
                int endLine = Integer.parseInt(UIController.this.endLine.getText());

                if(renameRadioButton.isSelected())
                {
                    // Selection of method to rename is based on provided line number
                    instructions = analyzer.generateInstructions(
                            "Rename",
                            inputJavaFile.getText(),
                            "MyMethod",
                            newMethodName.getText(),
                            startLine, -1);

                } else if(extractMethodRadioButton.isSelected())
                {
                    instructions = analyzer.generateInstructions(
                            "ExtractMethod",
                            inputJavaFile.getText(),
                            "EM",
                            newMethodName.getText(),
                            startLine, endLine);

                } else
                {
                    instructions.add("Unknown refactoring process selected");
                }

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
        prefabExamplesSelection.addItem("ExtractMethod.java");
        prefabExamplesSelection.addItem("SimpleClassWith2Methods.java.txt");

        prefabExamplesSelection.setSelectedIndex(2);

        //renameRadioButton.setSelected(true);
        extractMethodRadioButton.setSelected(true);
    }
}
