/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
* check git connection
 */
package customers.gui;

import customers.logic.BookList;
import customers.logic.Customer;
import customers.logic.DataFunc;
import customers.logic.Store;
import customers.logic.VehList;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import vehicles.Logic.vehicle;

/**
 * FXML Controller class
 *
 * @author P Sanjeev v
 */
public class CustProfileController implements Initializable 
{
    ObservableList<vehicle> data =FXCollections.observableArrayList();
    ObservableList<vehicle> data1 =FXCollections.observableArrayList();
    Customer cc;
    @FXML
    private AnchorPane AnchorPane;
    @FXML
    private TableColumn<VehList,String> VRegNo;
    @FXML
    private TableColumn<VehList,String> VMake;
    @FXML
    private TableColumn<VehList,String> VModel;
    @FXML
    private TableColumn<VehList,String> VMileage;
    @FXML
    private TableColumn<BookList, Integer> BookID;
    @FXML
    private TableColumn<BookList, Integer> BID;
    @FXML
    private TableColumn<BookList, String> BDurat;
    @FXML
    private TableColumn<BookList, Integer> BCost;
    @FXML
    private TableColumn<BookList, Integer> BEmp;
    @FXML
    private RadioButton type1;
    @FXML
    private RadioButton type2;
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
    private Label IDLabel;
    @FXML
    private TableView<vehicle> Vehicle;

    @FXML
    public void DeleteCust(ActionEvent e) throws SQLException
    {
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Delete Customer");
        alert.setHeaderText(null);
        alert.setContentText("Are you sure you want to delete this Account?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK)
        {
            DataFunc dd = new DataFunc();
            dd.DelCust(cc.getCustomerID());
        } 

    }
    
    @FXML
    public void AddNewCust(ActionEvent e) throws SQLException
    {
        String type;
        if(type1.isSelected())
        {
            type="Individual";
        }
        else
        {
            type="Business";
        }
        Customer cc=new Customer(0,txtName.getText(),txtAddr1.getText(),txtAddr2.getText(),txtPost.getText(),txtEmail.getText(),txtPhone.getText(),type);
        DataFunc dd = new DataFunc();
        dd.AddCust(cc);
    }
    
    @FXML
    public void UpdateCust(ActionEvent e) throws SQLException
    {
        String type;
        if(type1.isSelected())
        {
            type="Individual";
        }
        else
        {
            type="Business";
        }
        Customer ne=new Customer(cc.getCustomerID(),txtName.getText(),txtAddr1.getText(),txtAddr2.getText(),txtPost.getText(),txtPhone.getText(),txtEmail.getText(),type);
        DataFunc dd = new DataFunc();
        dd.UpdateCust(ne);
    }
    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
         cc=Store.getC();
        IDLabel.setText(""+cc.getCustomerID());
        txtName.setText(cc.getName());
        txtAddr1.setText(cc.getAddressL1());
        txtAddr2.setText(cc.getAddressL2());
        txtPost.setText(cc.getPostCode());
        txtEmail.setText(cc.getEmail());
        txtPhone.setText(cc.getPhoneNo());
        if(cc.getType().equals("Business"))
        {
            type2.setSelected(true);
        }
        else
        {
            type1.setSelected(true);
        }
        
        
        
        
        VRegNo.setCellValueFactory(new PropertyValueFactory<VehList, String>("Reg"));
        VMake.setCellValueFactory(new PropertyValueFactory<VehList, String>("make"));
        VModel.setCellValueFactory(new PropertyValueFactory<VehList, String>("model"));
        VMileage.setCellValueFactory(new PropertyValueFactory<VehList, String>("mile"));
        
        BookID.setCellValueFactory(new PropertyValueFactory<BookList, Integer>("bookID"));
        BID.setCellValueFactory(new PropertyValueFactory<BookList, Integer>("billID"));
        BDurat.setCellValueFactory(new PropertyValueFactory<BookList, String>("date"));
        BCost.setCellValueFactory(new PropertyValueFactory<BookList, Integer>("cost"));
        BEmp.setCellValueFactory(new PropertyValueFactory<BookList, Integer>("empID"));
        DataFunc dd = null;
        ArrayList <vehicle> a = null;
        ArrayList <BookList> s = null;
        try 
        {
            dd = new DataFunc();
            a=dd.getAllVeh(cc.getCustomerID());
            for(vehicle b:a)
            {
                data.add(b);
                s=dd.getAllBook(b.getReg());
                //IDList.add(b.getId());
            
                //System.out.println (b.getId()+"   "+ b.getName()+"   "+b.getPhone());
            }
            Vehicle.setItems(data);
            Vehicle.setOnMousePressed(new EventHandler<MouseEvent>() 
            {
                @Override 
                public void handle(MouseEvent event) 
                {
                    if (event.getClickCount() == 2) 
                    {
                    vehicle av = Vehicle.getSelectionModel().getSelectedItem() ;  
                    //System.out.println(av.getId()+"\n"+av.getName()+"\n"+av.getPhone());
                    DataFunc dd;
                    Alert alert = new Alert(AlertType.INFORMATION);
                    alert.setTitle("Information Dialog");
                    alert.setHeaderText(null);
                    alert.setContentText("           Parts Associated with this Vehicle \n"
                            + "");
                    alert.getDialogPane().setMinSize(250, 300);
                    alert.showAndWait();
                    
                }
             }
        });
        } 
        catch (SQLException ex) 
        {
            //Logger.getLogger(CustomerMainController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }    
    
}
