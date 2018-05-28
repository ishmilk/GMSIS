/*
 * Author:  Amy Dowse 
 * The controller for the AddVehicle fxml screen - adding a vehicle, warranty If there), booking to an existing customer  
 */
package vehicles.Logic; 

import common.DatabaseConnector;
import diagnosis_repairs.logic.Booking;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection; 
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.util.Callback;


public class AddVehicleController implements Initializable
{
    //customer
    @FXML private TextField txtSearch = new TextField();
    @FXML private Label lblName = new Label();
    @FXML private Label lblAddress = new Label();
    @FXML private Label lblNumber = new Label();
    
    //Table
    @FXML TableColumn nameCol;
    @FXML TableColumn addressCol;
    @FXML TableView multipleCust;
    
    //vehicle 
    @FXML private ComboBox cbClassification = new ComboBox();
    @FXML private TextField txtReg = new TextField();
    @FXML private ComboBox cbGiven = new ComboBox(); 
    @FXML private TextField txtMake = new TextField();
    @FXML private TextField txtModel = new TextField();
    @FXML private TextField txtEngine = new TextField();
    @FXML private TextField txtFuel = new TextField();
    @FXML private TextField txtColour = new TextField();
    @FXML private TextField txtMOT = new TextField();
    @FXML private TextField txtService = new TextField();
    @FXML private TextField txtMileage = new TextField();
    @FXML private CheckBox chWarranty = new CheckBox();
    
    //booking
    @FXML private TextField txtDate = new TextField();
    @FXML private TextField txtstartTime = new TextField();
    @FXML private TextField txtendTime = new TextField();
    @FXML private ComboBox cbMechanic = new ComboBox(); 
    
    //current bookings
    @FXML private TableView bookingTable = new TableView();
    @FXML private TableColumn startCol = new TableColumn();
    @FXML private TableColumn endCol = new TableColumn();
    @FXML private TableColumn empCol = new TableColumn();
    
    
    //warranty
    @FXML private Label lblWarranty = new Label();
    @FXML private TextField txtCompany = new TextField();
    @FXML private TextField txtAddress1 = new TextField();
    @FXML private TextField txtAddress2 = new TextField();
    @FXML private TextField txtPostcode= new TextField();
    @FXML private TextField txtExpiration = new TextField();
    
    
    //trying
     @FXML private DatePicker          datePicker ;
    @FXML private ChoiceBox< String > startTimeHour ;
    @FXML private ChoiceBox< String > startTimeMin ;
    @FXML private ComboBox< String >  endTimeHour ;
    @FXML private ChoiceBox< String > endTimeMin ;
    
    private boolean saturdaySelected = false ;
    
