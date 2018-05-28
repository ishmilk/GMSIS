                        
                    /*─────────────────┐
                    │      author : ismail.      │
                    └─────────────────*/

package diagnosis_repairs.logic ;

import java.util.* ;            // import package needed for date operations
import java.time.* ;            // import packages needed
import java.time.format.* ;     //                        for time operations
import java.sql.* ;             // imports Java SQL package 

public class Booking 
{
    
    private int    ID ;
    private int    billID ;
    private LocalDate date ;
    private LocalTime startTime ;
    private LocalTime endTime ;
    private double cost ;
    private int    employeeID ;
    private String vehicleRegistration ;
    private boolean saturdaySelected = false ;
            
    // constructor
    public Booking( int ID , int billID , LocalDate date , LocalTime startTime , LocalTime endTime , double cost , int employeeID , String vehicleRegistration  )
    {
    
        this.ID                   = ID ;
        this.billID               = billID ;
        this.date                 = date ;
        this.startTime            = startTime ;
        this.endTime              = endTime ;
        this.cost                 = cost ;
        this.employeeID           = employeeID ;
        this.vehicleRegistration  = vehicleRegistration ;

    }
    
    // default constructor
    public Booking()
    {
    
        /**/
    
    }
    
    public static void main( String[] ismail )
    {
    
        //LocalTime timenow = LocalTime.of( 20 , 59  ) ;
        String time = "20:59" ;
        LocalTime timeReturned = toLocalTime( time ) ;
        System.out.println( timeReturned );
        System.out.println( "LocalTime with .toString() --> " + timeReturned.toString() );
        
        LocalTime timeNow = LocalTime.now() ;
        System.out.println( "LocalTime AFTER methods .trimLocalTime( timeNow ) ) --> " + trimLocalTime( timeNow ) );
        System.out.println() ;
        
        /*
        LocalDate datenow = LocalDate.now() ;
        String date = "27-02-2017" ;
        LocalDate dateReturned = LocalDate.of(2017 , 02 , 27 ) ;
        System.out.println( dateReturned ) ;
        */
        
        LocalDate localDate = LocalDate.now();
        DateTimeFormatter theFormat = DateTimeFormatter.ofPattern( "dd/MM/yyyy" );
        String formattedString = localDate.format(theFormat);
        System.out.println( formattedString ) ;
        
        System.out.println( "LocalDate AFTER method .toLocalDate() --> " + toLocalDate( formattedString ) ) ;
        
        //System.out.println( "hrs worked between 09:00 and 10:00 --> " + calculateHoursWorked( "09:00" , "10:30" ) ) ;
        
    }
    
    /* this overridden method returns the string representation of a booking obj */
    @Override
    public String toString() 
    {
    
        String toBeReturned = "" ;
        
        toBeReturned = toBeReturned + this.getID()        + " , " 
                                    + this.getDate()      + " , " 
                                    + this.getStartTime() + " , " 
                                    + this.getEndTime()   + " , " 
                                    + this.getCost()      + " , " 
                                    + this.getVehicleRegistration() ;
        
        return toBeReturned ;
    
    }
    
     /************************** getter methods **************************/
    
    // this method returns bookingID
    public int getID()
    {
    
        return ID ;
    
    }

    
    // this method returns date
    public LocalDate getDate()
    {
    
        return date ;
    
    }
    
    // this method returns startTime
    public LocalTime getStartTime()
    {
    
        return startTime ;
    
    }
    
    // this method returns endTime
    public LocalTime getEndTime()
    {
    
        return endTime ;
    
    }
    
    // this method returns cost
    public double getCost()
    {
    
        return cost ;
    
    }
    
    // this helper method returns vehicle registration
    // NEEDED for --> TableView's
    public String getVehicleRegistration()
    {
        return vehicleRegistration ;
    }
    
    // this CUSTOM GETTER method returns a mechanic's name concatented with asociated employeeID 
    // NEEDED for --> TableView's
    public String getEmployeeID() throws Exception
    {
        
        Connection c = common.DatabaseConnector.activateConnection();
        c.setAutoCommit( false ); 
        ResultSet rs ;
        PreparedStatement pstmt = c.prepareStatement( "SELECT employeeID , name FROM Employee WHERE employeeID = ? " ) ;
        pstmt.setInt( 1 , employeeID ) ;
        rs = pstmt.executeQuery();
        
        int empID   = rs.getInt( "employeeID" );
        String name = rs.getString( "name" );
        
        c.close() ;
        
        return name + " - " +  empID ;
    
    }
    
