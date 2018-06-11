interface API_Interface {

    public void subscribe();
    public java.lang.String getAccountName();
}


public class API_Implementation implements API_Interface {

    @Override
    public void subscribe() {

    }

    public java.lang.String getAccountName(String prefix)
    {
        return prefix + getAccountName();
    }

    public java.lang.String getAccountName() {
        return null;
    }
}


public class API_SpecialImplementation extends API_Implementation {

    @Override
    public void subscribe() {

    }

    public java.lang.String getAccountName()
    { // API_SpecialImplementation,  Rename line 34

        String tempString = "";
        tempString = "Hello";

        tempString.toUpperCase();

        return tempString;
    }
}