                        
                    /*─────────────────┐
                    │      author : ismail.      │
                    └─────────────────*/

package diagnosis_repairs.gui ;

import java.sql.*                    ; // imports Java SQL package 
import java.io.*                     ; // imports Java IO package
import java.net.*                    ; // imports Java NET package
import java.util.*                   ; // imports Java UTIL package
import java.time.*                   ; // imports Java TIME package needed for time operations
import java.time.format.*            ; // imports Java TIME formatting package
import diagnosis_repairs.logic.*     ; // imports diagnosis + repairs logic package

/******************************************************************************/

import javafx.application.*          ; // imports JavaFX application package
import javafx.beans.value.*          ; // imports JavaFX value package
import javafx.fxml.*                 ; // imports JavaFX .fxml package
import javafx.event.*                ; // imports JavaFX event package
import javafx.scene.*                ; // imports JavaFX scene package
import javafx.scene.layout.*         ; // imports JavaFX scene layout package
import javafx.scene.control.*        ; // imports JavaFX scene UI control package
import javafx.scene.control.cell.*   ; // imports JavaFX scene UI ( table ) control package
import javafx.scene.control.Alert.*  ; // imports JavaFX scene alert package
import javafx.scene.paint.*          ; // imports JavaFX scene colour + gradients package
import javafx.stage.*                ; // imports JavaFX stage package
import javafx.collections.*          ; // imports JavaFX collections package
import javafx.util.*                 ; // imports JavaFX utilities package


public class BookingController implements Initializable
{
    
    // injected variables
    @FXML private Button              btnBookingsByTheHr ;
    @FXML private Button              btnBookingsByTheDay ;
    @FXML private Button              btnBookingsByTheMonth ;
    @FXML private Button              btnAllBookings ;
    @FXML private Button              btnClosestBookings ;
    @FXML private ChoiceBox< String > mechanicDropDown ;
    @FXML private DatePicker          datePicker ;
    @FXML private ChoiceBox< String > startTimeHour ;
    @FXML private ChoiceBox< String > startTimeMin ;
    @FXML private ComboBox < String > endTimeHour ;
    @FXML private ChoiceBox< String > endTimeMin ;
    @FXML private TableView           bookingsTableView ;
    @FXML private TableColumn         dateColumn ; 
    @FXML private TableColumn         startTimeColumn ;
    @FXML private TableColumn         endTimeColumn ;
    @FXML private TableColumn         customerColumn ;
    @FXML private TableColumn         vehicleRegistrationColumn ;
    @FXML private TableColumn         mechanicColumn ;
    @FXML private TextField           txtCustomerName ;
    @FXML private TextField           txtVehicleRegistration ;
    
    /*************************************************************************/
    
    // regular variables
    private Booking selectedBooking         = null ;
    private boolean saturdaySelected        = false ;
    private boolean pastBookingSelected     = false ;
    private int     bookingTimesCheckPassed = 0 ;
    
    // run as soon as relevant scene is loaded into the stage ( the window )
    @Override
    public void initialize( URL location , ResourceBundle resources ) 
    {
        
        try
        {
            
            // implement restrictions on the datePicker
            datePicker.setDayCellFactory( dayCellFactory );
            
            // get the mechanics from the database
            this.getMechanics() ;
            
            // implement css
            //this.implementStylesheet() ;
            
            // loads time options into start + end time DropDown's
            // based on INSTANCE VARIABLE --> saturdaySelected
            this.loadTimeOptions() ;
            
            // gets the current bookings from the database
            this.getCurrentBookings() ;
            
        }
        
        catch ( Exception e )
        {
        
           e.printStackTrace() ;
        
        }
        
    }
    
    // this method shows bookings ONLY in the current hr
    @FXML
    private void filterBookingsByTheHr() throws Exception
    {
    
        // clear the table
        bookingsTableView.getItems().clear() ;
        
        Connection c = common.DatabaseConnector.activateConnection();
        c.setAutoCommit( true ); 
        Statement stmt = c.createStatement();
        ResultSet rs = stmt.executeQuery( "SELECT * FROM Booking" );
        
        ObservableList currentBookings = FXCollections.observableArrayList() ;
        
        while ( rs.next() )
        {
            
            int    bookingID           = rs.getInt   ( "bookingID"  ) ;
            int    billID              = rs.getInt   ( "billID" ) ;
            String bookingDate         = rs.getString( "date"       ) ;
            LocalDate bookingLocalDate = Booking.toLocalDate( bookingDate ) ;
            String startTime           = rs.getString( "startTime"  ) ;
            LocalTime bookingStartTime = Booking.toLocalTime( startTime ) ;
            String endTime             = rs.getString( "endTime"    ) ;
            LocalTime bookingEndTime   = Booking.toLocalTime( endTime ) ;
            double cost                = rs.getDouble( "cost" ) ;
            int employeeID             = rs.getInt   ( "employeeID" ) ;
            String vehicleRegistration = rs.getString( "vehicleRegistration" ) ;
            
            Booking b = new Booking( bookingID , billID , bookingLocalDate , bookingStartTime , bookingEndTime , cost , employeeID , vehicleRegistration ) ;
            
            currentBookings.add( b ) ;
            
        }
        
        dateColumn.setCellValueFactory                ( new PropertyValueFactory( "date" )                ) ;
        startTimeColumn.setCellValueFactory           ( new PropertyValueFactory( "startTime" )           ) ;
        endTimeColumn.setCellValueFactory             ( new PropertyValueFactory( "endTime" )             ) ;
        customerColumn.setCellValueFactory            ( new PropertyValueFactory( "customerName" )        ) ;
        vehicleRegistrationColumn.setCellValueFactory ( new PropertyValueFactory( "vehicleRegistration" ) ) ;
        mechanicColumn.setCellValueFactory            ( new PropertyValueFactory( "employeeID" )          ) ;
        
        /* .getBookingsForThisHr() returns all the bookings in the current hr */
        // put data into TableView
        bookingsTableView.setItems( getBookingsForThisHr( currentBookings ) ) ;
        
        // load components with info based on selected TableView row
        bookingsTableView.getSelectionModel().selectedItemProperty().addListener( ( ObservableValue v , Object oldValue , Object newValue ) ->
        {
            
            if ( newValue != null )
            {
            
                try
                {

                    // reference for completing a booking
                    selectedBooking = (Booking) newValue ;
                    
                    // store obj reference to access later is possible
                    CodeBank.storeObject( selectedBooking );
                    
                    txtCustomerName.setText       (  ( (Booking) newValue ).getCustomerName()         ) ;
                    txtVehicleRegistration.setText(  ( (Booking) newValue ).getVehicleRegistration()  ) ;
                    datePicker.setValue           (  ( (Booking) newValue ).getDate()                 ) ;
                    mechanicDropDown.setValue     (  ( (Booking) newValue ).getEmployeeID()           ) ;
                    LocalTime sTime =             (    (Booking) newValue ).getStartTime()              ;
                    LocalTime eTime =             (    (Booking) newValue ).getEndTime()                ;
                    startTimeHour.setValue        (     Booking. getStartHr ( sTime )                 ) ;
                    startTimeMin.setValue         (     Booking. getStartMin( sTime )                 ) ;
                    endTimeHour.setValue          (     Booking. getEndHr   ( eTime )                 ) ;
                    endTimeMin.setValue           (     Booking. getEndMin  ( eTime )                 ) ;

                }

                catch ( Exception e )
                {

                    e.printStackTrace() ;

                }
            
            }
            
            else
            {
                
                //
                
            }
            
        });
        
        // clear the selection
        selectedBooking = null ;
        
        c.close() ;
        
        System.out.println( "** SHOWING BOOKINGS BY THE HR **" ) ;
    
    }
    
