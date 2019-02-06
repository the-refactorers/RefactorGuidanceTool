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

import java.util.*;

/**
 * Determines which specific contexts are present in a piece of code in a generic way.
 * by evaluating all given context detectors.
 */
public class ContextAnalyzer {

    private List<IContextDetector> _contextDetectors = new ArrayList<>();
    private EnumSet<CodeContext.CodeContextEnum> _detectedSet = EnumSet.noneOf(CodeContext.CodeContextEnum.class);
    private Map<String, List<String>> _parameterDefinitions = new HashMap<>();

    public void setContextDetectors(List<IContextDetector> detectors) {
        this._contextDetectors = detectors;
    }

    public EnumSet<CodeContext.CodeContextEnum> getDetectedContextSet()
    {
        return _detectedSet;
    }

    public void run()
    {
        for(IContextDetector detector : _contextDetectors)
            try {
                if (detector.detect()) {
                    _detectedSet.add(detector.getType());
                    extendParameterDefinitions(detector);
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
    }

    private void extendParameterDefinitions(IContextDetector detector) {
        Map<String, List<String>> parameters = detector.getParameters().getCollection();
        parameters.forEach((parameter, value) -> _parameterDefinitions.put(parameter, value));
    }

    public Map<String,List<String>> getParameterMap() {
        return _parameterDefinitions;
    }
}
