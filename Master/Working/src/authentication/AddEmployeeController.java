/*
 * Author : Amy Dowse 
 * Controller for adding a new employee to the system (a new user)
 */
package authentication;

import common.DatabaseConnector;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;


public class AddEmployeeController implements Initializable
{
    @FXML Label lblID = new Label();
    @FXML TextField txtFirstName = new TextField();
    @FXML TextField txtLastName = new TextField();
    @FXML TextField txtPassword1 = new TextField();
    @FXML TextField txtPassword2 = new TextField();
    @FXML TextField txtHourlyRate = new TextField();
    @FXML CheckBox cbAdmin = new CheckBox();
    @FXML CheckBox cbMechanic = new CheckBox();
    
    @FXML TableView tableEmployee = new TableView();
    @FXML TableColumn<Employee,Integer> IDCol = new TableColumn();
    @FXML TableColumn<Employee,String> nameCol = new TableColumn();
    @FXML TableColumn<Employee,Integer> adminCol = new TableColumn();
    @FXML TableColumn<Employee,Integer> mechanicCol = new TableColumn();
    @FXML TableColumn<Employee,Double> rateCol = new TableColumn();
    
    private ObservableList<Employee> allEmp = FXCollections.observableArrayList();
    
    @Override
    public void initialize(URL url, ResourceBundle rb) 
    {
        try
        {
            // open a connection
            Connection c = DatabaseConnector.activateConnection();
            c.setAutoCommit( true ); 
            ResultSet rs ;

            // when creating a statement object, you MUST use a connection object to call the instance method
            Statement stmt = c.createStatement();
            rs = stmt.executeQuery("SELECT MAX(employeeID) FROM Employee"); 
           
            int nextID = (Integer.parseInt(rs.getString(1)))+1;
            lblID.setText(Integer.toString(nextID));
            
            LoadTable();
            
            c.close();
         
        }
        catch (SQLException e)
        {
            
        }
    }
    
    //populates the table with all of the current users 
    public void LoadTable()
    {
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
        }
        catch (SQLException e)
        {
            
        }
    }
    
    
    
    //hides the hourly rate when mechanic is NOT selected 
    @FXML
    public void hide()
    {
        txtHourlyRate.setVisible(false);
    }
    
     //shows the hourly rate when mechanic IS selected 
    @FXML
    public void show()
    {
        txtHourlyRate.setVisible(true);
        txtHourlyRate.setText("");
    }
    
    //adds the new employee (user) to the system
    @FXML
    public void addEmp() throws IOException
    {
        //checks that all of the information is entered
        if(!txtFirstName.getText().equals("") || !txtLastName.getText().equals("") || !txtPassword1.getText().equals("") || !txtPassword2.getText().equals(""))
        {   //checks that the 2 versions of the database are the same 
            if(txtPassword1.getText().equals(txtPassword2.getText()))
            {   //checks that if mechanic, the hourly rate is entered  
                if(!cbMechanic.isSelected() || cbMechanic.isSelected() && !txtHourlyRate.getText().equals(""))
                {
                    try
                    {
                        String name = txtFirstName.getText() + " " + txtLastName.getText();

                        int admin;
                        int mechanic;
                        double hourlyRate;

                        if(cbAdmin.isSelected())
                        {
                            admin = 1;
                            mechanic = 0;
                            hourlyRate = 0;
                        }
                        else if (cbMechanic.isSelected())
                        {
                            admin = 0;
                            mechanic = 1;
                            hourlyRate = Double.parseDouble(txtHourlyRate.getText());
                        }
                        else
                        {
                            admin = 0;
                            mechanic = 0;
                            hourlyRate = 0;
                        }


                        // open a connection
                        Connection c = DatabaseConnector.activateConnection();
                        c.setAutoCommit( true ); 
                        ResultSet rs ;

                        // when creating a statement object, you MUST use a connection object to call the instance method
                        Statement stmt = c.createStatement();
                        String sql = "INSERT INTO Employee (employeeID, name, password, isAdmin, hourlyRate, isMechanic) VALUES ('"
                                                                    + lblID.getText() + "','"
                                                                    + name + "','"
                                                                    + txtPassword1.getText() + "','"
                                                                    + admin + "','"
                                                                    + hourlyRate + "','"
                                                                    + mechanic + "')"; 



                        stmt.executeUpdate(sql);
                        c.close();

                        Pane NewContent=FXMLLoader.load(getClass().getResource("Employee.fxml"));
                        common.FXMLDocumentController.changeContentPane(NewContent);

                    }
                    catch (SQLException e)
                    {

                    }
                }
                else
                {
                    EmployeeController.missingData();
                }
            }
            else
            {
                EmployeeController.unmatchedPassword();
                
            }
        }
        else
        {
            EmployeeController.missingData();
        }
    }

    
    //ensures only Admin is selected (deselects the machinic) 
    @FXML
    public void checkOnlyAdmin()
    {
        if(cbMechanic.isSelected())
        {
            cbMechanic.setSelected(false);
        }
        txtHourlyRate.setVisible(false);
    }
    
    //ensures only Mechanic is selected (deselects the admin) 
    @FXML 
    public void checkOnlyMechanic()
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
    
}//End of class 
