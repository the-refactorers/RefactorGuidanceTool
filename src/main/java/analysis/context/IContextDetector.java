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
package analysis.context;

import aig.CodeContext;

// A General interface which every context class should implement
public interface IContextDetector {

    // Each detector should implement constructor like the one below
    // public <<detectorName>>(ContextConfiguration cc);

    /**
     * Detects if a specific context exists in code that is analyzed by detector
     * @return Returns true if context exists
     * @throws Exception
     */
    boolean detect() throws Exception;

    /**
     * When a context exists a hashmap is filled with parametrized values in form <"$param","param-value">
     *     which is contained in the ParameterCollector
     * @return ParameterCollector object which can be queried for the desired parameters
     */
    public ParameterCollector getParameters();

    /**
     * Type name of detector
     * @return
     */
    CodeContext.CodeContextEnum getType();
}
