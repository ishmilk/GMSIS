/*
 * Authoer: Amy Dowse
 * The controller for the searchVehicle fxml screen - no editing capabilities 
 */
package vehicles.Logic;

import common.Context;
import common.DatabaseConnector;
import static diagnosis_repairs.logic.Booking.toLocalDate;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;


public class SearchVehicleController implements Initializable
{
    
    private String searchFor = Context.getInstance().getSearch();
    private String criteria = Context.getInstance().getCriteria();
    
    //list
    @FXML TableView tableReg;
    @FXML TableColumn regCol;
 
    //vhicle
    @FXML Label lblClassification = new Label();
    @FXML Label lblReg = new Label();
    @FXML Label lblMake = new Label();
    @FXML Label lblModel = new Label();
    @FXML Label lblEngine = new Label();
    @FXML Label lblFuel = new Label();
    @FXML Label lblColour = new Label();
    @FXML Label lblMOT = new Label();
    @FXML Label lblService = new Label();
    @FXML Label lblMileage = new Label();
    @FXML private CheckBox cbWarranty = new CheckBox();
      
    //warranty
    @FXML Label lblCompanyName = new Label();
    @FXML Label lblCompanyA1 = new Label();
    @FXML Label lblCompanyA2 = new Label();
    @FXML Label lblCompanyPost = new Label();
    @FXML Label lblExpiration = new Label();
    
    //owner
    @FXML Label lblName = new Label();
    @FXML Label lblAddress1 = new Label();
    @FXML Label lblAddress2 = new Label();
    @FXML Label lblPostcode = new Label();
    @FXML Label lblEmail = new Label();
    @FXML Label lblPhone = new Label();
    @FXML Label lblType = new Label();
    
         
    //bookings
    @FXML TableView tableBooking;
    @FXML TableColumn bookingDate;
    @FXML TableColumn bookingStart;
    @FXML TableColumn bookingEnd;
    @FXML TableColumn bookingCost;
    @FXML TableColumn bookingStatus;
    
    //parts 
    @FXML TableView tableParts;
    @FXML TableColumn partName;
    @FXML TableColumn partDescription;
    @FXML TableColumn partInstalled;
    
