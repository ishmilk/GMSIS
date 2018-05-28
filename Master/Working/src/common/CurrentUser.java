/*
 * Author: Amy Dowse
 * Singleton Object that holds the details of the currntly logged in user 
 */
package common;

/**
 *
 * @author User
 */
public class CurrentUser 
{
    private final static CurrentUser instance = new CurrentUser();

    public static CurrentUser getInstance() 
    {
        return instance;
    }

    private String userName;
    private boolean isAdmin;

    public String getUserName()
    {
        return userName;
    }
    
    public boolean getisAdmin()
    {
        return isAdmin;
    }
    
    public void setUserName(String newUserName)
    {
        userName = newUserName;
    }
    
    public void setisAdmin(Boolean newisAdmin)
    {
        isAdmin = newisAdmin;
    }
    
    
    
    
}
