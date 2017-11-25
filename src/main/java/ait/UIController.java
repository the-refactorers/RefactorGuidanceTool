package ait;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ResourceBundle;

public class UIController {
    public JPanel mainPanel;
    private JTextPane hereTheGeneratedTextTextPane;
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
