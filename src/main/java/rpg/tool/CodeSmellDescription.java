package rpg.tool;

/**
 * Data structure containing information about a code smell that has been detected by
 * a third party tool
 */
public class CodeSmellDescription {

    // Name of the smell (used to find the corresponding template with procedural guidance info
    public String Name = "";

    // Name of java file where smell was detected
    public String JavaFile = "";

    // Location (line number) in the file where the smell is detected
    public int fileLocation = 0;

}
