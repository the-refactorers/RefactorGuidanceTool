package rpg.tool;

import ait.UIController;
import org.apache.commons.cli.*;

import javax.swing.*;

/**
 * Refactoring (with) Procedural Guidance Tool
 */
public class RPGTool {
    public static void main(String[] args) throws Exception {

        System.out.println("-= Refactor with Procedural Guidance v0.01 =-");
        Options cmdLineOptions = new Options();

        Option fileForAnalysis = new Option("f","file", true, "Java File to be analyzed");
        fileForAnalysis.setRequired(true);
        Option detectedLoc     = new Option("l", "loc", true, "Location of smell in code");
        fileForAnalysis.setRequired(true);
        Option typeOfSmell     = new Option("s", "smell", true, "Name of smell detected");
        typeOfSmell.setRequired(false);

        cmdLineOptions.addOption(fileForAnalysis);
        cmdLineOptions.addOption(detectedLoc);
        cmdLineOptions.addOption(typeOfSmell);

        CommandLineParser parser = new DefaultParser();
        HelpFormatter formatter = new HelpFormatter();
        CommandLine cmd = null;

        try {
            cmd = parser.parse(cmdLineOptions, args);
        } catch (ParseException e) {
            System.out.println(e.getMessage());
            formatter.printHelp("rpg-tool", cmdLineOptions);

            //System.exit(1);
            //return;
        }

        //System.out.print(cmd.getOptionValue("file"));
        //System.out.print(cmd.getOptionValue("loc"));

        JFrame frame = new  JFrame("UIController");
        frame.setContentPane(new UIController().mainPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);

    }
}