    private customers.logic.Customer chosenCustomer = null;
    
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) 
    {
        //get the values of the Combo box for classification 
        cbClassification.getItems().addAll("Car", "Van", "Truck");
        diagnosis_repairs.logic.CodeBank.applyDecimalRestriction(txtEngine);
        diagnosis_repairs.logic.CodeBank.applyDecimalRestriction(txtMileage);
        showMechanics();
               
        datePicker.setDayCellFactory( dayCellFactory );
    }
    
    
    
    //getting all of the mechanics  to show in the drop down menu 
    public void showMechanics()
    {
        try
        {
            Connection c = common.DatabaseConnector.activateConnection();
            c.setAutoCommit( true ); 
            Statement stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT employeeID , name , isMechanic FROM Employee WHERE isMechanic == 1" );

            // load mechanics into drop down
            while ( rs.next() )
            {
                int employeeID      = rs.getInt( "employeeID" ) ;
                String mechanicName = rs.getString( "name" )    ;
                int isAMechanic     = rs.getInt( "isMechanic" ) ;

               cbMechanic.getItems().add( mechanicName + " - " + employeeID );
            }
            c.close();
        }
        catch (SQLException e)
        {
            
        } 
    }
    
    
    //getting all of the templates to show in the drop down menu 
    @FXML
    public void showTemplates()
    {
        cbGiven.getItems().clear();
        txtMake.setText("");
        txtModel.setText("");
        txtEngine.setText("");
        txtFuel.setText("");
        try
        {
            Connection c = common.DatabaseConnector.activateConnection();
            c.setAutoCommit( true ); 
            Statement stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM VehicleTemplates WHERE classification = '" + cbClassification.getValue() + "'");

            // load templates into drop down
            while ( rs.next() )
            {
                String make = rs.getString("make");
                String model = rs.getString("model");
                Double engine = rs.getDouble("engineSize");
                String fuel = rs.getString("fuelType");
                
                vehicleTemplate x = new vehicleTemplate(make, model, engine, fuel);
                cbGiven.getItems().add(x.showTemplate());
            }
            // close connection
            c.close();            
        }
        catch (SQLException e)
        {
            
        } 
    }
    
    
    
    //Fill textboxes when you select a template vehicle
    @FXML
    private void chooseTemplate()
    {
        try
        {
            String template = (String)cbGiven.getValue();
            String[] parts = template.split(", ");
            txtMake.setText(parts[0]);
            txtModel.setText(parts[1]);
            txtEngine.setText(parts[2]);
            txtFuel.setText(parts[3]);
        } 
        catch(NullPointerException e)
        {
            
        }
    }
            
    
    
    //searching for a customer, showing options in the menu and displaying selected on in the labels 
    @FXML
    private void searchCustomer()
    {
        multipleCust.getItems().clear();
        try
        {
            Connection c = DatabaseConnector.activateConnection();
            c.setAutoCommit( true ); 
            ResultSet rs;
            Statement stmt = c.createStatement();
            rs = stmt.executeQuery("SELECT * FROM Customer WHERE name LIKE '%" + txtSearch.getText() + "%'"); 
               
            ObservableList matchingCustomers = FXCollections.observableArrayList();      
            
            while(rs.next())
            {  
                Integer customerID = rs.getInt("customerID");
                String name = rs.getString("name");
                String addressL1 = rs.getString("addressLine1");
                String addressLine2 = rs.getString("addressLine2");
                String postcode = rs.getString("postcode");
                String phoneNo= rs.getString("phoneNumber");
                String email = rs.getString("email");
                String type = rs.getString("type");
                
                customers.logic.Customer x = new customers.logic.Customer(customerID, name, addressL1, addressLine2, postcode, phoneNo, email, type);
                matchingCustomers.add(x);  
            }
            
            nameCol.setCellValueFactory(new PropertyValueFactory("name"));
            addressCol.setCellValueFactory(new PropertyValueFactory("addressL1"));
                       
            multipleCust.getItems().addAll(matchingCustomers);
            
            multipleCust.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> 
            {
                lblName.setText(((customers.logic.Customer)newSelection).getName());
                lblAddress.setText(((customers.logic.Customer)newSelection).getAddressL1());
                lblNumber.setText(((customers.logic.Customer)newSelection).getPhoneNo());
                
                chosenCustomer = (customers.logic.Customer)newSelection;
                
            });
            
            c.close();
            
        }
        catch (SQLException e)
        {
            
        } 
        
    }
    
    @FXML
    private void save() throws IOException
    {
        //checks that a customer has been selected
        if(!lblName.getText().equals("Unknown"))
        {   //check that all of the data has been entered
            if(!txtReg.getText().equals("") && !cbClassification.getValue().equals("") && !txtMake.getText().equals("") && !txtModel.getText().equals("") && !txtEngine.getText().equals("") && !txtFuel.getText().equals("") && !txtColour.getText().equals("") && !txtMOT.getText().equals("") && !txtService.getText().equals("") && !txtMileage.getText().equals("") && !datePicker.getValue().equals("") && !startTimeHour.getValue().equals("") && !startTimeMin.getValue().equals("") && !endTimeHour.getValue().equals("") && !endTimeMin.getValue().equals("") && !cbMechanic.getValue().equals("") )
            {   //check that the MOT and Service dates are in the correct format 
                if(validateDate(txtMOT.getText()) &&  validateDate(txtService.getText()))
                {
                    int warranty = 0;
                    
                    if(chWarranty.isSelected())
                    {
                        warranty = 1;
                    }
                    //checks if there is a warranty and that the warranty info is in the correct format 
                    if(chWarranty.isSelected() && validateDate(txtExpiration.getText()) || !chWarranty.isSelected())
                    { 
                        try
                        {
                            Connection c = DatabaseConnector.activateConnection();
                            c.setAutoCommit( true ); 
                            Statement stmt = c.createStatement(); 

                            //adding vehicle 
                            String sql = addVehicle(warranty);
                            stmt.executeUpdate(sql); ;

                            //adding warranty 
                            if(chWarranty.isSelected())
                            {
                                sql = addWarranty();
                                stmt.executeUpdate(sql);
                            }

                            //adding booking
                            sql = addBooking();
                            stmt.executeUpdate(sql); 
                            
                            c.close();
                            
                            addBill();

                            Pane NewContent=FXMLLoader.load(getClass().getResource("/vehicles/GUI/Vehicle.fxml"));
                            common.FXMLDocumentController.changeContentPane(NewContent);

                        }
                        catch (SQLException e)
                        {
                            //trying to add a vheicle that has already been entered (repeated reg)
                            Alerts.repeatedReg();
                        }
                    }
                    else
                    {   //warranty date invalid
                        Alerts.invalidDate();
                    }
                }
                else
                {   //MOT or service date invalid 
                    Alerts.invalidDate();
                }
            }
            else
            {   //missing data 
                Alerts.missingData();
            }
        }
        else
        {   //no customer selected
            Alerts.noCustomer();
        }
    }
    
    //adding the new vehicle to the database using textbox info
    public String addVehicle(int warranty)
    {
        return ("INSERT INTO Vehicle (registration, classification, make, model, engineSize, fuelType, colour, motDate, serviceDate, mileage, customerID, hasWarranty) VALUES('"      
                                                                            + txtReg.getText() + "','" 
                                                                            + cbClassification.getValue() + "','" 
                                                                            + txtMake.getText() + "','" 
                                                                            + txtModel.getText() + "','" 
                                                                            + Double.parseDouble(txtEngine.getText()) + "','"
                                                                            + txtFuel.getText() + "','" 
                                                                            + txtColour.getText() + "','" 
                                                                            + txtMOT.getText() + "','" 
                                                                            + txtService.getText() + "','" 
                                                                            + Double.parseDouble(txtMileage.getText()) + "','" 
                                                                            + chosenCustomer.getCustomerID() + "','"
                                                                            + warranty + "')"
        );
    }
    
    
    //adding a warranty for the vehicle using the textbox info 
    public String addWarranty()
    {
        return ("INSERT INTO Warranty (date, companyName, companyAddressLine1, companyAddressLine2, companyAddressPostCode, vehicleRegistration)  VALUES ('"    
                                                                    + txtExpiration.getText() + "','"
                                                                    + txtCompany.getText() + "','"
                                                                    + txtAddress1.getText() + "','"
                                                                    + txtAddress2.getText() + "','"
                                                                    + txtPostcode.getText() + "','"
                                                                    + txtReg.getText() + "')"
                
        );
    }
    
    
    //adding a booking for the vehicle using the datepicker / dropdown boxes info 
    public String addBooking()
    {
        String chosenMech = (cbMechanic.getValue()).toString();
        String ID = chosenMech.substring(chosenMech.length() - 5);
                          
        String strBookingDate = diagnosis_repairs.logic.Booking.fromLocalDate( datePicker.getValue() ) ;
          
        // get the start + end times for the booking
        int startHr  = Integer.parseInt( startTimeHour.getValue() ) ;
        int startMin = Integer.parseInt( startTimeMin.getValue() ) ;
        int endHr    = Integer.parseInt( endTimeHour.getValue() ) ;
        int endMin   = Integer.parseInt( endTimeMin.getValue() ) ;
        LocalTime bookingStartTime = LocalTime.of( startHr , startMin  ) ;
        LocalTime bookingEndTime   = LocalTime.of( endHr   , endMin  ) ;
                            
        return("INSERT INTO Booking (date, startTime, endTime, employeeID, vehicleRegistration) VALUES ( '"    
                                                                    + strBookingDate + "','"
                                                                    + bookingStartTime.toString() + "','"
                                                                    + bookingEndTime.toString() + "','"
                                                                    + ID + "','" //employee ID
                                                                    + txtReg.getText() + "')"
        );
    }
    
    
    //adding a 'defualt' bill for the vehicle connected to the booking you have just made 
    public void addBill()
    {
        try 
        {
            int bookingID = 0;
                    
            Connection c = DatabaseConnector.activateConnection();
            c.setAutoCommit(true);
            Statement stmt = c.createStatement();
            ResultSet rs;

            rs = stmt.executeQuery("SELECT MAX(bookingID) FROM Booking");
           
            if(rs.next())
            {
                bookingID = rs.getInt(1);
            }
            stmt.executeUpdate("INSERT INTO Bill (total, status, bookingID) VALUES (0.0, 'N/A', '" + bookingID + "')");
            
            c.close();
        } 
        catch (SQLException e) 
        {
          
        }
        
    }
  
    
    
    //shows or hides the warranty textboxes depending on if the checkbox is selected or not 
    @FXML
    private void changeWarranty()
    {
        if(chWarranty.isSelected())
        {
            lblWarranty.setVisible(true);
            txtCompany.setVisible(true); 
            txtAddress1.setVisible(true);
            txtAddress2.setVisible(true);
            txtPostcode.setVisible(true);
            txtExpiration.setVisible(true);
            
        }
        else
        {
            lblWarranty.setVisible(false);
            txtCompany.setVisible(false); 
            txtAddress1.setVisible(false);
            txtAddress2.setVisible(false);
            txtPostcode.setVisible(false);
            txtExpiration.setVisible(false);
        }
    }
    
    
    //method ot check if the date is in the dd/mm/yyyy format 
    public static boolean validateDate(String date)
    {
        if(date.matches("\\d\\d/\\d\\d/\\d\\d\\d\\d"))
        {
            try 
            {
                DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
                df.setLenient(false);
                df.parse(date);
                System.out.println("DATE OK");
                return true;
            } 
            catch (ParseException e) 
            {
                System.out.println("DATE INVALID");
                return false;
            }
        }
        else
        {
            System.out.println("NOT OK: " + date);
            return false;
        }
    }
    
    