    // this method shows bookings ONLY in the current day
    @FXML
    private void filterBookingsByTheDay() throws Exception
    {
    
        // clear the table
        bookingsTableView.getItems().clear() ;
        
        Connection c = common.DatabaseConnector.activateConnection();
        c.setAutoCommit( true ); 
        Statement stmt = c.createStatement();
        ResultSet rs = stmt.executeQuery( "SELECT * FROM Booking" );
        
        ObservableList currentBookings = FXCollections.observableArrayList() ;
        
        while ( rs.next() )
        {
            
            int    bookingID           = rs.getInt   ( "bookingID"  ) ;
            int    billID              = rs.getInt   ( "billID" ) ;
            String bookingDate         = rs.getString( "date"       ) ;
            LocalDate bookingLocalDate = Booking.toLocalDate( bookingDate ) ;
            String startTime           = rs.getString( "startTime"  ) ;
            LocalTime bookingStartTime = Booking.toLocalTime( startTime ) ;
            String endTime             = rs.getString( "endTime"    ) ;
            LocalTime bookingEndTime   = Booking.toLocalTime( endTime ) ;
            double cost                = rs.getDouble( "cost" ) ;
            int employeeID             = rs.getInt   ( "employeeID" ) ;
            String vehicleRegistration = rs.getString( "vehicleRegistration" ) ;
            
            Booking b = new Booking( bookingID , billID , bookingLocalDate , bookingStartTime , bookingEndTime , cost , employeeID , vehicleRegistration ) ;
            
            currentBookings.add( b ) ;
            
        }
        
        dateColumn.setCellValueFactory                ( new PropertyValueFactory( "date" )                ) ;
        startTimeColumn.setCellValueFactory           ( new PropertyValueFactory( "startTime" )           ) ;
        endTimeColumn.setCellValueFactory             ( new PropertyValueFactory( "endTime" )             ) ;
        customerColumn.setCellValueFactory            ( new PropertyValueFactory( "customerName" )        ) ;
        vehicleRegistrationColumn.setCellValueFactory ( new PropertyValueFactory( "vehicleRegistration" ) ) ;
        mechanicColumn.setCellValueFactory            ( new PropertyValueFactory( "employeeID" )          ) ;
        
        /* .getBookingsForToday() returns all the bookings in the current day */
        // put data into TableView
        bookingsTableView.setItems( getBookingsForToday( currentBookings ) ) ;
        
        // load components with info based on selected TableView row
        bookingsTableView.getSelectionModel().selectedItemProperty().addListener( ( ObservableValue v , Object oldValue , Object newValue ) ->
        {
            
            if ( newValue != null )
            {
            
                try
                {

                    // reference for completing a booking
                    selectedBooking = (Booking) newValue ;
                    
                    // store obj reference to access later is possible
                    CodeBank.storeObject( selectedBooking );
                    
                    txtCustomerName.setText       (  ( (Booking) newValue ).getCustomerName()         ) ;
                    txtVehicleRegistration.setText(  ( (Booking) newValue ).getVehicleRegistration()  ) ;
                    datePicker.setValue           (  ( (Booking) newValue ).getDate()                 ) ;
                    mechanicDropDown.setValue     (  ( (Booking) newValue ).getEmployeeID()           ) ;
                    LocalTime sTime =             (    (Booking) newValue ).getStartTime()              ;
                    LocalTime eTime =             (    (Booking) newValue ).getEndTime()                ;
                    startTimeHour.setValue        (     Booking. getStartHr ( sTime )                 ) ;
                    startTimeMin.setValue         (     Booking. getStartMin( sTime )                 ) ;
                    endTimeHour.setValue          (     Booking. getEndHr   ( eTime )                 ) ;
                    endTimeMin.setValue           (     Booking. getEndMin  ( eTime )                 ) ;

                }

                catch ( Exception e )
                {

                    e.printStackTrace() ;

                }
            
            }
            
            else
            {
                
                //
                
            }
            
        });
        
        // clear the selection
        selectedBooking = null ;
        
        c.close() ;
        
        System.out.println( "** SHOWING BOOKINGS BY THE DAY **" ) ;
    
    }
    
