           
                    /*─────────────────┐
                    │      author : ismail.      │
                    └─────────────────*/
package authentication;

import authentication.Employee;


public class Mechanic extends Employee
{

    private double hourlyRate ;
    
    // constructor
    public Mechanic( int loginID , String password , String name , int isAdmin , int isMechanic )
    {
    
        super( loginID , password , name , isAdmin , isMechanic );
        
        // assumption made --> employee = mechanic XOR admin
        isAdmin    = 0 ;
        isMechanic = 1 ;
    
    }
    
    /************************** getter methods **************************/
    
    // this method returns the hourlyRate
    public double getHourlyRate() 
    {
        
        return hourlyRate ;
        
    }
    
    /************************** setter methods **************************/
    
    // this method sets a new hourlyRate + returns TRUE on success , FALSE otherwise
    public boolean setHourlyRate( double newHourlyRate )
    {
    
        try 
        {
        
            hourlyRate = newHourlyRate ;
            return true                ; // action succeded
        
        }
        
        catch ( Exception e ) 
        {
        
            e.printStackTrace()        ;
            return false               ; // action flopped
        
        }
    
    }

}