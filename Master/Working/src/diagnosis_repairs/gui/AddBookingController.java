                        
                    /*─────────────────┐
                    │      author : ismail.      │
                    └─────────────────*/

package diagnosis_repairs.gui;

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


public class AddBookingController implements Initializable
{

    // injected variables
    @FXML private TextField           txtVehicleRegistration ;
    @FXML private Label               vehicleSearchStatus ;
    @FXML private ChoiceBox< String > mechanicDropDown ;
    @FXML private DatePicker          datePicker ;
    @FXML private ChoiceBox< String > startTimeHour ;
    @FXML private ChoiceBox< String > startTimeMin ;
    @FXML private ComboBox < String > endTimeHour ;
    @FXML private ChoiceBox< String > endTimeMin ;
    @FXML private TableView           currentBookingsTableView ;
    @FXML private TableColumn         dateColumn ; 
    @FXML private TableColumn         mechanicColumn ; 
    @FXML private TableColumn         startTimeColumn ; 
    @FXML private TableColumn         endTimeColumn ; 
    
    /*************************************************************************/
    
    // regular variables
    private boolean foundVehicle            = false ;
    private boolean anyDateSelected         = false ;
    private boolean saturdaySelected        = false ;
    private boolean pastBookingSelected     = false ;
    private int     bookingTimesCheckPassed = 0 ;

    // run as soon as relevant scene is loaded into the stage ( the window )
    @Override
    public void initialize( URL location, ResourceBundle resources ) 
    {
        
        // load the mechanics from the database --> ChoiceBox
        try
        {
            
            // implement restrictions on the datePicker
            datePicker.setDayCellFactory( dayCellFactory );
            
            // get the mechanics from the database
            this.getMechanics() ;
            
            // gets the current bookings from the database
            this.getCurrentBookings() ;
            
        }
        
        catch ( Exception e )
        {
        
           e.printStackTrace() ;
        
        }
        
    }
    