    // this method shows bookings ONLY in the current month
    @FXML
    private void filterBookingsByTheMonth() throws Exception
    {
    
        // clear the table
        bookingsTableView.getItems().clear() ;
        
        Connection c = common.DatabaseConnector.activateConnection();
        c.setAutoCommit( true ); 
        Statement stmt = c.createStatement();
        ResultSet rs = stmt.executeQuery( "SELECT * FROM Booking" );
        
        ObservableList currentBookings = FXCollections.observableArrayList() ;
        
        while ( rs.next() )
        {
            
            int    bookingID           = rs.getInt   ( "bookingID"  ) ;
            int    billID              = rs.getInt   ( "billID" ) ;
            String bookingDate         = rs.getString( "date"       ) ;
            LocalDate bookingLocalDate = Booking.toLocalDate( bookingDate ) ;
            String startTime           = rs.getString( "startTime"  ) ;
            LocalTime bookingStartTime = Booking.toLocalTime( startTime ) ;
            String endTime             = rs.getString( "endTime"    ) ;
            LocalTime bookingEndTime   = Booking.toLocalTime( endTime ) ;
            double cost                = rs.getDouble( "cost" ) ;
            int employeeID             = rs.getInt   ( "employeeID" ) ;
            String vehicleRegistration = rs.getString( "vehicleRegistration" ) ;
            
            Booking b = new Booking( bookingID , billID , bookingLocalDate , bookingStartTime , bookingEndTime , cost , employeeID , vehicleRegistration ) ;
            
            currentBookings.add( b ) ;
            
        }
        
        dateColumn.setCellValueFactory                ( new PropertyValueFactory( "date" )                ) ;
        startTimeColumn.setCellValueFactory           ( new PropertyValueFactory( "startTime" )           ) ;
        endTimeColumn.setCellValueFactory             ( new PropertyValueFactory( "endTime" )             ) ;
        customerColumn.setCellValueFactory            ( new PropertyValueFactory( "customerName" )        ) ;
        vehicleRegistrationColumn.setCellValueFactory ( new PropertyValueFactory( "vehicleRegistration" ) ) ;
        mechanicColumn.setCellValueFactory            ( new PropertyValueFactory( "employeeID" )          ) ;
        
        /* .getBookingsForThisMonth() returns all the bookings in the current month */
        // put data into TableView
        bookingsTableView.setItems( getBookingsForThisMonth( currentBookings ) ) ;
        
        // load components with info based on selected TableView row
        bookingsTableView.getSelectionModel().selectedItemProperty().addListener( ( ObservableValue v , Object oldValue , Object newValue ) ->
        {
            
            if ( newValue != null )
            {
            
                try
                {

                    // reference for completing a booking
                    selectedBooking = (Booking) newValue ;
                    
                    // store obj reference to access later is possible
                    CodeBank.storeObject( selectedBooking );
                    
                    txtCustomerName.setText       (  ( (Booking) newValue ).getCustomerName()         ) ;
                    txtVehicleRegistration.setText(  ( (Booking) newValue ).getVehicleRegistration()  ) ;
                    datePicker.setValue           (  ( (Booking) newValue ).getDate()                 ) ;
                    mechanicDropDown.setValue     (  ( (Booking) newValue ).getEmployeeID()           ) ;
                    LocalTime sTime =             (    (Booking) newValue ).getStartTime()              ;
                    LocalTime eTime =             (    (Booking) newValue ).getEndTime()                ;
                    startTimeHour.setValue        (     Booking. getStartHr ( sTime )                 ) ;
                    startTimeMin.setValue         (     Booking. getStartMin( sTime )                 ) ;
                    endTimeHour.setValue          (     Booking. getEndHr   ( eTime )                 ) ;
                    endTimeMin.setValue           (     Booking. getEndMin  ( eTime )                 ) ;

                }

                catch ( Exception e )
                {

                    e.printStackTrace() ;

                }
            
            }
            
            else
            {
                
                //
                
            }
            
        });
        
        // clear the selection
        selectedBooking = null ;
        
        c.close() ;
        
        System.out.println( "** SHOWING BOOKINGS BY THE MONTH **" ) ;
    
    }
    
    // this method shows All bookings
    @FXML
    private void filterBookingsByNothing() throws Exception
    {
    
        this.getCurrentBookings() ;
        
        // clear the selection
        selectedBooking = null ;
        
        System.out.println( "** SHOWING ALL BOOKINGS **" ) ;
    
    }
    
    // this method filters the bookings to show the most immediate ones
    @FXML
    private void filterBookingsByTimeProximity() throws Exception
    {
        
        
        // clear the table
        bookingsTableView.getItems().clear() ;
        
        // variables to initialise
        ObservableList closestBookings = FXCollections.observableArrayList() ;
        ObservableList allBookings     = null ;
        ObservableList futureBookings  = null ;
       
        // distinctly get vehicle registrations which have a booking 
        List<String> vehiclesInBooking = this.getDistinctVehicles() ;
        
        // iterate through vehiclesInBooking
        for ( int t = 0 ; t < vehiclesInBooking.size() ; t = t + 1)
        {
        
            // get all bookings for a vehicle
            allBookings = this.getVehicleBookings( vehiclesInBooking.get( t ) ) ;
            
            // for a vehicle --> get the bookings in the future
            futureBookings = this.getFutureBookings( allBookings ) ;
            
            // check if there are any future bookings
            if ( futureBookings.isEmpty() )             // there !are future bookings for this vehicle
            {

                System.out.println( "** THERE ARE NO FUTURE BOOKINGS FOR THIS VEHICLE **" ) ; 

            }
            
            else                                        // there are future bookings for this vehicle
            {
            
                // from the List of future bookings --> get the closest booking + add onto List to view
                closestBookings.add( this.getImminentBooking( futureBookings ) ) ;
            
            }
        
        }
        
        // put data into TableView
        bookingsTableView.setItems( closestBookings ) ;
        
        // load components with info based on selected TableView row
        bookingsTableView.getSelectionModel().selectedItemProperty().addListener( ( ObservableValue v , Object oldValue , Object newValue ) ->
        {
            
            if ( newValue != null )
            {
            
                try
                {

                    // reference for completing a booking
                    selectedBooking = (Booking) newValue ;
                    
                    // store obj reference to access later is possible
                    CodeBank.storeObject( selectedBooking );
                    
                    txtCustomerName.setText       (  ( (Booking) newValue ).getCustomerName()         ) ;
                    txtVehicleRegistration.setText(  ( (Booking) newValue ).getVehicleRegistration()  ) ;
                    datePicker.setValue           (  ( (Booking) newValue ).getDate()                 ) ;
                    mechanicDropDown.setValue     (  ( (Booking) newValue ).getEmployeeID()           ) ;
                    LocalTime sTime =             (    (Booking) newValue ).getStartTime()              ;
                    LocalTime eTime =             (    (Booking) newValue ).getEndTime()                ;
                    startTimeHour.setValue        (     Booking. getStartHr ( sTime )                 ) ;
                    startTimeMin.setValue         (     Booking. getStartMin( sTime )                 ) ;
                    endTimeHour.setValue          (     Booking. getEndHr   ( eTime )                 ) ;
                    endTimeMin.setValue           (     Booking. getEndMin  ( eTime )                 ) ;

                }

                catch ( Exception e )
                {

                    e.printStackTrace() ;

                }
            
            }
            
            else
            {
                
                //
                
            }
            
        });
        
        // clear the selection
        selectedBooking = null ;
        
    }
    