    // this helper method returns customer's name
    // NEEDED for --> TableView's
    public String getCustomerName() throws Exception
    {
    
        /*
            THE LINK = bookingID --> vehicleRegistration --> customerID --> customerName
        */
        
        Connection c = common.DatabaseConnector.activateConnection();
        c.setAutoCommit( false ); 
        ResultSet rs ;
        PreparedStatement pstmt = c.prepareStatement( " SELECT name FROM Vehicle , Customer WHERE registration = ? AND Vehicle.customerID = Customer.customerID " ) ;
        pstmt.setString( 1 , vehicleRegistration ) ;
        rs = pstmt.executeQuery();
        
        String customerName = rs.getString( "name" ) ;
        
        c.close() ;

        return customerName ;
    
    }
    
    /************************** setter methods **************************/
    
    // this method sets a new bookingID + returns TRUE on success , FALSE otherwise
    public boolean setID( int newBookingID )
    {
    
        try 
        {
        
            ID = newBookingID ;
            return true              ; // action succeded
        
        }
        
        catch ( Exception e ) 
        {
        
            e.printStackTrace()      ;
            return false             ; // action flopped
        
        }
    
    }
    
    // this method sets a new date + returns TRUE on success , FALSE otherwise
    public boolean setDate( String newBookingDate )
    {
    
        try 
        {
        
            date = toLocalDate( newBookingDate )   ;
            return true                            ; // action succeded
        
        }
        
        catch ( Exception e ) 
        {
        
            e.printStackTrace()                    ;
            return false                           ; // action flopped
        
        }
    
    }
    
    // this method sets a new startTime + returns TRUE on success , FALSE otherwise
    public boolean setStartTime( String newBookingStartTime )
    {
    
        try 
        {
        
            startTime = toLocalTime( newBookingStartTime ) ;
            return true                                    ; // action succeded
        
        }
        
        catch ( Exception e ) 
        {
        
            e.printStackTrace()                            ;
            return false                                   ; // action flopped
        
        }
    
    }
    
    // this method sets a new endTime + returns TRUE on success , FALSE otherwise
    public boolean setEndTime( String newBookingEndTime )
    {
    
        try 
        {
        
            endTime = toLocalTime( newBookingEndTime )     ;
            return true                                    ; // action succeded
        
        }
        
        catch ( Exception e ) 
        {
        
            e.printStackTrace()                            ;
            return false                                   ; // action flopped
        
        }
    
    }
    
    // this method sets a new cost + returns TRUE on success , FALSE otherwise
    public boolean setCost( double newBookingcost )
    {
    
        try 
        {
        
            cost = newBookingcost           ;
            return true                            ; // action succeded
        
        }
        
        catch ( Exception e ) 
        {
        
            e.printStackTrace()                    ;
            return false                           ; // action flopped
        
        }
    
    }
    
    /***************************************************************************/
    
    // this helper method trims a LocalTime to ( HH:MM ) + returns as a string
    public static String trimLocalTime( LocalTime time )
    {
    
        // convert LocalTime into a string
        String strTime = time.toString() ;
        
        // split the time components
        String strSplitTime[] = strTime.split( ":" ) ;
        
        return strSplitTime[ 0 ] + ":" + strSplitTime[ 1 ] ;
    
    }
    
    // this helper method converts a string into an equivalent LocalTime
    // for reverse --> use the .toString() method
    public static LocalTime toLocalTime( String time )
    {
 
        // split the string time into hr + min components
        String strLocalTime[] = time.split( ":" ) ;
                
        // convert strings --> integers
        int intLocalTime[] = new int[ 2 ] ;
        intLocalTime[ 0 ]  = Integer.parseInt( strLocalTime[ 0 ] ) ;
        intLocalTime[ 1 ]  = Integer.parseInt( strLocalTime[ 1 ] ) ;
        
        /* TODO : ADD ERROR CHECKING */
        
        LocalTime timeToReturn = LocalTime.of( intLocalTime[ 0 ] , intLocalTime[ 1 ] ) ;
        
        return timeToReturn ;
    
    }
    
    // this helper method gets the hr component from a given LocalTime
    public static String getStartHr( LocalTime time )
    {
    
        // convert to a string
        String strTime = time.toString() ;
        
        // split the startTime into 2 components --> hr + min
        String[] strStartTime = strTime.split( ":" ) ;
        
        // return hr component
        return strStartTime[ 0 ] ;
    
    }
    
