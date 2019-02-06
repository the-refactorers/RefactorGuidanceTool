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

package aig;

public class CodeContext {

    public enum contextAction {
        listSameName
    }

    public enum CodeContextEnum {

        // Rename refactoring defined context
        MethodSingleDeclaration,
        MethodMultipleDeclarations,
        MethodNoneInterfaceDeclaration,
        MethodInterfaceDeclaration,
        MethodOverload,
        MethodNoneOverload,
        MethodOverride,
        MethodNoneOverride,
        MethodOverrideNoAnnotation,
        MethodNoneOverrideNoAnnotation,

        // Extract method
        MethodExtractNoneLocalDependencies,
        MethodExtractSingleArgument,
        MethodExtractSingleResult,
        MethodExtractMultiArgument,
        MethodExtractMultiResult,
        MethodExtractNameHiding, // variable name is binded on different scopes
        MethodExtractNoNameHiding,
        MethodExtractNoneArguments,
        MethodExtractNoneResults,
        MethodExtractControlReturn,
        MethodExtractNoControlReturn

        , always_true     // used to link multiple actions together, where no decision is required
    }
}

