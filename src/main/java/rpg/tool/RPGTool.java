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
package rpg.tool;

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

        JFrame frame = new  JFrame("RefactoringGuidance");
        frame.setContentPane(new RefactoringGuidance().mainPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}