    // this procedure is used to add a booking to the system
    @FXML
    public void addBooking() throws Exception
    {
    
        if ( !foundVehicle ) // invalid OR non-existant vehicle registration
        {
            
            // red hex code = #ff0000
            txtVehicleRegistration.setStyle( " -fx-border-color : #ff0000 ; " ) ;
            
            /* .showErrorAlert( String T , String C ) has 2 parameters --> the title T and the content C */
            CodeBank.showErrorAlert( "MISSING INFORMATION" , "You MUST enter a valid vehicle registration to create a booking" ) ;
            
        }
        
        else                 // valid vehicle registration
        {
            
            if( !anyDateSelected ) // no date chosen
            {
            
                // red hex code = #ff0000
                datePicker.setStyle( " -fx-border-color : #ff0000 ; " ) ;

                /* .showErrorAlert( String T , String C ) has 2 parameters --> the title T and the content C */
                CodeBank.showErrorAlert( "MISSING INFORMATION" , "You MUST choose a date to create a booking" ) ;
            
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
                    
                    pastBookingSelected = Booking.checkBookingDate( Booking.fromLocalDate( datePicker.getValue() ) , startHr + ":" + startMin ) ;
                    
                    if( pastBookingSelected )
                    {
                    
                        // red hex code = #ff0000
                        startTimeHour.setStyle( " -fx-border-color : #ff0000 ; " ) ;
                        startTimeMin.setStyle( " -fx-border-color : #ff0000 ; " ) ;
                        endTimeHour.setStyle( " -fx-border-color : #ff0000 ; " ) ;
                        endTimeMin.setStyle( " -fx-border-color : #ff0000 ; " ) ;

                        /* .showErrorAlert( String T , String C ) has 2 parameters --> the title T and the content C */
                        CodeBank.showErrorAlert( "YOU CAN'T CHANGE THE PAST" , "You CANNOT add a booking in the past" ) ;
                    
                    }
                    
                    else
                    {
                    
                        // get the date for the booking + format
                        String strBookingDate = Booking.fromLocalDate( datePicker.getValue() ) ;

                        // get the start + end times for the booking
                        LocalTime bookingStartTime = LocalTime.of( startHr , startMin  ) ;
                        LocalTime bookingEndTime   = LocalTime.of( endHr   , endMin  ) ;
                        startTimeHour.setStyle( " -fx-border-color : none ; " ) ;
                        startTimeMin.setStyle( " -fx-border-color : none ; " ) ;
                        endTimeHour.setStyle( " -fx-border-color : none ; " ) ;
                        endTimeMin.setStyle( " -fx-border-color : none ; " ) ;
                        String vehicleRegistration = txtVehicleRegistration.getText() ;

                        // get the mechanic for this job
                        String mechanic = mechanicDropDown.getValue() ;
                        int employeeID  = this.extractEmployeeID( mechanic ) ;

                        /* TODO : check for overlapping times */

                        // make + execute SQL query
                        Connection c = common.DatabaseConnector.activateConnection();
                        c.setAutoCommit( true ); 

                        // use of a PreparedStatement -> replace ? later
                        PreparedStatement pstmt = c.prepareStatement( "INSERT INTO Booking ( date , startTime , endTime , employeeID , vehicleRegistration )"
                                                                    + " VALUES             ( ? , ? , ? , ? , ? )                                            " ) ;

                        // in an instance of PreparedStatement --> .setXXX( ? no. , DATA ) allows to swap the ?'s from before for data
                        pstmt.setString( 1 ,        strBookingDate         );
                        pstmt.setString( 2 ,  bookingStartTime.toString()  );
                        pstmt.setString( 3 ,    bookingEndTime.toString()  );
                        pstmt.setInt   ( 4 ,         employeeID            );
                        pstmt.setString( 5 ,      vehicleRegistration      );

                        // executes the SQL statement
                        pstmt.execute() ;

                        // refresh the table to show new bookings
                        this.getCurrentBookings() ;

                        // reset check to see if a saturday has been selected from the datePicker
                        saturdaySelected = false ;

                        // reset screen
                        this.resetPane() ;

                        System.out.println( "SUCCESS :) - ADDED A NEW BOOKING INTO DATABASE " );
                        
                        Connection c2 = common.DatabaseConnector.activateConnection();
                        c2.setAutoCommit( true ); 
                        
                        // insert a record into the Bill table
                        PreparedStatement pstmt2 = c2.prepareStatement( "INSERT INTO Bill ( total , status , bookingID ) VALUES ( ? , ? , ? ) " ) ;
                        pstmt2.setDouble( 1 , 0 ) ;
                        pstmt2.setString( 2 , "N/A" ) ;
                        pstmt2.setInt   ( 3 , Booking.getLatestBookingID() ) ;
                        pstmt2.execute() ;
                        pstmt2.close() ;
                        
                        c2.close() ;
                        
                        System.out.println( "SUCCESS :) - ADDED A NEW BILL INTO DATABASE " );
                    
                    }
                
                }
            
            }
           
        }
    
    }
    
    // this helper method returns the mechanics stored in the database
    private void getMechanics() throws Exception
    {
    
        Connection c = common.DatabaseConnector.activateConnection();
        c.setAutoCommit( true ); 
        Statement stmt = c.createStatement();
        ResultSet rs = stmt.executeQuery( "SELECT employeeID , name , isMechanic FROM Employee" );
        
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
    
    // this helper method searches database for a vehicle match
    @FXML
    private void searchForVehicle() throws Exception
    {
    
        Connection c = common.DatabaseConnector.activateConnection();
        c.setAutoCommit( true ); 
        Statement stmt = c.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT registration FROM Vehicle WHERE registration = '" + txtVehicleRegistration.getText() + "'" );
        System.out.println( txtVehicleRegistration.getText() ) ;
        
        if ( rs.next() == false )   // no vehicle found
        {
        
            foundVehicle = false ;
            vehicleSearchStatus.setText( "A vehicle has NOT been found for vehicle registration --> " + txtVehicleRegistration.getText() ) ;
            vehicleSearchStatus.setTextFill( Color.rgb( 255 , 0 , 0 ) );
            System.out.println( "** VEHICLE NOT FOUND **" ) ;
        
        }
        
        else                        // vehicle found
        {
        
            foundVehicle = true ;
            vehicleSearchStatus.setText( "A vehicle has been found for vehicle registration --> " + txtVehicleRegistration.getText() ) ;
            vehicleSearchStatus.setTextFill( Color.rgb( 0 , 255 , 0 ) );
            txtVehicleRegistration.setStyle( " -fx-border-color : none ; " ) ;
            System.out.println( "** VEHICLE FOUND **" ) ;
        
        }
        
        c.close() ;
        
    }
    
    // this helper method loads ChoiceBox ( + 1 ComboBox ) options into ChoiceBox's ( + 1 ComboBox ) considering date restraints
    /* 
        - loads time options into start + end time DropDown's
        - this takes into consideration the date chosen to enact time restrictions
        - VIA INSTANCE VARIABLE --> saturdaySelected
    */
    /* TODO : check for overlapping */
    private void loadTimeOptions()
    {
        
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
    
    // this helper method distills the parameter to return the employeeID
    private int extractEmployeeID ( String str )
    {
        
        String[] dividedSTR = str.split( " - " ) ;
        
        return Integer.parseInt( dividedSTR[ 1 ] ) ;
        
    
    }
    
    // this helper method displays the current bookings
    private void getCurrentBookings() throws Exception
    {
    
        Connection c = common.DatabaseConnector.activateConnection();
        c.setAutoCommit( true ); 
        Statement stmt = c.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM Booking" );
        
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
        
        /* 
            the PropertyValueFactory --> MUST MUST MUST --> 
            take EXACTLY the same argument NAME from the class the TableView is associated to 
            the SETTERS + GETTERS MUST MUST MUST be in the format --> .getXXX()  +  .setXXX()
            where the 1st 'X' is a capital letter / sign ---> YOU MUST FOLLOW JAVA NAMING CONVENTIONS
        */
        dateColumn.setCellValueFactory       ( new PropertyValueFactory( "date" )        ) ;
        mechanicColumn.setCellValueFactory   ( new PropertyValueFactory( "employeeID" )  ) ;
        startTimeColumn.setCellValueFactory  ( new PropertyValueFactory( "startTime" )   ) ;
        endTimeColumn.setCellValueFactory    ( new PropertyValueFactory( "endTime" )     ) ;
        
        // put data into TableView
        currentBookingsTableView.setItems( currentBookings ) ;
    
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
        
        try
        {
        
            // set FLAG = true for error checking
            anyDateSelected  = true ;
            datePicker.setStyle( " -fx-border-color : none ; " ) ;

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
        
        catch ( Exception e )
        {
        
            // DO NOTHING
        
        }
    
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
    
    /* this helper method does what it says on the tin --> resets the pane */
    private boolean resetPane()
    {
    
        try
        {
            
            txtVehicleRegistration.clear() ;
            datePicker.setValue( null );
            foundVehicle    = !foundVehicle ;
            anyDateSelected = !anyDateSelected ;
            vehicleSearchStatus.setText( null );
            
            return true ;
        
        }
        
        catch ( Exception e )
        {
        
            e.printStackTrace() ;
            
            return false ;
        
        }
    
    }
    
}