//Date + Time limitations (Booking) --------------------------------------------------------------------------------------------------------------------------------------------------
    
    
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
                
                this.setDisable ( true )                        ;
                this.setStyle(" -fx-background-color: #ff0000; ") ;
                
            }
            
              // hunt + disable bank holidays
            if (   this.isBankHoliday(  Booking.fromLocalDate( item )  )   )
            {
            
                this.setDisable ( true ) ;
                this.setStyle(" -fx-background-color: #ff0000; ") ;
            
            }
            
        }

        //blocks out bank holidays - no reliable jar file for this, cna be done using web
        public boolean isBankHoliday( String date ) 
        {
            
            switch ( date )
            {            
                //Good Friday
                case "14/04/2017" : return true ;
                
                //Easter Monday
                case "17/04/2017" : return true ;
                
                //Early May Bank Holiday
                case "01/05/2017" : return true ;
                
                //Spring Bank Holiday
                case "29/05/2017" : return true ;
                
                //Summer Bank Holiday
                case "28/04/2017" : return true ;
                
                //Christmas Day
                case "25/12/2017" : return true ;
                
                //Boxing Day
                case "26/12/2017" : return true ;
                
                //Not a bank holiday
                default           : return false ;
            
            }
            
        }
                
    };
    
    // this helper method checks whether saturday has been selected from the datePicker
    @FXML
    private void checkIfSaturdaySelected()
    {
    
        showBookings(datePicker.getValue());
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
    

    //shows the current booking made for the day that you select from the date picker 
    private void showBookings(LocalDate item) 
    {
        bookingTable.getItems().clear();
        ObservableList matchingBookings = FXCollections.observableArrayList();     
          
        DateTimeFormatter theFormat = DateTimeFormatter.ofPattern( "dd/MM/yyyy" );
        String date = (item).format(theFormat);
        
        try
        {
            Connection c = DatabaseConnector.activateConnection();
            c.setAutoCommit( true ); 
            ResultSet rs;
            Statement stmt = c.createStatement();
            rs = stmt.executeQuery("SELECT * FROM Booking WHERE date = '" + date + "'"); 
               
            while(rs.next())
            {  
                String startTime = rs.getString("startTime");
                String endTime = rs.getString("endTime");
                int employee = rs.getInt("employeeID");
                    
                String employeeID = ""+employee;
                   
                bookingDay x = new bookingDay(startTime, endTime, employeeID);
                     
                matchingBookings.add(x);  
            }

            startCol.setCellValueFactory(new PropertyValueFactory("startTime"));
            endCol.setCellValueFactory(new PropertyValueFactory("endTime"));
            empCol.setCellValueFactory(new PropertyValueFactory("employeeID"));
                
            bookingTable.getItems().addAll(matchingBookings);

        }
        catch (SQLException e)
        {
                
        }
    } 
    
    
    
}//end of class 