    //select the query depending on criteria and displays all matching vehile regs for user to select 
    @Override
    public void initialize(URL url, ResourceBundle rb) 
    {
        lblEmail.setPrefWidth(175);
        lblEmail.setWrapText(true);
        
        try
        {
            Connection c = DatabaseConnector.activateConnection();
            c.setAutoCommit( true ); 
            ResultSet rs ;
            Statement stmt = c.createStatement();    
            
            String sql;
            if(criteria.equals("Registration"))
            {
                sql = ("SELECT * FROM Vehicle WHERE registration LIKE '%" + searchFor +"%'");
            }
            else if(criteria.equals("Make"))
            {
                sql = ("SELECT * FROM Vehicle WHERE make LIKE '%" + searchFor +"%'");
            }
            else if(criteria.equals("Type"))
            {
                sql = ("SELECT * FROM Vehicle WHERE classification = '" + searchFor +"'");
            }
            else
            {
                String[] parts = searchFor.split(", ");
                                
                sql = ("SELECT * FROM Vehicle WHERE make = '" + parts[0] + "' AND model = '" + parts[1] + "' AND engineSize = '" + parts[2] + "' AND fuelType = '" + parts[3] + "'");
            }
                    
            
            rs = stmt.executeQuery(sql); 
            ObservableList matchingVehicles = FXCollections.observableArrayList();
            
            while(rs.next())
            {
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
                matchingVehicles.add(x);
                
            }
             
            
                regCol.setCellValueFactory(new PropertyValueFactory("reg"));

                tableReg.getItems().addAll(matchingVehicles);

                tableReg.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> 
                {
                    lblReg.setText(((vehicle)newSelection).getReg());
                    lblMake.setText(((vehicle)newSelection).getMake());
                    lblModel.setText(((vehicle)newSelection).getModel());
                    lblEngine.setText(""+((vehicle)newSelection).getEngineSize());
                    lblColour.setText(((vehicle)newSelection).getColour());
                    lblMileage.setText(""+((vehicle)newSelection).getMileage());
                    lblFuel.setText(((vehicle)newSelection).getFuel());
                    lblClassification.setText(((vehicle)newSelection).getClassification()); 

                    DateTimeFormatter theFormat = DateTimeFormatter.ofPattern( "dd/MM/yyyy" );
                    String MOTString = (((vehicle) newSelection).getMOT()).format(theFormat);
                    String serviceString = (((vehicle) newSelection).getService()).format(theFormat);
                            
                    lblMOT.setText(MOTString);
                    lblService.setText(serviceString);
                    
                    tableBooking.getItems().clear();
                    tableParts.getItems().clear();
                    
                    showWarranty(lblReg.getText());
                    showOwner(lblReg.getText());
                    showBookings(lblReg.getText());
                    showParts(lblReg.getText());
                });

            
                      
            
            
            c.close();   
        }
        catch (SQLException e)
        {
            
        }
           
    }
    
    
    //shows the warranty information of a selected vehicle if there is one
    public void showWarranty(String reg)
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
                    lblCompanyName.setText("N/A");
                    lblCompanyA1.setText("N/A");
                    lblCompanyA2.setText("N/A");
                    lblCompanyPost.setText("N/A");
                    lblExpiration.setText("N/A");
                    cbWarranty.setSelected(false);     
                }
                else
                {
                    lblCompanyName.setText(rs.getString("companyName"));
                    lblCompanyA1.setText(rs.getString("companyAddressLine1"));
                    lblCompanyA2.setText(rs.getString("companyAddressLine2"));
                    lblCompanyPost.setText(rs.getString("companyAddressPostCode"));
                    lblExpiration.setText(rs.getString("date"));
                    cbWarranty.setSelected(true);
                }
            }
            catch (SQLException e)
            {

            }
    }
    
    
    //shows the owner information of the selected vehicle 
    public void showOwner(String reg)
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
            }
            catch (SQLException e)
            {

            }
    }
    
    
    //showing all of the bookings + bill for the selected vehicle - past and present - uses the Search object 
    public void showBookings(String reg)
    {
        try
        {
            Connection c = DatabaseConnector.activateConnection();
            c.setAutoCommit( true ); 
            Statement stmt = c.createStatement();  
            ResultSet rs ;
            
            rs = stmt.executeQuery("SELECT bookingID, date, startTime, endTime FROM Booking WHERE vehicleRegistration = '" + reg + "'"); 
                
            ObservableList matchingBookings = FXCollections.observableArrayList();
                       
            while(rs.next())
            {
                Integer ID = rs.getInt("bookingID");
                String date = rs.getString("date");
                String startTime = rs.getString("startTime");
                String endTime = rs.getString("endTime"); 
               
                Search x = new Search(ID, date, startTime, endTime);
                matchingBookings.add(x); 
            }
            
            
            for(int i=0; i<matchingBookings.size(); i++)
            {
                Search y = (Search)matchingBookings.get(i);
                
                String sql = "SELECT total, status FROM Bill WHERE bookingID = '" + y.getBookingID() + "'";
                
                rs = stmt.executeQuery(sql);
                
                if(rs.next())
                {
                    y.setTotal(rs.getString("total"));
                    y.setStatus(rs.getString("status"));
                }
            }
                                 
            bookingDate.setCellValueFactory(new PropertyValueFactory("date"));
            bookingStart.setCellValueFactory(new PropertyValueFactory("startTime"));
            bookingEnd.setCellValueFactory(new PropertyValueFactory("endTime"));
            bookingCost.setCellValueFactory(new PropertyValueFactory("total"));
            bookingStatus.setCellValueFactory(new PropertyValueFactory("status"));

            tableBooking.getItems().addAll(matchingBookings);
            
            c.close(); 
               
        }
        catch (SQLException e)
        {
            
        }
    }
    
    
    //showing all of the parts used on the selected vehicle 
    public void showParts(String reg)
    {
        try
            {
                Connection c = DatabaseConnector.activateConnection();
                c.setAutoCommit( true ); 
                Statement stmt = c.createStatement();  
                ResultSet rs ;
                
                rs = stmt.executeQuery("SELECT * FROM Booking, Parts WHERE Booking.vehicleRegistration = '" + reg + "' AND Booking.bookingID = Parts.bookingID"); 
                
                ObservableList matchingParts = FXCollections.observableArrayList();
            
                while(rs.next())
                {
                    Integer partID = rs.getInt("partID");
                    String name = rs.getString("name");
                    String description = rs.getString("description");
                    Double cost = rs.getDouble("cost");
                    String dateInStock = rs.getString("dateInStock"); 
                    String dateInstalled = rs.getString("dateInstalled"); 
                    Integer bookingID = rs.getInt("bookingID");
                    
                    
                    LocalDate partInStock = toLocalDate(dateInStock);                     
                                    
                    
                    parts.logic.Part x = new parts.logic.Part(partID, name, description, cost, partInStock);
                    
                    if(dateInstalled != null)
                    {
                        LocalDate partInstalled = toLocalDate(dateInstalled);
                        x.setDateInstalled(partInstalled);
                    }
                                      
                    matchingParts.add(x);

            }
                
            partName.setCellValueFactory(new PropertyValueFactory("PartName"));
            partDescription.setCellValueFactory(new PropertyValueFactory("PartDescr"));
            partInstalled.setCellValueFactory(new PropertyValueFactory("dateInstalled"));
                       
            tableParts.getItems().addAll(matchingParts);
      
            }
            catch (SQLException e)
            {

            }
    }

  
   
}//end of class 