    // this method opens the pane related to add bookings
    @FXML
    private void spawnBooking() throws Exception
    {
        
        // get the new pane to be loaded
        Pane spawnBookingPane = FXMLLoader.load( getClass().getResource( "AddBooking.fxml" ) );
        
        // load the new pane into the base pane
        common.FXMLDocumentController.changeContentPane( spawnBookingPane );
        
    }
    
    // this method opens the pane related to complete a booking
    @FXML
    private void completeBooking() throws Exception
    {
        
        /* TODO : SAVE CURRENTLY SELECTED BOOKING VIA INSTANCE VARIABLE */
        
        /* check to see if user has selected a record */
        if ( selectedBooking == null ) // booking !selected
        {
        
            /* .showErrorAlert( String T , String C ) has 2 parameters --> the title T and the content C */
            CodeBank.showErrorAlert( "YOU DIDN'T SELECT A BOOKING" , "You MUST select a booking to complete" ) ;
        
        }
        
        else                           // booking selected
        {
        
            // get the new pane to be loaded
            Pane spawnBookingPane = FXMLLoader.load( getClass().getResource( "CompleteBooking.fxml" ) );

            // load the new pane into the base pane
            common.FXMLDocumentController.changeContentPane( spawnBookingPane );
            
        }
        
    }
    
    // this method updates the currently selected record of booking
    @FXML
    private void saveBooking() throws Exception
    {
        
        /* TODO : SAVE CURRENTLY SELECTED BOOKING VIA INSTANCE VARIABLE */
        
        /* check to see if user has selected a record */
        if ( selectedBooking == null ) // booking !selected
        {
        
            /* .showErrorAlert( String T , String C ) has 2 parameters --> the title T and the content C */
            CodeBank.showErrorAlert( "YOU DIDN'T SELECT A BOOKING" , "You MUST select a booking to edit" ) ;
        
        }
        
        else
        {
            
            pastBookingSelected = Booking.checkBookingDate( Booking.fromLocalDate( selectedBooking.getDate() ) , selectedBooking.getStartTime().toString() ) ;
            
            /* checks to see if user is trying to edit a past booking */
            if ( pastBookingSelected )
            {
            
                /* .showErrorAlert( String T , String C ) has 2 parameters --> the title T and the content C */
                CodeBank.showErrorAlert( "YOU CAN'T CHANGE THE PAST" , "You CANNOT edit a booking in the past" ) ;
            
            }
            
            else
            {
            
                int startHr  = Integer.parseInt( startTimeHour.getValue() ) ;
                int startMin = Integer.parseInt( startTimeMin.getValue() ) ;
                int endHr    = Integer.parseInt( endTimeHour.getValue() ) ;
                int endMin   = Integer.parseInt( endTimeMin.getValue() ) ;

                bookingTimesCheckPassed = Booking.checkBookingTimes( startHr + ":" + startMin , endHr + ":" + endMin ) ;

                /*
                    bookingTimesCheckPassed == 1 means --> start time is before end time
                    bookingTimesCheckPassed == 2 means --> start time is the same as end time
                */

                /* check the format of the booking times */
                if ( bookingTimesCheckPassed == 1 )     
                {

                    // red hex code = #ff0000
                    startTimeHour.setStyle( " -fx-border-color : #ff0000 ; " ) ;
                    startTimeMin.setStyle( " -fx-border-color : #ff0000 ; " ) ;
                    endTimeHour.setStyle( " -fx-border-color : #ff0000 ; " ) ;
                    endTimeMin.setStyle( " -fx-border-color : #ff0000 ; " ) ;

                    /* .showErrorAlert( String T , String C ) has 2 parameters --> the title T and the content C */
                    CodeBank.showErrorAlert( "BOOKING TIME ERROR" , "Start time CANNOT be before end time" ) ;

                }

                else if ( bookingTimesCheckPassed == 2 )
                {

                    // red hex code = #ff0000
                    startTimeHour.setStyle( " -fx-border-color : #ff0000 ; " ) ;
                    startTimeMin.setStyle( " -fx-border-color : #ff0000 ; " ) ;
                    endTimeHour.setStyle( " -fx-border-color : #ff0000 ; " ) ;
                    endTimeMin.setStyle( " -fx-border-color : #ff0000 ; " ) ;

                    /* .showErrorAlert( String T , String C ) has 2 parameters --> the title T and the content C */
                    CodeBank.showErrorAlert( "BOOKING TIME ERROR" , "Start time CANNOT be equal to end time" ) ;

                }

                else
                {

                    int bookingID = selectedBooking.getID() ;
                    Connection c = common.DatabaseConnector.activateConnection();
                    c.setAutoCommit( true ); 
                    PreparedStatement pstmt = c.prepareStatement( "UPDATE Booking SET date = ? , startTime = ? , endTime = ? , employeeID = ? WHERE bookingID = ? " ) ;

                    // date formatting
                    LocalDate bookingDate       = datePicker.getValue() ;
                    DateTimeFormatter theFormat = DateTimeFormatter.ofPattern( "dd/MM/yyyy" );
                    String strBookingDate       = bookingDate.format( theFormat );

                    // the date
                    pstmt.setString( 1 , strBookingDate );

                    // the start time
                    pstmt.setString( 2 , startTimeHour.getValue() + ":" + startTimeMin.getValue() );

                    // the end time
                    pstmt.setString( 3 , endTimeHour.getValue() + ":" +  endTimeMin.getValue() );

                    // the employeeID
                    pstmt.setInt   ( 4 , extractEmployeeID( mechanicDropDown.getValue() ) );

                    // the bookingID
                    pstmt.setInt   ( 5 , bookingID );

                    // execute the query
                    pstmt.execute() ;

                    //close the connection
                    c.close();

                    // clear the table
                    bookingsTableView.getItems().clear();

                    // clear the selected option
                    selectedBooking = null ;

                    // refresh table to visualise changes
                    this.getCurrentBookings();

                    System.out.println( "** SUCCESS :) - UPDATED A CURRENT BOOKING FROM DATABASE **" ) ;
                    
                }
            
            }
        
        }
    
    }
    
