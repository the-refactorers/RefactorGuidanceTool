package rpg.tool;

class testbase
{
    public void methodA()
    {
        System.out.println("base");
    }
}

public class testhide extends testbase {

    // A student might not be aware (even when there are hints) that this method is actually overriden by the
    // base class and therefore a simple rename might change behavior
    public void methodA()
    {
        System.out.println("hide");
    }


}


class runner {
    public static void main(String[] args) {
        testbase base = new testhide();
        base.methodA();
        System.out.println(0);
    }
}
