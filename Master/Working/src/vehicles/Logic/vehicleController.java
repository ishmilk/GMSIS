/*
 * Author: Amy Dowse
 * The controller for the Vehicle fxml screen - show, edit and delete the vehicles  
 */
package vehicles.Logic;

import common.DatabaseConnector;
import vehicles.Logic.Warranty;
import static diagnosis_repairs.logic.Booking.toLocalDate;
import java.sql.*;               // imports Java SQL package 
/******************************************************************************/
import java.io.IOException;
import java.net.URL;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;


public class vehicleController implements Initializable {
    
    //vehicle table 
    @FXML TableColumn regCol;
    @FXML TableColumn ownerCol;
    @FXML TableColumn makeCol;
    @FXML TableColumn modelCol;
    @FXML TableView tableView;
    
    //vehilce
    @FXML private TextField txtReg = new TextField();
    @FXML private TextField txtMake = new TextField();
    @FXML private TextField txtModel = new TextField();
    @FXML private TextField txtEngine = new TextField();
    @FXML private TextField txtColour = new TextField();
    @FXML private TextField txtFuel = new TextField();
    @FXML private TextField txtMileage = new TextField();
    @FXML private TextField txtMOT = new TextField();
    @FXML private TextField txtService = new TextField();
    @FXML private ComboBox cbClassification = new ComboBox();
   
   //warranty 
   @FXML private Label lblWarranty = new Label();
   @FXML private TextField txtCompany = new TextField();
   @FXML private TextField txtAddress1 = new TextField();
   @FXML private TextField txtAddress2 = new TextField();
   @FXML private TextField txtPostcode = new TextField();
   @FXML private TextField txtExpiration = new TextField();
   @FXML private CheckBox cbWarranty = new CheckBox();
   
   //cutomer
   @FXML private Label lblName = new Label();
   @FXML private Label lblAddress1 = new Label();
   @FXML private Label lblAddress2 = new Label();
   @FXML private Label lblPostcode = new Label();
   @FXML private Label lblEmail = new Label();
   @FXML private Label lblPhone = new Label();
   @FXML private Label lblType = new Label();
   
   //booking
   @FXML private Label lblDate = new Label();
   @FXML private Label lblStartTime = new Label();
   @FXML private Label lblEndTime = new Label();
   @FXML private Label lblMechanic = new Label();
   
    private ObservableList allVehicles = FXCollections.observableArrayList();    
    
    
    
    //Start the page ------------------------------------------------------------------------------------------------------------------------------------------------------------
    
    @Override
    public void initialize(URL url, ResourceBundle rb) 
    {
        lblEmail.setPrefWidth(175);
        lblEmail.setWrapText(true);
        
        //get the values of the Combo box for classification 
        cbClassification.getItems().addAll("Car", "Van", "Truck");
        
        diagnosis_repairs.logic.CodeBank.applyDecimalRestriction(txtEngine);
        diagnosis_repairs.logic.CodeBank.applyDecimalRestriction(txtMileage);
        
        Load();
    }
    
    //change page when you click to add a vehicle 
    @FXML
    public void AddVeh(ActionEvent event) throws IOException
    {
        Pane NewContent=FXMLLoader.load(getClass().getResource("/vehicles/GUI/AddVehicle.fxml"));
        common.FXMLDocumentController.changeContentPane(NewContent);
    }
    
    
    //LOAD THE PAGE ---------------------------------------------------------------------------------------------------------------------------------------------------------
    