    // this procedure is used to delete a booking in the system
    @FXML
    public void deleteBooking() throws Exception
    {
        
        /* TODO : SAVE CURRENTLY SELECTED BOOKING VIA INSTANCE VARIABLE */
    
        /* check to see if user has selected a record */
        if ( selectedBooking == null ) // booking !selected
        {
        
            /* .showErrorAlert( String T , String C ) has 2 parameters --> the title T and the content C */
            CodeBank.showErrorAlert( "YOU DIDN'T SELECT A BOOKING" , "You MUST select a booking to delete" ) ;
        
        }
        
        else
        {
        
            int bookingID = selectedBooking.getID() ;
        
            /* .showConfirmationAlert( String T , String C ) has 2 parameters --> the title T and the content C */
            Optional<ButtonType> result = CodeBank.showConfirmationAlert( " DELETE BOOKING ? " , " To delete the booking --> press OK " ) ;

            if ( result.get() == ButtonType.OK )
            {

                /* method which allows foreign key constraints to be enforced */ 
                Connection c = common.DatabaseConnector.activateCascadedConnection();
                c.setAutoCommit( true ); 
                Statement stmt = c.createStatement();
                ResultSet rs ;
                //System.out.println(bookingID);
                stmt.executeUpdate( "DELETE FROM Booking WHERE bookingID = " + bookingID ) ;
                c.close() ;
                
                // clear the selected option
                selectedBooking = null ;

                // refresh booking list
                this.getCurrentBookings() ;

                System.out.println( "** BOOKING HAS BEEN DELETED **" ) ;

            }

            else 
            {
                
                System.out.println( "** DELETION CANCELLED **" ) ;

            }
        
        }
    
    }
    
    /* this helper method sieves through the parameter + returns list of bookings ONLY in this hr */
    private ObservableList getBookingsForThisHr( ObservableList bookings )
    {
        
        // final list to be returned
        ObservableList bookingsThisHr = FXCollections.observableArrayList() ; 
        
        // get the day we are currently in
        LocalDate today = LocalDate.now() ;
        int currentDay  = today.getDayOfMonth();
        
        // get the hr we are currently in
        LocalTime timeNow = Booking.toLocalTime(  Booking.trimLocalTime( LocalTime.now() )  ) ;
        int currentHr     = timeNow.getHour() ;
        
        for ( int h = 0 ; h < bookings.size() ; h = h + 1 )
        {
        
            // temp booking obj + booking start + end time
            Booking tempBooking       = ( Booking )bookings.get( h ) ;
            LocalDate tempBookingDate = tempBooking.getDate() ;
            LocalTime startTime       = tempBooking.getStartTime() ;
            LocalTime endTime         = tempBooking.getEndTime() ;            
            
            // check we are in the current day
            if ( tempBookingDate.getDayOfMonth() == currentDay ) /* we ARE in current day */
            {
            
                // check we are in the current hr IN THE CURRENT DAY
                if ( startTime.getHour() == currentHr || endTime.getHour() == currentHr ) /* we ARE in current day */
                {

                    bookingsThisHr.add( tempBooking ) ;

                }
            
            }
        
        }
        
        return bookingsThisHr ;
    
    }
    
    /* this helper method sieves through the parameter + returns list of bookings ONLY in this month */
    private ObservableList getBookingsForToday( ObservableList bookings )
    {
        
        // final list to be returned
        ObservableList bookingsToday = FXCollections.observableArrayList() ; 
        
        // get the day we are currently in
        LocalDate today = LocalDate.now() ;
        int currentDay  = today.getDayOfMonth();
        
        for ( int d = 0 ; d < bookings.size() ; d = d + 1 )
        {
        
            // temp booking obj + booking date
            Booking tempBooking       = ( Booking )bookings.get( d ) ;
            LocalDate tempBookingDate = tempBooking.getDate() ;
            
            // check we are in the current day
            if ( tempBookingDate.getDayOfMonth() == currentDay ) /* we ARE in current day */
            {
            
                bookingsToday.add( tempBooking ) ;
            
            }
        
        }
        
        return bookingsToday ;
    
    }
    
