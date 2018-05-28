class SystemUser
{
    int employeeID;
    String firstName;
    String surname;
    private String loginID;
    private String PassKey;  
    SystemUser(int a,String b,String c)   //3 arg constructor
    {
        employeeID=a;
        firstName=b;
        surname=c;
    }
    SystemUser credentials(String a,String b) // 2 arg constructor changing pw'ds?
    {
        loginID=a;
        PassKey=b;
        return this;
    }
    boolean IDcheck(String ID) // is this to test for uniqueness?
    {
        if(loginID.equals(ID))
        {
            return true;
        }
        else
        {
            return false;
        }
    }
    boolean Passcheck(String Pass)
    {
        if(PassKey.equals(Pass))
        {
            return true;
        }
        else
        {
            return false;
        }
    }
    void updateFName(String name)
    {
        firstName=name;
    }
    void updateSName(String name)
    {
        surname=name;
    }
    void updateID(int ID)
    {
        employeeID=ID;
    }
}
