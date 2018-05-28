/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package parts.logic;

import common.Context;
import common.DatabaseConnector;
import static diagnosis_repairs.logic.Booking.toLocalDate;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TablePosition;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import vehicles.Logic.vehicle;

/**
 *
 * @author User
 */
public class searchPartsController implements Initializable
{
    @FXML TableView customerTable = new TableView();
    @FXML TableColumn customerNameCol = new TableColumn();
    @FXML TableColumn registrationCol = new TableColumn();
    
    @FXML TableView<Part> partsTable = new TableView();
    @FXML TableColumn partNameCol = new TableColumn();
    @FXML TableColumn installedCol = new TableColumn();
    
    @FXML TextField partID = new TextField();
    @FXML TextField partName = new TextField();
    @FXML TextArea partDescription = new TextArea();
    @FXML TextField partCost = new TextField();
    @FXML TextField partDateInStock = new TextField();
    @FXML TextField partDateInstalled = new TextField();
    @FXML TextField partWarranty = new TextField();
    @FXML TextField partRegisteredTo = new TextField();
    @FXML TextField partBookingID = new TextField();
    
    @FXML Label vehicleClassification = new Label();
    @FXML Label vehicleRegistration = new Label();
    @FXML Label vehicleMake = new Label();
    
    @FXML Label customerName = new Label();
    @FXML Label customerAddress = new Label();
    @FXML Label customerPostCode = new Label();
    
    private String searchFor = Context.getInstance().getSearch();
    private String criteria = Context.getInstance().getCriteria();
    
