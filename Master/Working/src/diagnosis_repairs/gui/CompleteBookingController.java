                        
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
import parts.logic.*                 ; // imports Java parts package
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


public class CompleteBookingController implements Initializable
{

    // injected variables
    @FXML private TextField          txtCustomerName ;
    @FXML private TextField          txtVehicleRegistration ;
    @FXML private TextField          txtBookingDate ;
    @FXML private TextField          txtStartTime ;
    @FXML private TextField          txtEndTime ;
    @FXML private TextField          txtMechanic ;
    @FXML private TableView          partsTableView ;
    @FXML private TableColumn        partNameColumn ;
    @FXML private TableColumn        partPriceColumn ;
    @FXML private Button             btnEditPartsUsed ;
    @FXML private TextField          txtMileage ;
    @FXML private Label              lblTotalCost ;
    @FXML private CheckBox           settledCheckBox ;
    @FXML private Button             btnComplete ;
    @FXML private Label              statusAfterButtonPress ;
    
    /*************************************************************************/
    
    // regular variables
    private Booking  bookingToComplete = null ;
    private boolean  hasWarranty       = false ;
    private boolean  validWarranty     = false ;
    private String   status            = "" ;
    private double   duration          = 0.0 ;
    private double   totalCost         = 0.0 ;
    private double   manpowerCost      = 0.0 ; 
    private double   partsCost         = 0.0 ;
    private double   vehicleMileage    = 0.0 ;
    
    @Override
    public void initialize( URL location , ResourceBundle resources ) 
    {
        
        try 
        {
                                                            /* LOOK HERE ------------------> TIME ERROR CHECKS FOR UPDATING A BOOKING FROM MAIN SCREEN */
            // get stored object reference
            bookingToComplete = (Booking) CodeBank.getStoredObject() ;
            
            txtCustomerName.setText         ( bookingToComplete.getCustomerName()         ) ;
            txtVehicleRegistration.setText  ( bookingToComplete.getVehicleRegistration()  ) ;
            LocalDate bookingDate          =  bookingToComplete.getDate()                   ;
            String strBookingDate          =  Booking.fromLocalDate( bookingDate )          ;
            txtBookingDate.setText          ( strBookingDate )                              ;
            txtStartTime.setText            ( bookingToComplete.getStartTime().toString() ) ;
            txtEndTime.setText              ( bookingToComplete.getEndTime().toString()   ) ;
            txtMechanic.setText             ( bookingToComplete.getEmployeeID()           ) ;
            
            // apply formatting restrictions for --> txtMilage
            CodeBank.applyDecimalRestriction( txtMileage ) ;
            
            // get the parts associated with a booking
            this.getCurrentPartsList() ;
            
            // check if a vehicle has a warranty
            hasWarranty = this.checkVehicleWarranty( bookingToComplete.getVehicleRegistration() ) ;
            
            if ( hasWarranty ) // vehicle has a warranty --> now check if warranty == valid ( NOT EXPIRED )
            {
                
                validWarranty = this.checkWarrantyValidity( bookingToComplete.getVehicleRegistration() ) ;
                
                if( validWarranty )     // warranty valid ( !expired )
                {
                
                    settledCheckBox.setSelected( true );
                    settledCheckBox.setDisable( true );
                    lblTotalCost.setText( "VEHICLE HAS VALID WARRANTY" );
                    /* ALTERNATIVE GREEN COLOUR --> ( 12 , 181 , 3 ) */
                    lblTotalCost.setTextFill( Color.rgb( 0 , 255 , 0 ) );
                    
                }
            
                else                    // waranty !valid ( expired )
                {
                
                    /* execution done in --> .completeBooking() */
                
                }
            
            }
            
            else               // vehicle !has a warranty --> PAY UP
            {
            
                /* execution done in --> .completeBooking() */
            
            }
            
            // calculate how long the mechanic has worked for on this booking
            duration = this.calculateHoursWorked( txtStartTime.getText() , txtEndTime.getText() ) ;

            // calculate the cost of manpower on a booking
            manpowerCost = this.calculateManpowerCost() ;

            // calculate the cost of parts used on a booking
            partsCost = this.calculatePartsUsedCost() ;
            
            /* CALCULATE THE TOTAL COST OF THE BOOKING */
            totalCost = manpowerCost + partsCost ;
            System.out.println( "** total cost for this booking ( for a person with no / invalid warranty ) --> " + totalCost + " **" ) ;
            
            if ( !validWarranty )
            {
                
                // set the total cost label to show user
                lblTotalCost.setText( "£ " + Math.round( totalCost * 100.0 ) / 100.0 ) ;
                lblTotalCost.setTextFill( Color.rgb( 0 , 0 , 0 ) );
                
            }
            
        } 
        
        catch ( Exception e ) 
        {
            
            e.printStackTrace();
            
        }
        
    }
    
