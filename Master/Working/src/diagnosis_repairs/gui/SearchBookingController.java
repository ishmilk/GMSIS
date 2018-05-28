                        
                    /*─────────────────┐
                    │      author : ismail.      │
                    └─────────────────*/

package diagnosis_repairs.gui ;

import java.sql.*                    ; // imports Java SQL package 
import java.io.*                     ; // imports Java IO package
import java.net.*                    ; // imports Java NET package
import java.util.*                   ; // imports Java UTIL package
import java.text.*                   ; // imports Java TEXT package
import java.util.*                   ; // imports Java UTIL package
import java.time.*                   ; // imports Java TIME package needed for time operations
import java.time.format.*            ; // imports Java TIME formatting package
import common.*                      ; // imports the common package
import diagnosis_repairs.logic.*     ; // imports diagnosis + repairs logic package
import vehicles.Logic.*              ; // imports the vehicles package
import parts.logic.*                 ; // imports Java parts package

/******************************************************************************/

import javafx.application.*          ; // imports JavaFX application package
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


public class SearchBookingController  implements Initializable
{
    
    private String searchFor = Context.getInstance().getSearch();
    private String criteria  = Context.getInstance().getCriteria();
    
    // injected variables
    @FXML private TableView   bookingResultsTableView ;
    @FXML private TableColumn bookingResultsDateColumn ;
    @FXML private TableColumn bookingResultsCustomerColumn ;
    @FXML private TableColumn bookingResultsVehicleRegistrationColumn ;
    @FXML private TextField   txtBookingDate ;
    @FXML private TextField   txtCustomerName ;
    @FXML private TextField   txtStartTime ;
    @FXML private TextField   txtEndTime ;
    @FXML private TextField   txtMechanic ;
    @FXML private TextField   txtVehicleClassification ;
    @FXML private TextField   txtVehicleRegistration ;
    @FXML private TextField   txtVehicleMake ;
    @FXML private TextField   txtVehicleModel ;
    @FXML private TextField   txtVehicleEngineSize ;
    @FXML private TextField   txtVehicleFuelType ;
    @FXML private TextField   txtVehicleColour ;
    @FXML private TextField   txtVehicleMOTDate ;
    @FXML private TextField   txtVehicleServiceDate ;
    @FXML private TextField   txtVehicleMileage ;
    @FXML private TableView   partsUsedTableView ;
    @FXML private TableColumn partsUsedNameColumn ;
    @FXML private TableColumn partsUsedDescriptionColumn ;
    @FXML private TableColumn partsUsedPriceColumn ;
    @FXML private TableColumn partsUsedDateInstalledColumn ;
    @FXML private TableColumn partsUsedWarrantyExpiredColumn ;
    
    /*************************************************************************/
    
