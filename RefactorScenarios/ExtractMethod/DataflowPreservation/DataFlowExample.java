package DataflowPreservation;

public class DataFlowExample {

    int s;

    class myObj
    {
        private int i = 6;
        void SetI(int p) { i =p; }
        int GetI() {return i;}
    }

    /**
     * Extraction of a piece of code where there is no variable from before section in used
     * and no changed variable outside section is changed, should indicate no variables for special attention
     */
    public void testZeroInZeroOut()
    {
        int a=0;

        //START extract
        int b=6;
        b++;
        System.out.print(b);
        //STOP extract

        return;
    }

    public int simpleSequence(int t)
    {
        myObj m = new myObj();
        int a;            // VariableDeclarationExpr | WRITE(A)
        int b=1;            // VariableDeclarationExpr | WRITE(B)
        int c = 0; s=4;
//        int s = 8;

        s = 9;

        // REFACTOR FROM HERE
        a = b + 1;          // AssignExpr ; target NameExpr="a" ; value BinaryExpr "b+1" -> NameExpr | WRITE(A) | READ(B
        m.SetI(7);
        t = 6;
        int d = 4;
        // EXTRACT METHOD ENDS

        if (b>1) {
            b = newB();
            b++;
        };

        a = a -1;           // AssignExpr | READ(A), WRITE(A)
        a = 2;              // AssignExpr ; target NameExpr="a" ; value IntegerLiteralExpr "2" | WRITE(A)
        c++;

        System.out.println(m.GetI());
        System.out.println(a);

        return t;
    }

    private int newB()
    {
        return 8;
    }
}
