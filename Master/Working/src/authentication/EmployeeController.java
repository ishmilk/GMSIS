/*
 * Author: Amy Dowse
 * EDITED BY : ismail.
 * Controller for when viewing, editing and deleteing the users 
 */
package authentication;

import common.CurrentUser;
import common.DatabaseConnector;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;


public class EmployeeController implements Initializable
{
    //Table 
    @FXML TableView tableEmployee = new TableView();
    @FXML TableColumn<Employee,Integer> IDCol = new TableColumn();
    @FXML TableColumn<Employee,String> nameCol = new TableColumn();
    @FXML TableColumn<Employee,Integer> adminCol = new TableColumn();
    @FXML TableColumn<Employee,Integer> mechanicCol = new TableColumn();
    @FXML TableColumn<Employee,Double> rateCol = new TableColumn();
    
    @FXML Label lblID = new Label();
    @FXML TextField txtFirstName = new TextField();
    @FXML TextField txtLastName = new TextField();
    @FXML TextField txtPassword1 = new TextField();
    @FXML TextField txtPassword2 = new TextField();
    @FXML TextField txtHourlyRate = new TextField();
    @FXML CheckBox cbAdmin = new CheckBox();
    @FXML CheckBox cbMechanic = new CheckBox();
    
    private String currentUser = CurrentUser.getInstance().getUserName();
    
    private ObservableList<Employee> allEmp = FXCollections.observableArrayList();
    
    @Override
    public void initialize(URL url, ResourceBundle rb) 
    {
       Load();
    }
    
    
    //Linking to the Add Employee Screen 
    public void AddEmp() throws IOException
    {
       Pane NewContent=FXMLLoader.load(getClass().getResource("AddEmployee.fxml"));
       common.FXMLDocumentController.changeContentPane(NewContent);
    }
   
 
    //DELETE --------------------------------------------------------------------------------------------------------------------------------------------
    
    public void Delete()
    {
        //stops you from deleting the account you are currently logged in on 
       if((lblID.getText()).equals(currentUser ))
       {
           unableToDelete();
       }
       
       else
       {
           System.out.println("ismail :" + lblID.getText());
           // check if a user is currently selected
           if ( lblID.getText().equals( "ID Number" ) )     // user currently !selected
           {
           
               diagnosis_repairs.logic.CodeBank.showErrorAlert( "No user selected" , "Please select a user to delete" );
           
           }
           
           else                                             // user currently selected
           {
           
               Optional<ButtonType> result = confirmDelete();
           
                if (result.get() == ButtonType.OK)
                {
                    
                    try
                    {
                        
                        Connection c = DatabaseConnector.activateConnection();
                        c.setAutoCommit( true ); 
                        Statement stmt = c.createStatement();    

                        String sql = "DELETE FROM Employee WHERE employeeID = '" + lblID.getText() + "'";

                        stmt.executeUpdate(sql); 
                        c.close();

                        deleteSuccessful(lblID.getText());
                        resetView();

                        tableEmployee.getItems().clear();

                        Load();  
                        
                    }
                    
                    catch (SQLException e)
                    {

                        e.printStackTrace() ;
                    
                    }
                    
                } 
                
                else
                {
                
                    lblID.setText( "ID Number" ) ; // reset ID label state
                    
                    tableEmployee.getItems().clear();
                    Load();
                
                }
           
            }
           
       }
       
    }
    
    
    
    //DELETE HELPER METHODS--------------------------------------------------------------------------------------------------------------------
    
    //confirms delete has happened 
    private void deleteSuccessful(String userID)
    {
       Alert alert = new Alert(Alert.AlertType.INFORMATION);
       alert.setTitle("Delete Successful");
       alert.setHeaderText("Delete Successful");
       alert.setContentText("You have deleted " + userID);
       alert.showAndWait();
    }
    
    //informs the user they cannot delete the account they are loggeed in on 
    private void unableToDelete()
    {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Unable to Delete");
        alert.setHeaderText("You cannot delete the account that you are currently logged in on");
        alert.setContentText("This employee account will NOT be deleted \nTo continue, press OK");
        alert.showAndWait();
    }
    
