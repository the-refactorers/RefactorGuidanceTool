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

import org.junit.Assert;
import org.junit.Test;

public class SomeDummyTesting {

    class MyObj
    {
        private int i = 6;
        void SetI(int p) { i =p; }
        int GetI() {return i;}
    }

    @Test
    public void changingObjectDataByMethodsBeingExtracted()
    {
        MyObj obj = new MyObj();
        int a = 0;

        a = getA(obj, a);

        Assert.assertEquals(3, obj.GetI());
        System.out.println(obj.GetI() + "" + a);
    }

    // This was extracted from @Test changingObjectDataByMethodsBeingExtracted
    // changes to variable 'a' should be returned, while changes to obj are automatically reflected in the local object
    private int getA(MyObj obj, int a) {
        a++;
        ChangeMyObj(obj);
        return a;
    }

    private void ChangeMyObj(MyObj obj) {
        obj.SetI(3);
    }
}
