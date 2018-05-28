
package vehicles.Logic;

/**
 *
 * @author Amy Dowse
 * A class used when you want to know certain info about a booking and bill combined (not all of the info)
 * Date, start time, end time, total and payment status 
 */

public class Search 
{
    private int bookingID;
    private String Date;
    private String startTime;
    private String endTime;
    private String total;
    private String status;
    
    public Search(int ID, String Date, String startTime, String endTime)
    {
        this.bookingID = ID;
        this.Date = Date;
        this.startTime = startTime;
        this.endTime = endTime;
    }
    
    
    public String getDate()
    {
        return Date;
    }
    
    public String getStartTime()
    {
        return startTime;
    }
    
    public String getEndTime()
    {
        return endTime;
    }
    
    public String getTotal()
    {
        return total;
    }
    
    public int getBookingID()
    {
        return bookingID;
    }
    
    public String getStatus()
    {
        return status;
    }
    
    
    
    public void setDate(String newDate)
    {
        Date = newDate;
    }
    
    public void setStartTime(String newStartTime)
    {
        startTime = newStartTime;
    }
    
    public void setEndTime(String newEndTime)
    {
        endTime = newEndTime;
    }
    
    public void setTotal(String newCost)
    {
        total = newCost;
    }
    
    public void setStatus(String newStatus)
    {
        status = newStatus;
    }
}
