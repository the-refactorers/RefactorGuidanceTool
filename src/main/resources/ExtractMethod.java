class EM
{
    int _internal = 0;
    private String firstName = "";

    public void ExtractMe()
    {
        int itt = 0;
        String firstName = "Patrick";

        String copyOfFirstName;
        copyOfFirstName = firstName;
        int x = itt + 1;

        //System.out.println(copyOfFirstName);
    }

    public void ExtractNoArgOneRes()
    {
        String copyOfFirstName;
        copyOfFirstName = firstName;
        int x = 1;

        System.out.println(copyOfFirstName);
    }

    public void ExtractMultiArgOneRes()  // extract 32-34
    {
        int itt = 0;
        String firstName = "Patrick";

        String copyOfFirstName;
        copyOfFirstName = firstName;
        int x = itt + 1;

        System.out.println(copyOfFirstName);
    }

    public void longMethod() // extract 44-45
    {
        String lastName = "DuNo";
        String firstName = "MrM";
        String mergedName;

        mergedName = firstName + " " + lastName;

        System.out.println(mergedName);
    }

    public void doSomethingWhenBigger() // extract 54-57
    {
        int a = 0;

        if(a<10)
            return;
        else
            _internal = a + 6;

        System.out.println(_internal);
    }
}