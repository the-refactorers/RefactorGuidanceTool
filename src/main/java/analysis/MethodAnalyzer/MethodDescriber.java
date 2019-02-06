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
package analysis.MethodAnalyzer;

import com.github.javaparser.ast.body.MethodDeclaration;

public class MethodDescriber {

    private String _name = new String();
    private String _signature = new String();
    private String _type = new String();
    private MethodDeclaration _declarationAST;

    public MethodDescriber()
    {

    }

    public MethodDescriber(String methodReturnType, String methodName, String methodParamSignature)
    {
        setName(methodName);
        setReturnType(methodReturnType);
        setSignature(methodName+methodParamSignature);
    }

    public MethodDescriber(MethodDeclaration item) {
        setReturnType(item.getType().asString());
        setName(item.getName().asString());
        setSignature(item.getSignature().asString());
        setMethodDeclaration(item);
    }

    public boolean equals(MethodDescriber md)
    {
        //System.out.println(this.getType() + " " +
          //      this.getName() + " " + this.getSignature());
        //System.out.println(md.getType() + " " +
          //      md.getName() + " " + md.getSignature());

        return (md.getType().contentEquals(this._type) &&
                md.getName().contentEquals(this._name) &&
                md.getSignature().contentEquals(this._signature));
    }

    private void setName(String nameAsString) {
        _name = nameAsString;
    }

    private void setSignature(String s) {
        _signature = s;
    }

    private void setReturnType(String t) {
        _type = t;
    }

    public String getName() { return _name; }
    public String getSignature() {return _signature;}
    public String getType() { return _type;}

    public String fullTypeSignature() {
        return _type+" "+_signature;
    }

    public MethodDeclaration getMethodDeclaration()
    {
        return _declarationAST;
    }

    public void setMethodDeclaration(MethodDeclaration mDecl)
    {
        _declarationAST = mDecl;
    }
}
