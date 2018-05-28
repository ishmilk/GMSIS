/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package customers.gui;

//import static com.sun.xml.internal.fastinfoset.alphabet.BuiltInRestrictedAlphabets.table;
import common.FXMLDocumentController;
import customers.logic.CustList;
import customers.logic.Customer;
import customers.logic.DataFunc;
import customers.logic.Store;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;

/**
 * FXML Controller class
 *
 * @author P Sanjeev v
 */
public class CustomerMainController implements Initializable 
{
    ObservableList<Customer> data =FXCollections.observableArrayList();
    @FXML
    private TableView<Customer> ActiveList;
    @FXML
    private TableColumn<Customer,Integer> IDList;
    @FXML
    private TableColumn<Customer,String> NameList;
    @FXML
    private TableColumn<Customer,String> PhoneList;
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
    private RadioButton type2;
    @FXML
    private TextField txtRegNo;
    @FXML
    private TextField txtColour;
    @FXML
    private ComboBox<?> Template;
    @FXML
    private TextField txtMile;
    @FXML
    private TextField txtMOT;
    @FXML
    private TextField txtService;
    @FXML
    private DatePicker TimeDate;
    @FXML
    private TextField txtTime;
    @FXML
    private TextField txtDuration;

    
    @FXML
    public void ShowAddCust(ActionEvent e) throws IOException
    {
        Pane newLoadedPane = FXMLLoader.load(getClass().getResource("AdvancedAdd.fxml"));
        //Content.getChildren().add(newLoadedPane);
        FXMLDocumentController.changeContentPane(newLoadedPane);
    }
    @FXML
    public void addNewCust(ActionEvent e) throws SQLException
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
        Customer cc=new Customer(0,txtName.getText(),txtAddr1.getText(),txtAddr2.getText(),txtPost.getText(),txtPhone.getText(),txtEmail.getText(),type);
        DataFunc dd = new DataFunc();
        dd.AddCust(cc);
        populateList();
        //ActiveList.getColumns().addAll(IDList,NameList,PhoneList);
    }
            /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    public void populateList()
    {
        ActiveList.getItems().clear();
        
        IDList.setCellValueFactory(new PropertyValueFactory<Customer, Integer>("id"));
        NameList.setCellValueFactory(new PropertyValueFactory<Customer, String>("name"));
        PhoneList.setCellValueFactory(new PropertyValueFactory<Customer, String>("phone"));
        DataFunc dd = null;
        ArrayList <Customer> a = null;
        try 
        {
            dd = new DataFunc();
            a=dd.GetAllCust();
        } 
        catch (SQLException ex) 
        {
            //Logger.getLogger(CustomerMainController.class.getName()).log(Level.SEVERE, null, ex);
        }

        for(Customer b:a)
        {
            data.add(b);
            //IDList.add(b.getId());
            
            //System.out.println (b.getId()+"   "+ b.getName()+"   "+b.getPhone());
        }
        ActiveList.setItems(data);
        ActiveList.setOnMousePressed(new EventHandler<MouseEvent>() 
        {
            @Override 
            public void handle(MouseEvent event) 
            {
                if (event.getClickCount() == 2) 
                {
                    CustList av = null;
                    //CustList av = ActiveList.getSelectionModel().getSelectedItem() ;  
                    //System.out.println(av.getId()+"\n"+av.getName()+"\n"+av.getPhone());
                    DataFunc dd;
                    try 
                    {
                        dd = new DataFunc();
                        Customer cc=dd.GetByID(av.getId());
                        Store.setC(cc);
                        Pane newLoadedPane = FXMLLoader.load(getClass().getResource("CustProfile.fxml"));
                        FXMLDocumentController.changeContentPane(newLoadedPane);
                    } 
                    catch (SQLException | IOException ex) 
                    {
                        Logger.getLogger(CustomerMainController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
             }
        });
    }
    @Override
    public void initialize(URL url, ResourceBundle rb) 
    {
        this.populateList();
       /* IDList.setCellValueFactory(new PropertyValueFactory<CustList, Integer>("id"));
        NameList.setCellValueFactory(new PropertyValueFactory<CustList, String>("name"));
        PhoneList.setCellValueFactory(new PropertyValueFactory<CustList, String>("phone"));
        DataFunc dd = null;
        ArrayList <CustList> a = null;
        try 
        {
            dd = new DataFunc();
            a=dd.GetAllCust();
        } 
        catch (SQLException ex) 
        {
            //Logger.getLogger(CustomerMainController.class.getName()).log(Level.SEVERE, null, ex);
        }

        for(CustList b:a)
        {
            data.add(b);
            //IDList.add(b.getId());
            
            //System.out.println (b.getId()+"   "+ b.getName()+"   "+b.getPhone());
        }
        ActiveList.setItems(data);
        ActiveList.setOnMousePressed(new EventHandler<MouseEvent>() 
        {
            @Override 
            public void handle(MouseEvent event) 
            {
                if (event.getClickCount() == 2) 
                {
                    CustList av = ActiveList.getSelectionModel().getSelectedItem() ;  
                    //System.out.println(av.getId()+"\n"+av.getName()+"\n"+av.getPhone());
                    DataFunc dd;
                    try 
                    {
                        dd = new DataFunc();
                        Customer cc=dd.GetByID(av.getId());
                        Store.setC(cc);
                        Pane newLoadedPane = FXMLLoader.load(getClass().getResource("CustProfile.fxml"));
                        FXMLDocumentController.changeContentPane(newLoadedPane);
                    } 
                    catch (SQLException | IOException ex) 
                    {
                        Logger.getLogger(CustomerMainController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
             }
        });
        /*CustList av =  ActiveList.getSelectionModel().selectedItemProperty().get();
        System.out.println(av.getId()+"\n"+av.getName()+"\n"+av.getPhone());
        ActiveList.setRowFactory(new Callback<TableView<CustList>, TableRow<CustList>>() 
        {
            @Override
            public TableRow<CustList> call(TableView<CustList> tv) 
            {
                TableRow<CustList> row = new TableRow<>();
                row.setOnMouseClicked(event -> 
                {
                    if (! row.isEmpty() && event.getButton()==MouseButton.PRIMARY
                            && event.getClickCount() == 2) 
                    {
                        
                        CustList clickedRow = row.getItem();
                        System.out.println(clickedRow.getId()+"\n"+clickedRow.getName()+"\n"+clickedRow.getPhone());
                    }
                });
                return null;
            }
        });
*/
    }
}
        

               
    

            
