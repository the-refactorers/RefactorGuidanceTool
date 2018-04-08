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