    //asks the user to confirm they want to delete that user 
    private  Optional<ButtonType> confirmDelete()
    {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation on Delete");
        alert.setHeaderText("You are about to delete a employee record");
        alert.setContentText("To delete, press OK");
        return  alert.showAndWait(); 
    }
    
    
    
    //SAVE -----------------------------------------------------------------------------------------------------------------------------------------
    
    public void SaveChanges()
    {
        if((lblID.getText()).equals(currentUser ))
        {
           cannotEdit();
        }
        else
        {
            //checks all of the data has been entered 
            if(!txtFirstName.getText().equals("") || !txtLastName.getText().equals("") || !txtPassword1.getText().equals("") || !txtPassword2.getText().equals(""))
            {   //checks that the 2 passwors match 
                if(txtPassword1.getText().equals(txtPassword2.getText()))
                {   //if they are a mechanic, check that an hourly rate is entered 
                    if(!cbMechanic.isSelected() || cbMechanic.isSelected() && !txtHourlyRate.getText().equals(""))
                    {
                        try
                        {
                            Connection c = DatabaseConnector.activateConnection();
                            c.setAutoCommit( true ); 
                            Statement stmt = c.createStatement();    

                            String fullName = txtFirstName.getText() + " " + txtLastName.getText();

                            String sql = "UPDATE Employee SET    name = '" +  fullName +  
                                                                "', password = '" +  txtPassword1.getText() +
                                                                "' WHERE employeeID = '" + lblID.getText() + "'";
                            stmt.executeUpdate(sql);

                            if(cbAdmin.isSelected())
                            {
                                sql = "UPDATE Employee SET isAdmin = 1, isMechanic = 0, hourlyRate = 0 WHERE employeeID = '" + lblID.getText() + "'";
                                stmt.executeUpdate(sql);
                            }
                            else if(cbMechanic.isSelected())
                            {
                                sql = "UPDATE Employee SET isAdmin = 0, isMechanic = 1, hourlyRate = '" + txtHourlyRate.getText() + "' WHERE employeeID = '" + lblID.getText() + "'";
                                stmt.executeUpdate(sql);
                            }
                            else
                            {
                                sql = "UPDATE Employee SET isAdmin = 0, isMechanic = 0, hourlyRate = 0 WHERE employeeID = '" + lblID.getText() + "'";
                                stmt.executeUpdate(sql);
                            }

                            c.close();

                            tableEmployee.getItems().clear();
                            Load();    
                        }
                        catch (SQLException e)
                        {

                        }
                    }
                    else
                    {
                        missingData();
                    }
                }
                else
                {
                    unmatchedPassword();

                }
            }
            else
            {
                missingData();
            }
        }
    }
    
 
    //SAVE HELPER METHODS ------------------------------------------------------------------------------------------------------------------------------------
    
    public static void cannotEdit()
    {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Unable to Edit");
        alert.setHeaderText("You are unable to edit");
        alert.setContentText("You cannot edit the profile you are currently logged in on");
        alert.showAndWait();
    }
    
    //inform the user the 2 passwords entered don't match 
    public static void unmatchedPassword()
    {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Password");
        alert.setHeaderText("The password you have entered is not consistent");
        alert.setContentText("Please re-enter the pass word and click 'SAVE' again");
        alert.showAndWait();
    }
    
    //inform the user that some data is missing 
    public static void missingData()
    {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Missing Data");
        alert.setHeaderText("You have not entered all needed data");
        alert.setContentText("Please fill in the missing data and press 'SAVE' again");
        alert.showAndWait();
    }
    
    
    
    //Loading the main screen -------------------------------------------------------------------------------------------------------------------------------
    