    @Override
    public void initialize( URL location , ResourceBundle resources )
    {
        
        //("Name", "Registration", "Make", "Template");
        try
        {
            
            Connection c = DatabaseConnector.activateConnection();
            c.setAutoCommit( true ); 
            ResultSet rs ;
            Statement stmt = c.createStatement();    
            
            String sql = "" ;
            
            if( criteria.equals( "Name" ) )
            {
                
                sql = ( "SELECT Customer.name, Vehicle.registration, Booking.date FROM Customer, Vehicle, Booking WHERE Customer.name LIKE '%" + searchFor + "%' AND Customer.customerID = Vehicle.CustomerID AND Booking.vehicleRegistration = Vehicle.registration" ) ;   
               // sql = ("SELECT * FROM Customer WHERE name LIKE '%" + searchFor +"%'");
               
            }
            
            else if( criteria.equals( "Registration" ) )
            {
                
                 sql = ( "SELECT Customer.name, Vehicle.registration, Booking.date FROM Customer, Vehicle, Booking WHERE Vehicle.registration LIKE '%" + searchFor + "%' AND Customer.customerID = Vehicle.CustomerID AND Booking.vehicleRegistration = Vehicle.registration" ) ;   
                //sql = ("SELECT * FROM Vehicle WHERE registration LIKE '%" + searchFor +"%'");
            
            }
            
            else if( criteria.equals( "Make" ) )
            {
                
                 sql = ( "SELECT Customer.name, Vehicle.registration, Booking.date FROM Customer, Vehicle, Booking WHERE Vehicle.make LIKE '%" + searchFor + "%' AND Customer.customerID = Vehicle.CustomerID AND Booking.vehicleRegistration = Vehicle.registration" ) ;   
                //sql = ("SELECT * FROM Vehicle WHERE make LIKE '%" + searchFor +"%'");
            
            }
            
            else
            {
                
                String[] parts = searchFor.split( ", " ) ;
                
                sql = ( "SELECT Customer.name, Vehicle.registration, Booking.date FROM Customer, Vehicle, Booking WHERE Vehicle.make = '" + parts[0] + "' AND Vehicle.model = '" + parts[1] + "' AND Vehicle.engineSize = '" + parts[2] + "' AND Vehicle.fuelType = '" + parts[3] + "' AND Customer.customerID = Vehicle.CustomerID AND Booking.vehicleRegistration = Vehicle.registration" ) ;   
                
               // sql = ("SELECT * FROM Vehicle WHERE Vehicle.make = '" + parts[0] + "' AND Vehilce.model = '" + parts[1] + "' AND Vehilce.engineSize = '" + parts[2] + "' AND Vehilce.fuelType = '" + parts[3] + "'");
           
            }
            
            
            rs = stmt.executeQuery( sql ); 
            
            ObservableList matchingSearch = FXCollections.observableArrayList();
            
            while( rs.next() )
            {
                
                String name = rs.getString( "name" ) ;
                String reg  = rs.getString( "registration" ) ;
                String date = rs.getString( "date" ) ;
                
                LocalDate bookingDate = Booking.toLocalDate( date ) ;
                
                BookingSearch x = new BookingSearch( name , reg , bookingDate ) ;
                
                matchingSearch.add( x ) ;
                
            } 
            
            
            bookingResultsDateColumn.setCellValueFactory(new PropertyValueFactory("date"));
            bookingResultsCustomerColumn.setCellValueFactory(new PropertyValueFactory("name"));
            bookingResultsVehicleRegistrationColumn.setCellValueFactory(new PropertyValueFactory("reg"));
            
            bookingResultsTableView.getItems().addAll( matchingSearch ) ;

            
            bookingResultsTableView.getSelectionModel().selectedItemProperty().addListener( ( obs , oldSelection , newSelection ) -> 
            {
                
                // clears parts used table
                partsUsedTableView.getItems().clear() ;
                
                LocalDate bookingDate       = (  (( BookingSearch )newSelection).getDate()  ) ;
                DateTimeFormatter theFormat = DateTimeFormatter.ofPattern( "dd/MM/yyyy" ) ;
                String strBookingDate       = bookingDate.format( theFormat ) ;
                
                txtBookingDate.setText( strBookingDate ) ;
                txtCustomerName.setText(  (( BookingSearch )newSelection).getName()  ) ;
                txtVehicleRegistration.setText(  (( BookingSearch )newSelection).getReg()  ) ;
                    
           
                try 
                {
                    
                    showBooking( strBookingDate , txtVehicleRegistration.getText() ) ;
                    showVehicle( txtVehicleRegistration.getText() ) ;
                    showParts( txtVehicleRegistration.getText() ) ;
                    
                } 
                
                catch ( Exception e ) 
                {
                    
                    e.printStackTrace() ;
                    
                }
                    
            });
            
            
            
            c.close();   
        }
        catch (SQLException e)
        {
            
        }
           
        
    }
    
    public void showBooking( String bookingDate , String reg )
    {
        
        try
        {
            
            Connection c = DatabaseConnector.activateConnection();
            c.setAutoCommit( true ); 
            Statement stmt = c.createStatement();  
            ResultSet rs ;
                
            rs = stmt.executeQuery( "SELECT * FROM Booking WHERE vehicleRegistration = '" + reg + "' AND date ='" + bookingDate + "'" ) ; 
                
            Booking x = null ;
            
            while( rs.next() )
            {
                
                Integer bookingID = rs.getInt("bookingID");
                Integer billID = rs.getInt("billID");
                String date = rs.getString("date");
                String startTime = rs.getString("startTime");
                String endTime = rs.getString("endTime"); 
                Double cost = rs.getDouble("cost");
                Integer employeeID = rs.getInt("employeeID");
                
                LocalDate bookingDateDate = Booking.toLocalDate(bookingDate);
                LocalTime bookingStartTime = Booking.toLocalTime(startTime);
                LocalTime bookingEndTime = Booking.toLocalTime(endTime);
                  
                x = new Booking( bookingID , 0 , bookingDateDate , bookingStartTime , bookingEndTime , 0.0 , employeeID , reg ) ;
                
            }
            
            txtStartTime.setText( x.getStartTime().toString() ) ;
            txtEndTime.setText  ( x.getEndTime().toString() ) ;
            txtMechanic.setText ( x.getEmployeeID() ) ;
            
          
        }
        
        catch ( Exception e )
        {
        
            e.printStackTrace() ;
        
        }
        
    } 
    