    /* this helper method sieves through the parameter + returns list of bookings ONLY in this month */
    private ObservableList getBookingsForThisMonth( ObservableList bookings )
    {
        
        // final list to be returned
        ObservableList bookingsThisMonth = FXCollections.observableArrayList() ; 
        
        // get the month we are currently in
        LocalDate today    = LocalDate.now() ;
        Month currentMonth = today.getMonth() ;
        
        for ( int m = 0 ; m < bookings.size() ; m = m + 1 )
        {
        
            // temp booking obj + booking date
            Booking tempBooking       = ( Booking )bookings.get( m ) ;
            LocalDate tempBookingDate = tempBooking.getDate() ;
            
            // check we are in the current month
            if ( tempBookingDate.getMonth() == currentMonth ) /* we ARE in current month */
            {
            
                bookingsThisMonth.add( tempBooking ) ;
            
            }
        
        }
        
        return bookingsThisMonth ;
    
    }
    
    /* this helper method returns all the DISTINCT vehicles in the booking table */
    private List getDistinctVehicles() throws Exception
    {
    
        // vehicles to return
        List<String> toBeReturned = new ArrayList<>() ;
        
        Connection c = common.DatabaseConnector.activateConnection();
        c.setAutoCommit( true ); 
        Statement stmt = c.createStatement();
        ResultSet rs = stmt.executeQuery( "SELECT DISTINCT vehicleRegistration FROM Booking " ) ;
        
        while ( rs.next() )
        {
        
            toBeReturned.add( rs.getString( "vehicleRegistration" ) ) ;
        
        }
        
        c.close() ;       
        
        /* THIS METHOD IS CHECKED */
        for( String VR : toBeReturned )
        {
        
            System.out.println( "distinct vehicle which has booking --> " + VR ) ;
        
        }
        
        return toBeReturned ;
    
    }
    
    /* this helper method returns all bookings for a given vehicle registration */
    private ObservableList getVehicleBookings( String vehicleRegistration ) throws Exception
    {
    
        Connection c = common.DatabaseConnector.activateConnection() ;
        c.setAutoCommit( false ) ; 
        ResultSet rs ;
        PreparedStatement pstmt = c.prepareStatement( "SELECT * FROM Booking WHERE vehicleRegistration = ? " ) ;
        pstmt.setString( 1 , vehicleRegistration ) ;
        rs = pstmt.executeQuery() ;
        
        // bookings to return
        ObservableList toBeReturned = FXCollections.observableArrayList() ;
        
        while ( rs.next() )
        {
            
            int    bookingID           = rs.getInt   ( "bookingID"  ) ;
            int    billID              = rs.getInt   ( "billID" ) ;
            String bookingDate         = rs.getString( "date"       ) ;
            LocalDate bookingLocalDate = Booking.toLocalDate( bookingDate ) ;
            String startTime           = rs.getString( "startTime"  ) ;
            LocalTime bookingStartTime = Booking.toLocalTime( startTime ) ;
            String endTime             = rs.getString( "endTime"    ) ;
            LocalTime bookingEndTime   = Booking.toLocalTime( endTime ) ;
            double cost                = rs.getDouble( "cost" ) ;
            int employeeID             = rs.getInt   ( "employeeID" ) ;
            
            Booking b = new Booking( bookingID , billID , bookingLocalDate , bookingStartTime , bookingEndTime , cost , employeeID , vehicleRegistration ) ;
            
            toBeReturned.add( b ) ;
            
        }
        
        c.close() ;
        
        /* THIS METHOD IS CHECKED */
        for ( Object obj : toBeReturned )
        {
        
            Booking b = ( Booking ) obj ;
            
            System.out.println( b.toString() ) ;
        
        }
        
        return toBeReturned ;
    
    }
    
    /* this helper method returns a vehicle's future bookings from a List containing all bookings */
    private ObservableList getFutureBookings( ObservableList allBookings )
    {
    
        // bookings to return
        ObservableList toBeReturned = FXCollections.observableArrayList() ;
        
        // get today's DateTime
        LocalDateTime todayLDT = LocalDateTime.now() ;
        
        // iterate through allBookings
        for ( int f = 0 ; f < allBookings.size() ; f = f + 1 )
        {
        
            Booking tempBooking      = ( Booking ) allBookings.get( f ) ;
            LocalDate date           = tempBooking.getDate() ;
            LocalTime startTime      = tempBooking.getStartTime() ;
            LocalDateTime bookingLDT = date.atTime( startTime ) ;
            
            // check if booking date is after today
            if ( bookingLDT.isAfter( todayLDT ) )
            {
            
                toBeReturned.add( tempBooking ) ;
            
            }
        
        }
        
        /* THIS METHOD HAS BEEN CHECKED */
        for ( Object obj : toBeReturned )
        {
        
            Booking b = ( Booking ) obj ;
            
            System.out.println( b.toString() ) ;
        
        }
        
        return toBeReturned ;
    
    }
    
    /* this helper method returns the closest booking from a List of future bookings */
    private Booking getImminentBooking( ObservableList futureBookings )
    {
        
        System.out.println( " size of ObservableList just passed through --> " + futureBookings.size() ) ;
        
        // get LocalDateTime for the 1st booking in futureBookings
        Booking imminentBooking          = ( Booking ) futureBookings.get( 0 ) ;
        LocalDate bookingDate            = imminentBooking.getDate() ;
        LocalTime startTime              = imminentBooking.getStartTime() ;
        LocalDateTime imminentBookingLDT = bookingDate.atTime( startTime ) ;
        
        // iterate through futureBookings
        for ( int i = 1 ; i < futureBookings.size() ; i = i + 1 )
        {
        
            Booking tempBooking          = ( Booking ) futureBookings.get( i ) ;
            LocalDate bDate              = tempBooking.getDate() ;
            LocalTime sTime              = tempBooking.getStartTime() ;
            LocalDateTime tempBookingLDT = bDate.atTime( sTime ) ;
            
            // check if the current imminent booking is after tempBooking
            if( imminentBookingLDT.isBefore( tempBookingLDT ) )      // current booking is the closest so far
            {
            
                /* DO NOTHING */
                
            }
            
            else                                                    // found a new closest booking
            {
                
                
                imminentBooking = tempBooking ;
            
            }
        
        }
        
        return imminentBooking ;
    
    }
    