    /* this method allows the user to edit the parts used on a booking */
    @FXML
    private void editPartsUsedTable() throws Exception
    {
    
        // get the new pane to be loaded
        Pane spawnBookingPane = FXMLLoader.load( getClass().getResource( "/parts/GUI/editBookingParts.fxml" ) );

        // load the new pane into the base pane
        common.FXMLDocumentController.changeContentPane( spawnBookingPane );
        /*
        // update the parts used table
        this.getCurrentPartsList() ;
        
        // recalculate the cost of parts used on a booking
        partsCost = this.calculatePartsUsedCost() ;
        */
    }
    
    /* this method creates + adds a bill into the database */
    @FXML
    private void completeBooking() throws Exception
    {
        
        // error check the vehicleMileage textfield
        if ( txtMileage.getText().equals( "" ) )
        {
        
            // red hex code = #ff0000
            txtMileage.setStyle( " -fx-border-color : #ff0000 ; " ) ;
            
            /* .showErrorAlert( String T , String C ) has 2 parameters --> the title T and the content C */
            CodeBank.showErrorAlert( "MISSING INFORMATION" , "You MUST enter the vehicle's mileage" ) ;
        
        }
        
        else
        {
        
            // get the updated vehicle mileage from --> txtMileage
            vehicleMileage             = Double.parseDouble( txtMileage.getText() ) ;
            int bookingID              = bookingToComplete.getID() ;
            String vehicleRegistration = txtVehicleRegistration.getText() ;

            // check to see if we are updating / generating a bill
            /* 1 BILL : 1 BOOKING */
            //boolean billAlreadyExists = this.billExists( bookingID ) ;
            
            // spawn a connection
            Connection c = common.DatabaseConnector.activateConnection();
            c.setAutoCommit( true ); 

            if ( validWarranty )               // warranty !expired + bill record exists --> execute a SQL UPDATE command
            {

                status    = "Warranty" ;
                totalCost = 0.0 ;

                // update vehicleMilege in the Vehicle Table
                PreparedStatement pstmtVM = c.prepareStatement( "UPDATE Vehicle SET mileage = ? WHERE registration = ? " ) ;
                pstmtVM.setDouble( 1 , vehicleMileage ) ;
                pstmtVM.setString( 2 , vehicleRegistration ) ;
                pstmtVM.execute() ;
                pstmtVM.close() ;

                // then update the record in the Bill Table
                PreparedStatement pstmt   = c.prepareStatement( "UPDATE Bill SET total = ? , status = ? WHERE bookingID = ? " ) ;
                pstmt.setDouble( 1 , totalCost ) ;
                pstmt.setString( 2 , status    ) ;
                pstmt.setInt   ( 3 , bookingID ) ;
                pstmt.execute() ;
                pstmt.close() ;

                /* indication to the user to inform them that there was a bill in the database that was updated */
                statusAfterButtonPress.setText( "BILL UPDATED" ) ;
                statusAfterButtonPress.setTextFill( Color.rgb( 0 , 255 , 0 ) );

                System.out.println( "** BILL HAS BEEN UPDATED **" ) ;

            }

            else if ( !validWarranty )         // warranty expired + bill record exists --> execute a SQL UPDATE command
            {

                /* check to see if settledCheckBox is ticked */
                if ( settledCheckBox.isSelected() )     // customer has paid for booking
                {

                    status    = "Paid" ;

                }

                else                                    // customer !has paid for booking
                {

                    status    = "Unpaid" ;

                }

                // update vehicleMilege in the Vehicle Table
                PreparedStatement pstmtVM = c.prepareStatement( "UPDATE Vehicle SET mileage = ? WHERE registration = ? " ) ;
                pstmtVM.setDouble( 1 , vehicleMileage ) ;
                pstmtVM.setString( 2 , vehicleRegistration ) ;
                pstmtVM.execute() ;
                pstmtVM.close() ;

                // then update the record in the Bill Table
                PreparedStatement pstmt = c.prepareStatement( "UPDATE Bill SET total = ? , status = ? WHERE bookingID = ? " ) ;
                pstmt.setDouble( 1 , totalCost ) ;
                pstmt.setString( 2 , status    ) ;
                pstmt.setInt   ( 3 , bookingID ) ;
                pstmt.execute() ;
                pstmt.close() ;

                /* indication to the user to inform them that there was a bill in the database that was updated */
                statusAfterButtonPress.setText( "BILL UPDATED" ) ;
                statusAfterButtonPress.setTextFill( Color.rgb( 0 , 255 , 0 ) );

                System.out.println( "** BILL HAS BEEN UPDATED **" ) ;

            }

            txtMileage.setStyle( " -fx-border-color : none ; " ) ;
            // close the spawned connection
            c.close() ;
        
        }
    
    }
    