    public void showVehicle( String reg )
    {
        
        try
        {
            
            Connection c = DatabaseConnector.activateConnection();
            c.setAutoCommit( true ); 
            Statement stmt = c.createStatement();  
            ResultSet rs ;
                
            rs = stmt.executeQuery( "SELECT * FROM Vehicle WHERE registration = '" + reg + "'" ) ; 
                
            vehicle x = null ;
            
            while( rs.next() )
            {
                
                Integer warrantyInt = rs.getInt("hasWarranty");
                String classification = rs.getString("classification");
                String make = rs.getString("make");
                String model = rs.getString("model");                
                Double engine = rs.getDouble("engineSize");
                String fuel = rs.getString("fuelType");
                String colour = rs.getString("colour");
                Double mileage = rs.getDouble("mileage");
                Integer customerID = rs.getInt("customerID");
                String MOT = rs.getString("motDate");
                String service = rs.getString("serviceDate");
                
                LocalDate MOTDate     = Booking.toLocalDate(MOT);
                LocalDate serviceDate = Booking.toLocalDate(service);
                Boolean warranty = false;
                
                if( warrantyInt == 1 )
                {
                    
                    warranty = true ;
                
                }
                
                try
                {

                    x = new vehicle( warranty , classification , reg , make , model , engine , fuel , colour , MOTDate , serviceDate , mileage ) ;
                
                }
                
                catch ( Exception e )
                {

                    e.printStackTrace() ;
                
                }
                
            }
            
            
            txtVehicleClassification.setText( x.getClassification() ) ;
            txtVehicleMake.setText          ( x.getMake() ) ;
            txtVehicleModel.setText         ( x.getModel() ) ;
            txtVehicleEngineSize.setText    ( "" + x.getEngineSize() ) ;
            txtVehicleFuelType.setText      ( x.getFuel() ) ;
            txtVehicleColour.setText        ( x.getColour() ) ;
            
            // string format --> dd/MM/YYYY
            DateTimeFormatter theFormat = DateTimeFormatter.ofPattern( "dd/MM/yyyy" );
            LocalDate MOTDate     = x.getMOT() ;
            LocalDate ServiceDate = x.getService() ;
            
            txtVehicleMOTDate.setText     ( MOTDate.format( theFormat ) ) ;
            txtVehicleServiceDate.setText ( ServiceDate.format( theFormat ) ) ;
            txtVehicleMileage.setText     ( "" + x.getMileage() ) ;
          
        }
        
        catch ( Exception e )
        {
        
            e.printStackTrace() ;
        
        }
        
    }  
    
    public void showParts( String reg )
    {
        
        try
        {
            
            Connection c = DatabaseConnector.activateConnection();
            c.setAutoCommit( true ); 
            Statement stmt = c.createStatement();  
            ResultSet rs ;

            rs = stmt.executeQuery( "SELECT * FROM Booking, Parts WHERE Booking.vehicleRegistration = '" + reg + "' AND Booking.bookingID = Parts.bookingID" ) ; 

            ObservableList matchingParts = FXCollections.observableArrayList();

            while( rs.next() )
            {
                
                Integer partID = rs.getInt("partID");
                String name = rs.getString("name");
                String description = rs.getString("description");
                Double cost = rs.getDouble("cost");
                String dateInStock = rs.getString("dateInStock"); 
                String dateInstalled = rs.getString("dateInstalled"); 
                Integer bookingID = rs.getInt("bookingID");

                LocalDate partInStock = Booking.toLocalDate(dateInStock);                     

                Part x = new Part(partID, name, description, cost, partInStock);

                if(dateInstalled != null)
                {
                    LocalDate partInstalled = Booking.toLocalDate(dateInstalled);
                    x.setDateInstalled(partInstalled);
                }

                matchingParts.add(x);

            }

            partsUsedNameColumn.setCellValueFactory(new PropertyValueFactory("PartName"));
            partsUsedDescriptionColumn.setCellValueFactory(new PropertyValueFactory("PartDescr"));
            partsUsedPriceColumn.setCellValueFactory(new PropertyValueFactory("PartCost"));
            partsUsedDateInstalledColumn.setCellValueFactory(new PropertyValueFactory("dateInstalled"));
            partsUsedWarrantyExpiredColumn.setCellValueFactory(new PropertyValueFactory("warrantyExpired"));

            partsUsedTableView.getItems().addAll(matchingParts);

        }

        catch ( Exception e )
        {

            e.printStackTrace() ;

        }
        
    } 

}