    ObservableList matchingParts = FXCollections.observableArrayList();
    private ObservableList<TablePosition> selectedCells;
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) 
    {
        try
        {
            Connection c = DatabaseConnector.activateConnection();
            c.setAutoCommit( true ); 
            ResultSet rs ;
            Statement stmt = c.createStatement();    
            
            String sql;
            if(criteria.equals("Registration"))
            {
                sql = ("SELECT name, registration, Customer.customerID FROM Vehicle, Customer WHERE registration LIKE '%" + searchFor +"%' AND Customer.customerID == Vehicle.customerID");
            }
            else //Name
            {
                sql = ("SELECT name, registration, Customer.customerID FROM Vehicle, Customer WHERE name LIKE '%" + searchFor +"%' AND Customer.customerID == Vehicle.customerID");
            }  
            
            rs = stmt.executeQuery(sql); 
            ObservableList matchingCustomers = FXCollections.observableArrayList();
            
            while(rs.next())
            {
                String name = rs.getString("name");
                String reg = rs.getString("registration");
                int ID = rs.getInt("customerID");
                
                Search x = new Search(name, reg, ID);
                matchingCustomers.add(x);
            }
               
            customerNameCol.setCellValueFactory(new PropertyValueFactory("name"));
            registrationCol.setCellValueFactory(new PropertyValueFactory("reg"));
                
            customerTable.getItems().addAll(matchingCustomers);

            customerTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> 
            {
                partsTable.getItems().clear();
                clearPartsTextboxes();
                
                showPartList((Search) newSelection);
                showCustomer((Search) newSelection);
                showVehicle((Search) newSelection);
            });

            c.close();   
        }
        catch (SQLException e)
        {
            
        }
    }//end of init
    
    
    
    public void showPartList(Search selected)
    {
        try
            {
                Connection c = DatabaseConnector.activateConnection();
                c.setAutoCommit( true ); 
                Statement stmt = c.createStatement();  
                ResultSet rs ;
                
                rs = stmt.executeQuery("SELECT Parts.partID, Parts.name, Parts.description, Parts.cost, Parts.dateInStock, Parts.dateInstalled, Parts.bookingID FROM Booking, Parts WHERE Booking.vehicleRegistration = '" + selected.getReg() + "' AND Booking.bookingID = Parts.bookingID"); 
                
            
                while(rs.next())
                {
                    Integer partID = rs.getInt("partID");
                    String name = rs.getString("name");
                    String description = rs.getString("description");
                    double cost = rs.getDouble("cost");
                    String dateInStock = rs.getString("dateInStock"); 
                    String dateInstalled = rs.getString("dateInstalled"); 
                    Integer bookingID = rs.getInt("bookingID");
                    
                    System.out.println("xxxx: " + cost);
                    
                    LocalDate partInStock = toLocalDate(dateInStock);                     
                                    
                    
                    parts.logic.Part x = new parts.logic.Part(partID, name, description, cost, partInStock);
                    System.out.println("ASSIGN: "+x.getPartCost());
                    if(dateInstalled != null)
                    {
                        LocalDate partInstalled = toLocalDate(dateInstalled);
                        x.setDateInstalled(partInstalled);
                    }
                                      
                    matchingParts.add(x);

                }

                partNameCol.setCellValueFactory(new PropertyValueFactory("PartName"));
                installedCol.setCellValueFactory(new PropertyValueFactory("dateInstalled"));

                partsTable.getItems().addAll(matchingParts);
                
                partsTable.setOnMouseClicked(new EventHandler<MouseEvent>() {
            
                    @Override
                    public void handle(MouseEvent click) {
                        if (click.getClickCount() == 1) {
                            
                            selectedCells = partsTable.getSelectionModel().getSelectedCells();
                            if (selectedCells != null && !selectedCells.isEmpty()) {
                                TablePosition pos = selectedCells.get(0);

                                int row = pos.getRow();
                                partsTable.getSelectionModel().select(row); // select whole row on click
                                
                                Part selectedPart = partsTable.getItems().get(row);
                                showParts(selectedPart);
                            }
                        }
                    }

                });
          
                /*
                partsTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> 
                {
                    if(newSelection != null)
                    {
                        showParts((Part) newSelection);
                        
                    }                    
                });
                
                */
            
            }
            catch (SQLException e)
            {

            }
    }
    
    
    
    public void showParts(Part selectedPart)
    {      

           partID.setText(Integer.toString(selectedPart.getPartID()));
           partName.setText(selectedPart.getPartName());
           partDescription.setText(selectedPart.getPartDescr());
           System.out.println(selectedPart.getPartCost());
           partCost.setText(""+(selectedPart.getPartCost()));
 
           DateTimeFormatter theFormat = DateTimeFormatter.ofPattern( "dd/MM/yyyy" ) ;
           String dateInStock = (selectedPart.getDateInStock()).format(theFormat);
           
           
           partDateInStock.setText(dateInStock);
           
           
           if(selectedPart.getWarrantyExpired())
           {
               partWarranty.setText("Under Warranty");
           }
           else
           {
               partWarranty.setText("Warranty Expired");
           }
           
           try
           {
               String dateInstalled = (selectedPart.getDateInstalled()).format(theFormat);
               partDateInstalled.setText(dateInstalled);
           }
           catch (NullPointerException e)
           {
               
           }
    }//end of show parts
    
    
    public void showCustomer(Search selected)
    {
        try
        {
            Connection c = DatabaseConnector.activateConnection();
            c.setAutoCommit( true ); 
            ResultSet rs ;
            Statement stmt = c.createStatement();    
            
            String sql = ("SELECT name, addressLine1, postCode FROM Customer WHERE customerID = '" + selected.getID() +"'");
           
            rs = stmt.executeQuery(sql); 
            
            customerName.setText(rs.getString("name"));
            customerAddress.setText(rs.getString("addressLine1"));
            customerPostCode.setText(rs.getString("postCode"));
            
            c.close();   
        }
        catch (SQLException e)
        {
            
        }
    }
    
    
    public void showVehicle(Search selected)
    {
        try
        {
            Connection c = DatabaseConnector.activateConnection();
            c.setAutoCommit( true ); 
            ResultSet rs ;
            Statement stmt = c.createStatement();    
            
            String sql = ("SELECT classification, make FROM Vehicle WHERE registration = '" + selected.getReg() +"'");
           
            rs = stmt.executeQuery(sql); 
            
            vehicleClassification.setText(rs.getString("classification"));
            vehicleRegistration.setText(selected.getReg());
            vehicleMake.setText(rs.getString("make"));
            
            c.close();   
        }
        catch (SQLException e)
        {
            
        }
    }
    
    
    
    public void clearPartsTextboxes()
    {
        partID.setText("");
        partName.setText("");
        partDescription.setText("");
        partCost.setText("");
        partDateInStock.setText("");
        partDateInstalled.setText("");
        partWarranty.setText("");
    }
    
    
}//end of class