    //loads all of the employees onto the screen when you laod it 
    public void Load()
    {   
        allEmp.clear();
        
        try
        {
            // open a connection
            Connection c = DatabaseConnector.activateConnection();
            c.setAutoCommit( true ); 
            ResultSet rs ;

            // when creating a statement object, you MUST use a connection object to call the instance method
            Statement stmt = c.createStatement();
            rs = stmt.executeQuery("SELECT * FROM Employee"); 
              
            while(rs.next())
            {  
                Integer employeeID = rs.getInt("employeeID");
                String name = rs.getString("name");
                String password = rs.getString("password");
                Integer isAdmin = rs.getInt("isAdmin");
                Integer isMechanic = rs.getInt("isMechanic");
                Double hourlyRate = rs.getDouble("hourlyRate");
                                
                if(isMechanic == 1)
                {
                    Mechanic x = new Mechanic(employeeID, password, name, 0, 1);
                    x.setHourlyRate(hourlyRate);
                    allEmp.add(x);
                }
                else if (isAdmin == 1)
                {
                    Admin x = new Admin(employeeID, password, name, 1, 0);
                    allEmp.add(x);
                }
                else
                {
                    Employee x = new Employee(employeeID, password, name, 0, 0);
                    allEmp.add(x);
                }
       
            }
           
            c.close();
                         
            IDCol.setCellValueFactory(new PropertyValueFactory<Employee,Integer>("employeeID"));
            nameCol.setCellValueFactory(new PropertyValueFactory<Employee,String>("name"));
            adminCol.setCellValueFactory(new PropertyValueFactory<Employee,Integer>("isAdmin"));
            mechanicCol.setCellValueFactory(new PropertyValueFactory<Employee,Integer>("isMechanic"));
            rateCol.setCellValueFactory(new PropertyValueFactory<Employee,Double>("hourlyRate"));

            tableEmployee.getItems().addAll(allEmp);
            
                    tableEmployee.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection)
                    -> {
                      
                        cbAdmin.setSelected(false);
                        cbMechanic.setSelected(false);
                        
                        if(newSelection != null)
                        {
                            
                            String[] parts = (((Employee) newSelection).getName()).split(" ");
                            lblID.setText(""+((Employee) newSelection).getEmployeeID());
                            txtFirstName.setText(parts[0]);
                            txtLastName.setText(parts[1]);

                            txtPassword1.setText(((Employee) newSelection).getPassword());
                            txtPassword2.setText(((Employee) newSelection).getPassword());

                            if(((Employee) newSelection).getIsAdmin()==1)
                            {
                                cbAdmin.setSelected(true);
                            }
                            
                            if(((Employee) newSelection).getIsMechanic()==1)
                            {
                                cbMechanic.setSelected(true);
                                
                                txtHourlyRate.setVisible(true);
                                txtHourlyRate.setText("" + ((Mechanic) newSelection).getHourlyRate());
                            }
                            else
                            {
                                txtHourlyRate.setVisible(false);
                            }
                        }
                    });
                          
            
        }
        catch (SQLException e)
        {
            
        } 
    }
    
    
    
    //HELPER METHODS - Cannot be Admin and Mechanic ------------------------------------------------------------------------------------------------------------
    
    //checks only admin selected (deselect mechanic)
    @FXML
    public void checkOnlyAdmin()
    {
        if(cbMechanic.isSelected())
        {
            cbMechanic.setSelected(false);
        }
        txtHourlyRate.setVisible(false);
    }
    
    //checks only mechanic selected (deselect admin)
    @FXML void checkOnlyMechanic()
    {
        if(cbAdmin.isSelected())
        {
            cbAdmin.setSelected(false);
        }
        if(cbMechanic.isSelected())
        {
            txtHourlyRate.setVisible(true);
            txtHourlyRate.setText("");
        }
        else
        {
            txtHourlyRate.setVisible(false);
        }
    }
    
    //when deleting a user reset the screen to be how it was when first loaded 
    public void resetView()
    {
        lblID.setText("ID Number");
        txtFirstName.setText("");
        txtLastName.setText("");
        txtPassword1.setText("");
        txtPassword2.setText("");
        cbAdmin.setSelected(false);
        cbMechanic.setSelected(false);
        txtHourlyRate.setText("");
               
    }
    
}//End of class 
