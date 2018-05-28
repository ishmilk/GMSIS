/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package customers.gui;

import common.DatabaseConnector;
import customers.logic.Customer;
import customers.logic.DataFunc;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.Pane;
import javafx.util.Callback;
import vehicles.Logic.vehicleTemplate;

/**
 * FXML Controller class
 *
 * @author P Sanjeev v
 */
public class AdvancedAddController implements Initializable {

    @FXML
    private TextField txtName;
    @FXML
    private TextField txtAddr1;
    @FXML
    private TextField txtAddr2;
    @FXML
    private TextField txtPost;
    @FXML
    private TextField txtEmail;
    @FXML
    private TextField txtPhone;
    @FXML
    private RadioButton type1;
    @FXML
    private ToggleGroup Type;
    @FXML
    private RadioButton type2;
    @FXML
    private TextField txtFuel;
    @FXML
    private TextField txtColour;
    @FXML
    private TextField txtMOT;
    @FXML
    private TextField txtService;
    @FXML
    private TextField txtMileage;
    @FXML
    private TextField txtReg;
    @FXML
    private CheckBox chWarranty;
    @FXML
    private Button btnSave;
    @FXML
    private Label lblWarranty;
    @FXML
    private TextField txtPostcode;
    @FXML
    private TextField txtAddress2;
    @FXML
    private TextField txtCompany;
    @FXML
    private TextField txtAddress1;
    @FXML
    private TextField txtExpiration;
    @FXML
    private ComboBox<String> cbClassification;
    @FXML
    private ComboBox<String> cbGiven;
    @FXML
    private TextField txtMake;
    @FXML
    private TextField txtModel;
    @FXML
    private TextField txtEngine;
    @FXML
    private ComboBox<String> cbMechanic;
    @FXML
    private DatePicker datePicker;
    @FXML
    private ChoiceBox<String> startTimeHour;
    @FXML
    private ChoiceBox<String> startTimeMin;
    @FXML
    private ChoiceBox<String> endTimeMin;
    @FXML
    private ComboBox<String> endTimeHour;
    int id;
    private boolean saturdaySelected;

     @Override
    public void initialize(URL url, ResourceBundle rb) 
    {
        //get the values of the Combo box for classification 
        cbClassification.getItems().addAll("Car", "Van", "Truck");
        diagnosis_repairs.logic.CodeBank.applyDecimalRestriction(txtEngine);
        diagnosis_repairs.logic.CodeBank.applyDecimalRestriction(txtMileage);
        showMechanics();
        showTemplates();
        
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
    public void showTemplates()
    {
        try
        {
            Connection c = common.DatabaseConnector.activateConnection();
            c.setAutoCommit( true ); 
            Statement stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM VehicleTemplates" );

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
        String template = (String)cbGiven.getValue();
        String[] parts = template.split(", ");
        txtMake.setText(parts[0]);
        txtModel.setText(parts[1]);
        txtEngine.setText(parts[2]);
        txtFuel.setText(parts[3]);
    }
            
    
    
    
    
    //searching for a customer, showing options in the menu and displaying selected on in the labels 
       
    @FXML
    private void save() throws IOException
    {
        //checks that a customer has been selected
           //check that all of the data has been entered
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
                            stmt.executeUpdate(sql); 
                            

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

                            Pane NewContent=FXMLLoader.load(getClass().getResource("Vehicle.fxml"));
                            common.FXMLDocumentController.changeContentPane(NewContent);

                        }
                        catch (SQLException e)
                        {
                                repeatedReg();
                        }
                    }
                    else
                    {   //warranty date invalid
                        invalidDate();
                    }
                }
                else
                {   //not or service date invalid 
                    invalidDate();
                }
            }
            else
            {   //missing data 
                missingData();
            }
        }


    
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
                                                                            + id + "','"
                                                                            + warranty + "')"
        );
    }
    
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
    
    
    
    
    
    //Helper methods for where there is incorrect data/input
    //Seperate alter methods for different types for ease of chaninging 
    //e.g.  if you want to change 'nocustomer' to error rather than alter - only change one, not all of them 
    
    public static void invalidDate()
    {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Invalid Date");
        alert.setHeaderText("The entered date(s) are in the incorrect format");
        alert.setContentText("Please enter dates as: dd/mm/yyyy");
        alert.showAndWait();
    }
    
    private void noCustomer()
    {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Missing Customer");
        alert.setHeaderText("There is no selected customer");
        alert.setContentText("Please search for and select the owner of the vehicle you are trying to save");
        alert.showAndWait();
    }
    
    public static void missingData()
    {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Missing Data");
        alert.setHeaderText("You have not entered all needed data");
        alert.setContentText("Please fill in the missing data and press 'SAVE' again");
        alert.showAndWait();
    }
    
     public static void repeatedReg()
    {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Repeated Registration");
        alert.setHeaderText("The registration you have entered is already registerd");
        alert.setContentText("Please check the registration you are entering");
        alert.showAndWait();
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
            //System.out.println("DATE OK");
            return true;
        }
        else
        {
            //System.out.println("NOT OK: " + date);
            return false;
        }
    }
    
    
//Date + Time limitations ---------------------------------------------------------------------------------------------------------------------------------------------------------
    
    
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

    @FXML
    private void save(ActionEvent event) throws SQLException 
    {
         String type;
         Connection c = DatabaseConnector.activateConnection();
                            c.setAutoCommit( true ); 
                            Statement stmt = c.createStatement();
        if(type1.isSelected())
        {
            type="Individual";
        }
        else
        {
            type="Business";
        }
        Customer cc=new Customer(0,txtName.getText(),txtAddr1.getText(),txtAddr2.getText(),txtPost.getText(),txtPhone.getText(),txtEmail.getText(),type);
        DataFunc dd = new DataFunc();
        id=dd.AddCust(cc);
        String sql=addBooking();
        stmt.executeUpdate(sql);
        int warranty = 0;
                    
                    if(chWarranty.isSelected())
                    {
                        warranty = 1;
                    }
        sql=addVehicle( warranty);
        stmt.executeUpdate(sql);
    }

    
     
    
}