    /* this helper method loads a css stylesheet into the scene */
    private boolean implementStylesheet()
    {
    
        try                              // implemented css
        {   
            
            // get the current scene
            Scene currentScene = mechanicDropDown.getScene() ;
            
            System.out.println( currentScene ) ;
        
            // .getStylesheets() + .add() allow you to insert custom stylesheets
            currentScene.getStylesheets().add( "/diagnosis_repairs/gui/bookingStylesheet.css" ) ;
            
            return true ;
        
        }
        
        catch ( Exception e )            // failed to implement css
        {
        
            e.printStackTrace() ;
            return false ;
        
        }
    
    }
    
    // this helper method returns the mechanics stored in the database
    private void getMechanics() throws Exception
    {
    
        Connection c = common.DatabaseConnector.activateConnection();
        c.setAutoCommit( true ); 
        Statement stmt = c.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT employeeID , name , isMechanic FROM Employee" );
        
        // load mechanics into drop down
        while ( rs.next() )
        {
            
            int     employeeID      = rs.getInt    ( "employeeID" ) ;
            String  mechanicName    = rs.getString ( "name" )       ;
            boolean isAMechanic     = rs.getBoolean( "isMechanic" ) ;
            //System.out.println(isAMechanic);
            if ( isAMechanic ) // employee is a mechanic so 
            {

                // add into obserablelist
                mechanicDropDown.getItems().add( mechanicName + " - " + employeeID );
                
                // set a prompt txt 
                mechanicDropDown.setValue( mechanicName + " - " + employeeID ) ;

            }

        }

        // close connection
        c.close() ;
    
    }
    
    // this helper method loads ChoiceBox ( + 1 ComboBox ) options into ChoiceBox's ( + 1 ComboBox ) considering date restraints
    /* TODO : check for overlapping */
    private void loadTimeOptions()
    {
        
        System.out.println( "** ENTERED .loadTimeOptions() **" ) ;
        // clear ALL previous selections
        startTimeHour.getItems().clear() ;
        startTimeMin.getItems().clear()  ;
        endTimeHour.getItems().clear()   ;
        endTimeMin.getItems().clear()    ;
        
        if ( saturdaySelected ) // saturday selected --> so different time restrictions
        {
            
            // load the available start hrs + mins
            startTimeHour.getItems().addAll( "09" , "10" , "11" , "12" ) ;
            startTimeMin.getItems().addAll ( "00" , "15" , "30" , "45" ) ;
            // set a default hr + min
            startTimeHour.setValue( "09" ) ;
            startTimeMin.setValue ( "00" ) ;

            // load the available end hrs + mins
            endTimeHour.getItems().addAll( "09" , "10" , "11" , "12" ) ;
            endTimeMin.getItems().addAll ( "00" , "15" , "30" , "45" ) ;
            // set a default hr + min
            endTimeHour.setValue( "09" ) ;
            endTimeMin.setValue ( "00" ) ;
        
        }
        
        else                    // saturday NOT selected --> so regular time restrictions
        {
        
            // load the available start hrs + mins
            startTimeHour.getItems().addAll( "09" , "10" , "11" , "12" , "13" , "14" , "15" , "16" , "17" ) ;
            startTimeMin.getItems().addAll ( "00" , "15" , "30" , "45" ) ;
            // set a default hr + min
            startTimeHour.setValue( "09" ) ;
            startTimeMin.setValue ( "00" ) ;

            // load the available end hrs + mins
            endTimeHour.getItems().addAll( "09" , "10" , "11" , "12" , "13" , "14" , "15" , "16" , "17" ) ;
            endTimeMin.getItems().addAll ( "00" , "15" , "30" , "45" ) ;
            // set a default hr + min
            endTimeHour.setValue( "09" ) ;
            endTimeMin.setValue ( "00" ) ;
        
        }
    
    }
    