    /* this helper method calculates how long a mechanic has worked on in hrs */
    private double calculateHoursWorked( String startTime , String endTime )
    {
        
        double durationInMins = 0.0 ;
        double startHr        = 0.0 ;
        double startMin       = 0.0 ;
        double endHr          = 0.0 ;
        double endMin         = 0.0 ;
        
        String[] strStartTime = startTime.split(":") ; 
        String[] strEndTime   = endTime.split  (":") ;
        
        startHr  = Integer.parseInt( strStartTime[ 0 ] ) ;
        startMin = Integer.parseInt( strStartTime[ 1 ] ) ;
        endHr    = Integer.parseInt(   strEndTime[ 0 ] ) ;
        endMin   = Integer.parseInt(   strEndTime[ 1 ] ) ;

        if( startMin == endMin )
        {
            
            durationInMins = durationInMins + ( endHr - startHr ) * 60 ;
            
        }
        
        else
        {   
            
            durationInMins = durationInMins + (  ( endHr - startHr ) - 1  ) * 60 ;
            
            durationInMins = durationInMins + ( 60 - startMin ) ;       
            
            durationInMins = durationInMins + endMin ; 
            
        }
        
        System.out.println( "** duration worked in hrs --> " + durationInMins / 60 + " **" ) ;
        
        // hrs = mins ÷ 60
        return durationInMins / 60 ;
        
    }
    
    /* this helper method calculates the manpower cost of a booking  */
    private double calculateManpowerCost() throws Exception
    {
    
        double hrlyRate = 0.0 ;
        int bookingID   = bookingToComplete.getID() ;
        
        Connection c = common.DatabaseConnector.activateConnection() ;
        c.setAutoCommit( false ) ; 
        ResultSet rs ;
        String SQL = "SELECT hourlyRate FROM Bill , Booking , Employee WHERE Bill.bookingID = ? AND Bill.bookingID = Booking.bookingID AND Booking.employeeID = Employee.employeeID " ;
        PreparedStatement pstmt = c.prepareStatement( SQL ) ;
        pstmt.setInt( 1 , bookingID ) ;
        rs = pstmt.executeQuery() ;
        
        if ( rs.next() )
        {
        
            hrlyRate = rs.getDouble( "hourlyRate" ) ;
        
        }
        
        c.close() ;
        
        System.out.println( "** manpower cost --> £" + hrlyRate * duration + " **" ) ;
        
        return hrlyRate * duration ;
    
    }
    
    /* this helper method the cost of parts used in a booking */
    private double calculatePartsUsedCost() throws Exception
    {
        
        //ObservableList partsList = partsTableView.getItems() ;
        // get all the parts obj's in the list
        ObservableList partsList = getCurrentPartsList() ;
        
        final int listSize = partsList.size() ;
        
        double total       = 0.0 ;
        
        for ( int p = 0 ; p < listSize ; p = p + 1 )
        {
        
            // initialise temp variable
            Part temp = (Part) partsList.get( p ) ;
            
            // add to total
            total = total + temp.getPartCost() ;
        
        }
        
        System.out.println( "** parts used cost --> £" + total + " **" ) ;
        
        return total ;
    
    }
    
