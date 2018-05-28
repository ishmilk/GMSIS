
package vehicles.Logic;

/**
 *
 * @author Amy Dowse
 * A class that gives you the time and mechanic assigned to a booking - don't need all of the booking information 
 */
public class bookingDay 
{
    private String startTime;
    private String endTime;
    private String employeeID;
    
    public bookingDay(String startTime, String endTime, String employeeID)
    {
        this.startTime = startTime;
        this.endTime = endTime;
        this.employeeID = employeeID;
    }
    
    public String getStartTime()
    {
        return startTime;
    }
    
    public String getEndTime()
    {
        return endTime;
    }
    
    public String getEmployeeID()
    {
        return employeeID;
    }
    
    public void setStartTime(String startTime)
    {
        this.startTime = startTime;
    }
    
    public void setEndTime(String endTime)
    {
        this.endTime = endTime;
    }
    
    public void setEmployeeID(String employeeID)
    {
        this.employeeID = employeeID;
    }
    
    
    
}