    // this helper method displays the current bookings
    private void getCurrentBookings() throws Exception
    {
    
        Connection c = common.DatabaseConnector.activateConnection();
        c.setAutoCommit( true ); 
        Statement stmt = c.createStatement();
        ResultSet rs = stmt.executeQuery( "SELECT * FROM Booking" );
        
        ObservableList currentBookings = FXCollections.observableArrayList() ;
        
        while ( rs.next() )
        {
            
            int    bookingID           = rs.getInt   ( "bookingID"  ) ;
            int    billID              = rs.getInt   ( "billID" ) ;
            String bookingDate         = rs.getString( "date"       ) ;
            LocalDate bookingLocalDate = Booking.toLocalDate( bookingDate ) ;
            String startTime           = rs.getString( "startTime"  ) ;
            LocalTime bookingStartTime = Booking.toLocalTime( startTime ) ;
            String endTime             = rs.getString( "endTime"    ) ;
            LocalTime bookingEndTime   = Booking.toLocalTime( endTime ) ;
            double cost                = rs.getDouble( "cost" ) ;
            int employeeID             = rs.getInt   ( "employeeID" ) ;
            String vehicleRegistration = rs.getString( "vehicleRegistration" ) ;
            
            Booking b = new Booking( bookingID , billID , bookingLocalDate , bookingStartTime , bookingEndTime , cost , employeeID , vehicleRegistration ) ;
            
            currentBookings.add( b ) ;
            
        }
        
        c.close() ;
        
        /* 
            the PropertyValueFactory --> MUST MUST MUST --> 
            take EXACTLY the same argument NAME from the class the TableView is associated to 
            the SETTERS + GETTERS MUST MUST MUST be in the format --> .getXXX()  +  .setXXX()
            where the 1st 'X' is a capital letter / sign ---> YOU MUST FOLLOW JAVA NAMING CONVENTIONS
        */
        
        dateColumn.setCellValueFactory                ( new PropertyValueFactory( "date" )                ) ;
        startTimeColumn.setCellValueFactory           ( new PropertyValueFactory( "startTime" )           ) ;
        endTimeColumn.setCellValueFactory             ( new PropertyValueFactory( "endTime" )             ) ;
        customerColumn.setCellValueFactory            ( new PropertyValueFactory( "customerName" )        ) ;
        vehicleRegistrationColumn.setCellValueFactory ( new PropertyValueFactory( "vehicleRegistration" ) ) ;
        mechanicColumn.setCellValueFactory            ( new PropertyValueFactory( "employeeID" )          ) ;
        
        // put data into TableView
        bookingsTableView.setItems( currentBookings ) ;
        
        // load components with info based on selected TableView row
        bookingsTableView.getSelectionModel().selectedItemProperty().addListener( ( ObservableValue v , Object oldValue , Object newValue ) ->
        {
            
            if ( newValue != null )
            {
            
                try
                {

                    // reference for completing a booking
                    selectedBooking = (Booking) newValue ;
                    
                    // store obj reference to access later is possible
                    CodeBank.storeObject( selectedBooking );
                    
                    txtCustomerName.setText       (  ( (Booking) newValue ).getCustomerName()         ) ;
                    txtVehicleRegistration.setText(  ( (Booking) newValue ).getVehicleRegistration()  ) ;
                    datePicker.setValue           (  ( (Booking) newValue ).getDate()                 ) ;
                    mechanicDropDown.setValue     (  ( (Booking) newValue ).getEmployeeID()           ) ;
                    LocalTime sTime =             (    (Booking) newValue ).getStartTime()              ;
                    LocalTime eTime =             (    (Booking) newValue ).getEndTime()                ;
                    startTimeHour.setValue        (     Booking. getStartHr ( sTime )                 ) ;
                    startTimeMin.setValue         (     Booking. getStartMin( sTime )                 ) ;
                    endTimeHour.setValue          (     Booking. getEndHr   ( eTime )                 ) ;
                    endTimeMin.setValue           (     Booking. getEndMin  ( eTime )                 ) ;

                }

                catch ( Exception e )
                {

                    e.printStackTrace() ;

                }
            
            }
            
            else
            {
                
                //
                
            }
            
        });
        
    }
    
    /* 
        creates a day cell factory to implement restrictions : no sundays + future dates only
    */
    private Callback< DatePicker, DateCell > dayCellFactory = (final DatePicker datePicker1) -> new DateCell()
    {
        
        @Override
        public void updateItem( LocalDate item , boolean empty )
        {
            
            // Must call super
            super.updateItem( item , empty );
            
            // disable all sundays + colours them red
            DayOfWeek day = DayOfWeek.from( item );
            if ( day == DayOfWeek.SUNDAY )
            {
                
                this.setDisable ( true );
                this.setStyle(" -fx-background-color: #ff0000; ") ;
                
            }
            
            // disable all past dates + colours them red
            if (  item.isBefore( LocalDate.now() )  )
            {
                
                this.setDisable ( true ) ;
                this.setStyle(" -fx-background-color: #ff0000; ") ;
                
            }
            
            
            // hunt + disable bank holidays
            if (   this.isBankHoliday(  Booking.fromLocalDate( item )  )   )
            {
            
                this.setDisable ( true ) ;
                this.setStyle(" -fx-background-color: #ff0000; ") ;
            
            }
            
        }

        /* this helper method checks whether a given date is a bank holiday */
        private boolean isBankHoliday( String date ) 
        {
            
            switch ( date )
            {
            
                /* Good Friday */
                case "14/04/2017" : return true ;
                
                /* Easter Monday */
                case "17/04/2017" : return true ;
                
                /* Early May Bank Holiday */
                case "01/05/2017" : return true ;
                
                /* Spring Bank Holiday */
                case "29/05/2017" : return true ;
                
                /* Summer Bank Holiday */
                case "28/048/2017" : return true ;
                
                /* Christmas Day */
                case "25/12/2017" : return true ;
                
                /* Boxing Day */
                case "26/12/2017" : return true ;
                
                /* Not a bank holiday */
                default           : return false ;
            
            }
            
        }
        
    };
    
    // this helper method checks whether saturday has been selected from the datePicker
    @FXML
    private void checkIfSaturdaySelected()
    {
    
        // get the day from datePicker then --> extract day from it to test
        DayOfWeek dayFromDatePicker = datePicker.getValue().getDayOfWeek() ;
        
        // test if dayFromDatePicker == saturday
        if ( dayFromDatePicker == DayOfWeek.SATURDAY )
        {
        
            saturdaySelected = true ;
        
        }
        
        else
        {
        
            saturdaySelected = false ;
        
        }
        
        this.loadTimeOptions() ;
    
    }
    
    // this helper method validates such that u can't make a booking past closing time
    @FXML
    private void endTimeRestrictor()
    {
        try
        {
            // use COMBO BOX
            // clear the ChoiceBox
            endTimeMin.getItems().clear() ;

            // check the end hr
            if ( saturdaySelected && endTimeHour.getValue().equals("12") ) // saturdaySelected + 12PM selected 
            {

                endTimeMin.getItems().addAll ( "00" ) ;

            }

            else if ( endTimeHour.getValue().equals("17") )                // 5PM selected
            {

                endTimeMin.getItems().addAll ( "00" , "15" , "30" ) ;

            }

            else                                                           // 5PM !selected XOR ( saturdaySelected + 12PM !selected )
            {

                endTimeMin.getItems().addAll ( "00" , "15" , "30" , "45" ) ;

            }

            endTimeMin.setValue ( "00" ) ;
        }
        
        catch ( Exception e )
        {
        
            // DO NOTHING
        
        }
        
    }
    
    // this helper method distills the parameter to return the employeeID
    private int extractEmployeeID ( String str )
    {
        
        String[] dividedSTR = str.split( " - " ) ;
        
        return Integer.parseInt( dividedSTR[ 1 ] ) ;
    
    }
    
}