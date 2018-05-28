           
                    /*─────────────────┐
                    │      author : ismail.      │
                    └─────────────────*/
package authentication;


public class Admin extends Employee
{
    
    // constructor
    public Admin( int loginID , String password , String name , int isAdmin , int isMechanic )
    {
    
        super( loginID , password , name , isAdmin , isMechanic );
        
        // assumption made --> employee = mechanic XOR admin
        isAdmin    = 1 ;
        isMechanic = 0 ;
    
    }
    
}