    // this helper method gets the min component from a given LocalTime
    public static String getStartMin( LocalTime time )
    {
    
        // convert to a string
        String strTime = time.toString() ;
        
        // split the startTime into 2 components --> hr + min
        String[] strStartTime = strTime.split( ":" ) ;
        
        // return min component
        return strStartTime[ 1 ] ;
    
    }
    
    // this helper method gets the hr component from a given LocalTime
    public static String getEndHr( LocalTime time )
    {
    
        // convert to a string
        String strTime = time.toString() ;
        
        // split the startTime into 2 components --> hr + min
        String[] strEndTime = strTime.split( ":" ) ;
        
        // return hr component
        return strEndTime[ 0 ] ;
    
    }
    
    // this helper method gets the min component from a given LocalTime
    public static String getEndMin( LocalTime time )
    {
    
        // convert to a string
        String strTime = time.toString() ;
        
        // split the startTime into 2 components --> hr + min
        String[] strEndTime = strTime.split( ":" ) ;
        
        // return hr component
        return strEndTime[ 1 ] ;
    
    }
    
    // this helper method converts a string into an equivalent LocalDate
    // for reverse --> use the .fromLocalDate() method
    public static LocalDate toLocalDate( String date )
    {
    
        // string format --> dd/MM/YYYY
        
        // split the string date into day + month + year components
        String[] strLocalDate = date.split( "/" ) ;
        
        // convert strings --> integers
        int[] intLocalDate = new int[ 3 ] ;
        intLocalDate[ 0 ]  = Integer.parseInt( strLocalDate[ 0 ] ) ;
        intLocalDate[ 1 ]  = Integer.parseInt( strLocalDate[ 1 ] ) ;
        intLocalDate[ 2 ]  = Integer.parseInt( strLocalDate[ 2 ] ) ;
        
        LocalDate dateToReturn = LocalDate.of( intLocalDate[ 2 ] , intLocalDate[ 1 ] , intLocalDate[ 0 ] ) ;
        
        return dateToReturn ;
    
    }
    
    // this helper method formats a LocalDate into an equivalent string
    // for reverse --> use the .toLocalDate() method
    public static String fromLocalDate( LocalDate date )
    {
    
        // string format --> dd/MM/YYYY
        DateTimeFormatter theFormat = DateTimeFormatter.ofPattern( "dd/MM/yyyy" );
        
        // return the formatted date
        return date.format( theFormat ) ;
    
    }
    
    /* this helper method checks whether 2 given booking times are valid */
    public static int checkBookingTimes ( String startTime , String endTime )
    {
    
        LocalTime localStartTime = toLocalTime( startTime ) ;
        LocalTime localEndTime   = toLocalTime( endTime ) ;
        
        // check if startTime is before endTime
        if(  localEndTime.isBefore( localStartTime )  )     // endTime is BEFORE startTime
        {
        
            return 1 ;
        
        }
        
        // check if ( startTime == endTime )
        if (  localStartTime.equals( localEndTime )  )      // startTime is the same as endTime
        {
        
            return 2 ;
        
        }
        
        // no errors --> booking times chosen = VALID
        return 0 ;
    
    }
    
    /* this helper method determines whether the selected booking is in the past */
    public static boolean checkBookingDate( String strBookingDate , String strStartTime )
    {
        
        LocalDate bookingDate      = toLocalDate( strBookingDate ) ;
        LocalTime bookingStartTime = toLocalTime( strStartTime ) ;
        
        // spawn a LocalDateTime obj for comparison
        LocalDateTime bookingLDT = bookingDate.atTime( bookingStartTime ) ;

        // get a LocalDateTime obj for now
        LocalDateTime todayLDT = LocalDateTime.now() ;
            
        // if bookingDate is before today --> return TRUE , else FALSE
        return bookingLDT.isBefore( todayLDT ) ;
    
    }
    
    /* this helper method returns the latest booking added into the database */
    public static int getLatestBookingID() throws Exception
    {
        
        int ID = 0 ;
        
        Connection c = common.DatabaseConnector.activateConnection();
        c.setAutoCommit( true ); 
        Statement stmt = c.createStatement();
        ResultSet rs = stmt.executeQuery( "SELECT bookingID FROM Booking ORDER BY bookingID DESC " ) ;

        // get only the first record's bookingID
        ID = rs.getInt( "bookingID" ) ;
        
        c.close() ;  
        
        return ID ;
    
    }
    
}