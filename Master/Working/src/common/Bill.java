package common ;

public class Bill 
{
    
    private double  total     ;
    private String  status    ;
    private int     bookingID ;
    
    public Bill( double total , String Status , int bookingID )
    {
     
        this.total         = total ;
        this.status        = status ;
        this.bookingID     = bookingID ;
         
    }
    
    /***************************** GETTER METHODS *****************************/
    public double getTotal()
    {
        
        return total;
        
    }
    
    public String getStatus()
    {
    
        return status ;
    
    }

}