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
