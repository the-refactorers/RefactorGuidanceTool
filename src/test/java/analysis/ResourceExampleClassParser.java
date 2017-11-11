package analysis;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;

import java.io.InputStream;

/**
 * Helper class to parse the classes defined in the resource folder
 */
public class ResourceExampleClassParser {

    /**
     * Parses class signatures contained in resources folder
     *
     * @param classAsResource   A java class as resource txt file which are parsed into a CompilationObject.
     *                          this object is used in the JavaParser for further code analysis.
     * @return A JavaParser compilationUnit. Null if resource could not be found or parsed.
     */
    public CompilationUnit Parse(String classAsResource) {

        InputStream parseStream = this.getClass().getClassLoader().getResourceAsStream(classAsResource);

        if (parseStream == null) {
            throw new RuntimeException("Unable to find sample " + classAsResource);
        }

        return JavaParser.parse(parseStream);
    }
}