    // this helper method reloads the TableView to show parts associated with a booking
    private ObservableList getCurrentPartsList() throws Exception
    {
        
        partsTableView.getItems().clear() ;
        
        // get bookingID
        int bookingID = bookingToComplete.getID() ;
        
        Connection c = common.DatabaseConnector.activateConnection();
        c.setAutoCommit( true ); 
        Statement stmt = c.createStatement();
        ResultSet rs ;
        rs = stmt.executeQuery( "SELECT name , cost FROM Parts WHERE bookingID = " + bookingID ) ;
        
        ObservableList partsList = FXCollections.observableArrayList() ; 
        
        while ( rs.next() )
        {
            
            String name = rs.getString( "name" ) ;
            double cost = rs.getDouble( "cost" ) ;
            
            Part p = new Part( 0 , name , "" , cost , LocalDate.now() ) ;
            
            partsList.add( p ) ;
            
        }
        
        c.close() ;
        
        partNameColumn.setCellValueFactory  ( new PropertyValueFactory( "partName" ) ) ;
        partPriceColumn.setCellValueFactory ( new PropertyValueFactory( "partCost" ) ) ;
        
        partsTableView.setItems( partsList ) ;
        
        //System.out.println( "** PARTS LIST UPDATED **" ) ;
        
        return partsList ;
    
    }
    
    // this helper method queries the database to see if a vehicle has a valid warranty
    private boolean checkVehicleWarranty( String vehicleRegistration ) throws Exception
    {
        
        Connection c = common.DatabaseConnector.activateConnection() ;
        c.setAutoCommit( false ) ; 
        ResultSet rs ;
        PreparedStatement pstmt = c.prepareStatement( "SELECT hasWarranty FROM Vehicle WHERE registration = ? " ) ;
        pstmt.setString( 1 , vehicleRegistration ) ;
        rs = pstmt.executeQuery() ;
        
        // lets me interpret integers as boolean
        boolean validWarranty = ( rs.getInt( "hasWarranty" ) != 0 ) ;
        
        c.close() ;
    
        return validWarranty ;
        
    }
    
    /* this helper method checks whether a vehicle's warranty is valid ( EXPIRED OR NAH ) */
    private boolean checkWarrantyValidity( String vehicleRegistration ) throws Exception
    {
    
        LocalDate expiryDate = null;
        Connection c = common.DatabaseConnector.activateConnection() ;
        c.setAutoCommit( false ) ; 
        ResultSet rs ;
        PreparedStatement pstmt = c.prepareStatement( "SELECT date FROM Warranty WHERE vehicleRegistration = ? " ) ;
        pstmt.setString( 1 , vehicleRegistration ) ;
        rs = pstmt.executeQuery() ;
        
        // might NOT have a warranty on the vehicle
        if ( rs.next() )                              // there is a warranty record
        {
            
            expiryDate   = Booking.toLocalDate( rs.getString( "date" ) );
            
            c.close() ;
            
        }
        
        else                                          // there !is a warranty record
        {
        
            c.close() ;
            
            return false ;
        
        }
        
        // check if expired
        /* if date expired --> returns FALSE , else returns TRUE */
        return expiryDate.isAfter( LocalDate.now() ) ;
        
    }
    
    /* this method queries the database to see if there is a bill associated with a booking */
    /* if there is a record of bill associated with a booking --> return TRUE , else return FALSE */
    /* 1 BILL : 1 BOOKING */
    private boolean billExists( int ID ) throws Exception
    {
    
        Connection c = common.DatabaseConnector.activateConnection();
        c.setAutoCommit( true ); 
        ResultSet rs ;
        PreparedStatement pstmt = c.prepareStatement( "SELECT * FROM Bill WHERE bookingID = ? " ) ;
        pstmt.setInt( 1 , ID );
        rs = pstmt.executeQuery();
    
        // check if there is a record
        if ( rs.next() )        // there IS a recorded BILL linked to a booking
        {
            
            c.close() ;
            System.out.println( "** BILL FOUND FOR A BOOKING ID **" ) ;
            
            return true ;
        
        }
        
        else                    // there IS NOT a recorded BILL linked to a booking
        {
        
            c.close() ;
            System.out.println( "** BILL !FOUND FOR A BOOKING ID **" ) ;
            
            return false ;
        
        }
        
    }

}