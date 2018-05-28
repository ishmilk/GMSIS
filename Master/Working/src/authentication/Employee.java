           
                    /*─────────────────┐
                    │      author : ismail.      │
                    └─────────────────*/

package authentication;

public class Employee 
{

    private int loginID ;
    private String password ;
    private String name ;
    private int    isAdmin ;
    private int    isMechanic ;

    // constructor
    public Employee( int loginID , String password , String name , int isAdmin , int isMechanic )
    {
    
        this.loginID    = loginID ;
        this.password   = password ;
        this.name       = name ;
        this.isAdmin    = isAdmin ;
        this.isMechanic = isMechanic ;
    
    }
    
    /************************** getter methods **************************/
    
    // this method returns loginID
    public int getEmployeeID()
    {
    
        return loginID ;
    
    }
    
    // this method returns password
    public String getPassword()
    {
    
        return password ;
    
    }
    
    // this method returns the name
    public String getName()
    {
    
        return name ;
    
    }
    
    // this method returns the adminStatus
    public int getIsAdmin()
    {
    
        return isAdmin ;
    
    }
    
    // this method returns the mechanicStatus
    public int getIsMechanic()
    {
    
        return isMechanic ;
    
    }
    
    /************************** setter methods **************************/
    
    // this method sets a new loginID + returns TRUE on success , FALSE otherwise
    public boolean setLoginID( int newLoginID )
    {
    
        try 
        {
        
            loginID = newLoginID  ;
            return true           ; // action succeded
        
        }
        
        catch ( Exception e ) 
        {
        
            e.printStackTrace()   ;
            return false          ; // action flopped
        
        }
    
    }
    
    // this method sets a new password + returns TRUE on success , FALSE otherwise
    public boolean setPassword( String newPassword )
    {
    
        try 
        {
        
            password = newPassword ;
            return true            ; // action succeded
        
        }
        
        catch ( Exception e ) 
        {
        
            e.printStackTrace()    ;
            return false           ; // action flopped
        
        }
    
    }
    
    // this method sets a new name + returns TRUE on success , FALSE otherwise
    public boolean setName( String newName )
    {
    
        try 
        {
        
            name = newName          ;
            return true             ; // action succeded
        
        }
        
        catch ( Exception e ) 
        {
        
            e.printStackTrace()     ;
            return false            ; // action flopped
        
        }
    
    }
    
    // this method sets a new admin status + returns TRUE on success , FALSE otherwise
    public boolean setAdminStatus( int newAdminStatus )
    {
    
        try 
        {
        
            isAdmin = newAdminStatus ;
            return true              ; // action succeded
        
        }
        
        catch ( Exception e ) 
        {
        
            e.printStackTrace()      ;
            return false             ; // action flopped
        
        }
    
    }
    
    // this method sets a new mechanic status + returns TRUE on success , FALSE otherwise
    public boolean setMechanicStatus( int newMechanicStatus )
    {
    
        try 
        {
        
            isMechanic = newMechanicStatus ;
            return true                    ; // action succeded
        
        }
        
        catch ( Exception e ) 
        {
        
            e.printStackTrace() ;
            return false        ; // action flopped
        
        }
    
    }
    
}