    //brings all of the vehicle details into the list and makes the details appear in the textboxes when selected 
    public void Load()
    {     
        allVehicles.clear();
        try
        {
            // open a connection
            Connection c = DatabaseConnector.activateConnection();
            c.setAutoCommit( true ); 
            ResultSet rs ;

            // when creating a statement object, you MUST use a connection object to call the instance method
            Statement stmt = c.createStatement();
            rs = stmt.executeQuery("SELECT * FROM Vehicle"); 
            while(rs.next())
            {  
                //Get warrenty to work properly 
                Integer warrantyInt = rs.getInt("hasWarranty");
                String reg = rs.getString("registration");
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
                
                LocalDate MOTDate = toLocalDate(MOT);
                LocalDate serviceDate = toLocalDate(service);
                Boolean warranty = false;
                
                if(warrantyInt == 1)
                {
                    warranty = true;
                }
                
                vehicle x = new vehicle(warranty, classification, reg, make, model, engine, fuel, colour, MOTDate, serviceDate, mileage);
                allVehicles.add(x);
                
            }
           
            c.close();
                    
            regCol.setCellValueFactory(new PropertyValueFactory("reg"));
            makeCol.setCellValueFactory(new PropertyValueFactory("make"));
            modelCol.setCellValueFactory(new PropertyValueFactory("model"));

            tableView.getItems().addAll(allVehicles);
            
                    tableView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection)
                    -> {
                       
                        if(newSelection != null)
                        {
                            txtReg.setText(((vehicle) newSelection).getReg());
                            txtMake.setText(((vehicle) newSelection).getMake());
                            txtModel.setText(((vehicle) newSelection).getModel());
                            txtEngine.setText("" + ((vehicle) newSelection).getEngineSize());
                            txtColour.setText(((vehicle) newSelection).getColour());
                            txtMileage.setText("" + ((vehicle) newSelection).getMileage());
                            txtFuel.setText(((vehicle) newSelection).getFuel());
                            cbClassification.getSelectionModel().select(((vehicle) newSelection).getClassification());

                            
                            DateTimeFormatter theFormat = DateTimeFormatter.ofPattern( "dd/MM/yyyy" );
                            String MOTString = (((vehicle) newSelection).getMOT()).format(theFormat);
                            String serviceString = (((vehicle) newSelection).getService()).format(theFormat);
                            
                            txtMOT.setText(MOTString);
                            txtService.setText(serviceString);

                            txtCompany.setText("");
                            txtAddress1.setText("");
                            txtAddress2.setText("");
                            txtPostcode.setText("");
                            txtExpiration.setText("");

                            showCustomerInfo(txtReg.getText());
                            showWarrantyInfo(txtReg.getText());
                            showBookingInfo(txtReg.getText());
                        }
                    });
                     
            
        }
        catch (SQLException e)
        {
            
        } 
    }
    
    
    //shows the customer details associated with the selected vehicle (in labels)
    public void showCustomerInfo(String reg)
    {
        try
            {
                Connection c = DatabaseConnector.activateConnection();
                c.setAutoCommit( true ); 
                Statement stmt = c.createStatement();  
                ResultSet rs ;
                
                rs = stmt.executeQuery("SELECT * FROM Vehicle, Customer WHERE registration = '" + reg + "' AND Customer.customerID = Vehicle.customerID"); 
                
                lblName.setText(rs.getString("name"));
                lblAddress1.setText(rs.getString("addressLine1"));
                lblAddress2.setText(rs.getString("addressLine2"));
                lblPostcode.setText(rs.getString("postCode"));
                lblEmail.setText(rs.getString("email"));
                lblPhone.setText(rs.getString("phoneNumber"));
                lblType.setText(rs.getString("type"));  
                
                c.close();
            }
            catch (SQLException e)
            {
                    
            }
    }
    
    
    //shows the warranty details (if there is one) for the vehicle that is selected - in textboxes 
    public void showWarrantyInfo(String reg)
    {
        try
            {
                Connection c = DatabaseConnector.activateConnection();
                c.setAutoCommit( true ); 
                Statement stmt = c.createStatement();  
                ResultSet rs ;
                
                rs = stmt.executeQuery("SELECT * FROM Warranty WHERE vehicleRegistration = '" + reg + "'");
                
                if(rs.next() == false)
                {
                    lblWarranty.setVisible(false);
                    txtCompany.setVisible(false);
                    txtAddress1.setVisible(false);
                    txtAddress2.setVisible(false);
                    txtPostcode.setVisible(false);
                    txtExpiration.setVisible(false);
                    cbWarranty.setSelected(false);     
                }
                else
                {
                    lblWarranty.setVisible(true);
                    txtCompany.setVisible(true);
                    txtAddress1.setVisible(true);
                    txtAddress2.setVisible(true);
                    txtPostcode.setVisible(true);
                    txtExpiration.setVisible(true);
                    cbWarranty.setSelected(true);
                    
                    LocalDate Expiration = toLocalDate(rs.getString("date"));
                    
                    vehicles.Logic.Warranty x = new Warranty(Expiration, rs.getString("companyName"), rs.getString("companyAddressLine1"), rs.getString("companyAddressLine2"), rs.getString("companyAddressPostCode"), reg);
                    
                    txtCompany.setText(x.getCompanyName());
                    txtAddress1.setText(x.getCompanyAddressLine1());
                    txtAddress2.setText(x.getCompanyAddressLine2());
                    txtPostcode.setText(x.getCompanyAddressPostCode());
                    txtExpiration.setText(rs.getString("date"));
                }
                
                c.close();
            }
            catch (SQLException e)
            {

            }
    }
    
    //shows the next booking details (in labels) for the vehicle that is selected 
    public void showBookingInfo(String reg) 
    {
        try
            {
                Connection c = DatabaseConnector.activateConnection();
                c.setAutoCommit( true ); 
                Statement stmt = c.createStatement();  
                ResultSet rs ;
                
                ArrayList bookings = new ArrayList();
                 
                rs = stmt.executeQuery("SELECT * FROM Booking WHERE vehicleRegistration = '" + reg + "'"); 
                
                while(rs.next())
                {
                    Integer employeeID = rs.getInt("employeeID");
                    Integer bookingID = rs.getInt("bookingID");
                    String date = rs.getString("date");
                    String startTime = rs.getString("startTime");
                    String endTime = rs.getString("endTime");
                    
                    LocalDate bookingDate = toLocalDate(date);
                    LocalTime bookingStartTime = diagnosis_repairs.logic.Booking.toLocalTime(startTime);
                    LocalTime bookingEndTime = diagnosis_repairs.logic.Booking.toLocalTime(endTime);
                  
                    diagnosis_repairs.logic.Booking x = new diagnosis_repairs.logic.Booking(bookingID, 0, bookingDate, bookingStartTime, bookingEndTime, 0.0, employeeID, reg);
                    bookings.add(x);
                }
                
                //find the closest date to current date 
                ArrayList<diagnosis_repairs.logic.Booking> future = findFuture(bookings);
              
                if(future.size() != 0)
                {
                    diagnosis_repairs.logic.Booking nextBooking = findNext(future);
                
                    //convert LocalDate to String
                    DateTimeFormatter theFormat = DateTimeFormatter.ofPattern( "dd/MM/yyyy" );
                    String dateString = (nextBooking.getDate()).format(theFormat);

                    try
                    {
                        lblDate.setText(dateString);
                        lblStartTime.setText((nextBooking.getStartTime()).toString());
                        lblEndTime.setText((nextBooking.getEndTime()).toString());
                        lblMechanic.setText(nextBooking.getEmployeeID());
                    }
                    catch (Exception e)
                    {

                    }
                }
                else
                {
                    lblDate.setText("N/A");
                    lblStartTime.setText("N/A");
                    lblEndTime.setText("N/A");
                    lblMechanic.setText("N/A");
                }
                
                
                c.close();

            }
            catch (SQLException e)
            {

            }
    }
    
    //creates a list of all of the future bookings for a vehicle 
    public ArrayList<diagnosis_repairs.logic.Booking> findFuture(ArrayList<diagnosis_repairs.logic.Booking> allBookings)
    {
        ArrayList<diagnosis_repairs.logic.Booking> future = new ArrayList<diagnosis_repairs.logic.Booking>();
        LocalDate now = LocalDate.now();
        
        for(int i = 0; i<allBookings.size(); i++)
        {
            diagnosis_repairs.logic.Booking consider = ((diagnosis_repairs.logic.Booking)allBookings.get(i));
           
           //tests if left is before right 
            if(now.isBefore(consider.getDate()))
            {
                future.add(consider);
            }
        }
        return future;
    }
    
    //from a list of all future bookings, find the one closest to todays date 
    public diagnosis_repairs.logic.Booking findNext (ArrayList<diagnosis_repairs.logic.Booking> futureBookings)
    {
        diagnosis_repairs.logic.Booking nextBooking = futureBookings.get(0);
       
        
        for(int i = 0; i<futureBookings.size(); i++)
        {
            diagnosis_repairs.logic.Booking consider = ((diagnosis_repairs.logic.Booking)futureBookings.get(i));
            
            //tests if left is before right 
            if((consider.getDate()).isBefore((nextBooking).getDate()))
            {
                nextBooking = consider;
            }
            else if((consider.getStartTime()).isBefore((nextBooking).getStartTime()))
            {
                nextBooking = consider;
            }
        }
        
        return nextBooking;
    }
    
    
    
    
    
    
    
    
    
    //DELETE ---------------------------------------------------------------------------------------------------------------------------------------------------------------
    
    //delete a the vehicle that is in the texboxes 
    @FXML
    public void Delete(ActionEvent event) throws IOException
    {
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Confirmation on Delete");
        alert.setHeaderText("You are about to delete a vehicle record");
        alert.setContentText("To delete, press OK");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK)
        {
            try
            {
                Connection c = DatabaseConnector.activateConnection();
                c.setAutoCommit( true ); 
                Statement stmt = c.createStatement();    

                ResultSet rs;
                String sql = "SELECT CustomerID FROM Vehicle WHERE registration = '" + txtReg.getText() + "'"; //gets the customer ID of the vehicle you are deleting
                rs = stmt.executeQuery(sql);
                int customer = rs.getInt("CustomerID");
                
                sql = "DELETE FROM Vehicle WHERE registration = '" + txtReg.getText() + "'"; //deletes the actual vehicle 
                
                stmt.executeUpdate(sql); 
                
                c.close();
                
                deleteBookings(txtReg.getText()); //delete connected bookings
                deleteCustomer(customer, txtReg.getText()); //check if there are no more vehicles for that customer, delete that customer 

                tableView.getItems().clear();
                resetView();
                Load();    
            }
            catch (SQLException e)
            {

            }
        } 
        else 
        {
            
        }
    }
    
    
    //delete any bookings associated with the deleted vehicles 
    public void deleteBookings(String reg)
    {
        try 
        {
            Connection c = DatabaseConnector.activateConnection();
            c.setAutoCommit(true);
            Statement stmt = c.createStatement();
            ResultSet rs;
    
            ArrayList<Integer> allBookings = new ArrayList<Integer>();
    
            String sql = "SELECT bookingID FROM Booking WHERE vehicleRegistration = '" + txtReg.getText() + "'"; //gets all of the bookings IDs of the ones to delte 
            
            rs = stmt.executeQuery(sql);
    
            while(rs.next())
            {
                Integer booking = rs.getInt("bookingID");
                allBookings.add(booking);
            }

            sql = "DELETE FROM Booking WHERE vehicleRegistration = '" + txtReg.getText() + "'"; //deletes all connected bookings 

            stmt.executeUpdate(sql);
            
            c.close();
    
            deleteParts(allBookings); //deltes are parts associated with the deleted bookings 
    
        } 
        catch (SQLException e) 
        {

        }
    }
    
    
    //delete all the parts that are associated with the bookings that have been deleted 
    private void deleteParts(ArrayList<Integer> bookings)
    {
        try 
        {
            Connection c = DatabaseConnector.activateConnection();
            c.setAutoCommit(true);
            Statement stmt = c.createStatement();

            for(int i = 0; i<bookings.size(); i++)
            {
                String sql = "DELETE FROM Parts WHERE bookingID = '" + bookings.get(i) + "'";
                stmt.executeUpdate(sql);
            }
            c.close();
            
            deleteBills(bookings);

        } 
        catch (SQLException e) 
        {

        }
    }
    
    //delete all the bills that are associated with the bookings that have been deleted
    public void deleteBills(ArrayList<Integer> bookings)
    {
        try 
        {
            Connection c = DatabaseConnector.activateConnection();
            c.setAutoCommit(true);
            Statement stmt = c.createStatement();

            for(int i = 0; i<bookings.size(); i++)
            {
                String sql = "DELETE FROM Bill WHERE bookingID = '" + bookings.get(i) + "'";
                stmt.executeUpdate(sql);
            }
            c.close();
        } 
        catch (SQLException e) 
        {

        }
    }
    
    
    
    //if the vehicle you delete is the last vehicle associated with a customer, delete that customer 
    public void deleteCustomer(int CustomerID, String reg)
    {
        try 
        {
            Connection c = DatabaseConnector.activateConnection();
            c.setAutoCommit(true);
            Statement stmt = c.createStatement();
            ResultSet rs;
            
            rs = stmt.executeQuery("SELECT COUNT() FROM Vehicle WHERE customerID = '" + CustomerID + "'");
            
            if(rs.getInt(1) == 0 )
            {
                stmt.executeUpdate("DELETE FROM Customer WHERE CustomerID = '" + CustomerID + "'");
            }
            
            c.close();

        } 
        catch (SQLException e) 
        {

        }
        Alerts.deleteSuccessful(reg);
    }
    
    
    
    
   
    
   //SAVE ---------------------------------------------------------------------------------------------------------------------------------------------------------
    
    
    
    
    @FXML
    public void SaveChanges(ActionEvent event) throws IOException
    {
        //checks that all of the info has been entered
        if(!txtReg.getText().equals("") && !cbClassification.getValue().equals("") && !txtMake.getText().equals("") && !txtModel.getText().equals("") && !txtEngine.getText().equals("") && !txtFuel.getText().equals("") && !txtColour.getText().equals("") && !txtMOT.getText().equals("") && !txtService.getText().equals("") && !txtMileage.getText().equals(""))
        {   //checks that the dates are valid and in the right format 
            if(AddVehicleController.validateDate(txtMOT.getText()) && AddVehicleController.validateDate(txtService.getText()))
            {
                try
                {
                    Connection c = DatabaseConnector.activateConnection();
                    c.setAutoCommit( true ); 
                    Statement stmt = c.createStatement(); 

                    String sql = "UPDATE Vehicle SET    make = '" +  txtMake.getText() +  
                                                        "', model = '" +  txtModel.getText() +
                                                        "', colour = '" +  txtColour.getText() +
                                                        "', engineSize = '" +  Double.parseDouble(txtEngine.getText()) +
                                                        "', fuelType = '" +  txtFuel.getText() +
                                                        "', mileage = '" +  Double.parseDouble(txtMileage.getText()) +   
                                                        "', motDate = '" + txtMOT.getText() +
                                                        "', serviceDate = '" + txtService.getText() +
                    "' WHERE registration = '" + txtReg.getText() + "'";

                    stmt.executeUpdate(sql); 
                    c.close();

                    updateWarranty(txtReg.getText());

                    tableView.getItems().clear();
                    Load();    
                }
                catch (SQLException e)
                {

                }
            }
            else
            {
                //MOT or service date not in the correct format 
                Alerts.invalidDate();
            }
        }
        else
        {
            //missing data 
            Alerts.missingData();
        }
    
    }
    
    
    //updates the warranty information compared to what is now on the GUI
    public void updateWarranty(String reg)
    {
        try
        {
            Connection c = DatabaseConnector.activateConnection();
            c.setAutoCommit( true ); 
            Statement stmt = c.createStatement();  
            ResultSet rs;

            String sql = "SELECT * FROM Warranty WHERE vehicleRegistration = '" + reg + "'";
            rs = stmt.executeQuery(sql);

            if(rs.next())
            {
                //is already a warranty there 
                if(cbWarranty.isSelected() == false)
                {
                    //delete warranty and alter the vheicle table to show there is now no warrnaty  
                    String delete = "DELETE FROM Warranty WHERE vehicleRegistration = '" + reg + "'";
                    stmt.executeUpdate(delete);
                    
                    String alterVeh = "UPDATE Vehicle SET haswarranty = 0 WHERE registration = '" + reg + "'";
                    stmt.executeUpdate(alterVeh);
                }
                else
                {
                    //update the existing warranty 
                    if(!txtExpiration.getText().equals("") && !txtCompany.getText().equals("") && !txtAddress1.getText().equals("") && !txtAddress2.getText().equals("") && !txtPostcode.getText().equals(""))
                    {
                        if(AddVehicleController.validateDate(txtExpiration.getText()))
                        {
                            String updateWarranty = "UPDATE Warranty SET    date = '" + txtExpiration.getText() +
                                                                                "', companyName = '" + txtCompany.getText() +
                                                                                "', companyAddressLine1 = '" + txtAddress1.getText() +
                                                                                "', companyAddressLine2 = '" + txtAddress2.getText() +
                                                                                "', companyAddressPostCode = '" + txtPostcode.getText()
                                                                                + "' WHERE vehicleRegistration = '" + reg + "'";

                            stmt.executeUpdate(updateWarranty);
                        }
                        else
                        {
                            Alerts.invalidDate();
                        }
                    }
                    else
                    {
                        Alerts.missingData();
                    }
                }
            }
            else
            {
                //no recorded warranty
                if(cbWarranty.isSelected())
                {
                    if(!txtExpiration.getText().equals("") && !txtCompany.getText().equals("") && !txtAddress1.getText().equals("") && !txtAddress2.getText().equals("") && !txtPostcode.getText().equals(""))
                    {
                        if(AddVehicleController.validateDate(txtExpiration.getText()))
                        {
                            //add new warranty and alter vehicle 
                            String newWarranty = "INSERT INTO Warranty (date, companyName, companyAddressLine1, companyAddressLine2, companyAddressPostCode, vehicleRegistration)  VALUES ('"    
                                                                    + txtExpiration.getText() + "','"
                                                                    + txtCompany.getText() + "','"
                                                                    + txtAddress1.getText() + "','"
                                                                    + txtAddress2.getText() + "','"
                                                                    + txtPostcode.getText() + "','"
                                                                    + txtReg.getText() + "')";
                            stmt.executeUpdate(newWarranty);

                            String alterVeh = "UPDATE Vehile SET hasWarranty = 1 WHERE registration = '" + reg + "'";
                            stmt.executeUpdate(alterVeh);
                        }
                        else
                        {
                            Alerts.invalidDate();
                        }
                    }
                    else
                    {
                        Alerts.missingData();
                    }
                }
            }

            c.close();
            
        }
        catch (SQLException e)
        {

        }
    }
    
    
    //show or hide warranty textboxes depending on if the checkbox is selected 
    @FXML
    private void changeWarranty()
    {
        if(cbWarranty.isSelected())
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
    
    //when a vehcile is deleted reset all of the textboxes to be blank (same as when you first start)
    public void resetView()
    {
        cbClassification.getItems().clear();
        
        txtReg.setText("");
        txtMake.setText("");
        txtModel.setText("");
        txtEngine.setText("");
        txtColour.setText("");
        txtFuel.setText("");
        txtMOT.setText("");
        txtService.setText("");
        
        
        txtCompany.setText("");
        txtAddress1.setText("");
        txtAddress2.setText("");
        txtPostcode.setText("");
        txtExpiration.setText("");
        cbWarranty.setSelected(false);
 
        lblName.setText("Name");
        lblAddress1.setText("Address Line 1");
        lblAddress2.setText("Address Line 2");
        lblPostcode.setText("Postcode");
        lblEmail.setText("Email");
        lblPhone.setText("Phone Number");
        lblType.setText("Type");
   
        lblDate.setText("Date");
        lblStartTime.setText("Start Time");
        lblEndTime.setText("End Time");
        lblMechanic.setText("Mechanic");
    }
    
    
     
}//end of class 



