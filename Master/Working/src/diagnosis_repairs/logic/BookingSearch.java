                        
                    /*─────────────────┐
                    │      author : ismail.      │
                    └─────────────────*/

package diagnosis_repairs.logic ;

import java.time.* ; // import packages needed

public class BookingSearch 
{
    
    private String    name ;
    private String    reg ;
    private LocalDate date ;
    
    public BookingSearch( String name , String reg , LocalDate date )
    {
        
        this.name = name ;
        this.reg  = reg ;
        this.date = date ;
        
    }
    
    public void setName( String name )
    {
        
        this.name = name ;
    
    }
    
    public void setReg( String reg )
    {
        
        this.reg = reg ;
    
    }
    
    public void setName( LocalDate date )
    {
        
        this.date = date ;
    
    }
    
    public String getName()
    {
        
        return name ;
        
    }
    
    public String getReg()
    {
        
        return reg ;
        
    }
    
    public LocalDate getDate()
    {
        
        return date ;
        
    }
    
}