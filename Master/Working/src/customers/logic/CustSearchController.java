/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package customers.logic;

import common.Context;
import customers.logic.BookList;
import customers.logic.Customer;
import customers.logic.DataFunc;
import customers.logic.PartList;
import customers.logic.VehList;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
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
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;

/**
 * FXML Controller class
 *
 * @author P Sanjeev v
 */
public class CustSearchController implements Initializable {
    ObservableList<Customer> data = FXCollections.observableArrayList();
    ObservableList<VehList> data1 = FXCollections.observableArrayList();
    ObservableList<BookList> data2 = FXCollections.observableArrayList();
    ObservableList<PartList> data3 = FXCollections.observableArrayList();
    @FXML
    private TableView<Customer> Cust;
    @FXML
    private TableColumn<?, ?> custName;
    @FXML
    private Label LBname;
    @FXML
    private Label LBaddr1;
    @FXML
    private Label LBaddr2;
    @FXML
    private Label LBpost;
    @FXML
    private Label LBemail;
    @FXML
    private Label LBphone;
    @FXML
    private TableView<VehList> InfoVeh;
    @FXML
    private TableColumn<?, ?> viewReg1;
    @FXML
    private TableColumn<?, ?> viewType;
    @FXML
    private TableColumn<?, ?> viewModel;
    @FXML
    private TableColumn<?, ?> viewFuel;
    @FXML
    private TableColumn<?, ?> viewMake;
    @FXML
    private Label LBtype;
    @FXML
    private TableView<BookList> BookInfo;
    @FXML
    private TableColumn<?, ?> ViewID;
    @FXML
    private TableColumn<?, ?> VviewDate;
    @FXML
    private TableColumn<?, ?> viewEmp;
    @FXML
    private TableColumn<?, ?> viewCost;
    @FXML
    private TableView<PartList> PartInfo;
    @FXML
    private TableColumn<?, ?> PartName;
    @FXML
    private TableColumn<?, ?> PartDesc;
    @FXML
    private TableColumn<?, ?> PartDate;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) 
    {
        Context ct=Context.getInstance();
        String name = ct.getSearch();
        ArrayList<Customer> a=new ArrayList<>();
        try {
            if(ct.getCriteria().equals("Name"))
            {
                a=(new DataFunc()).getSearchCrit(name);
            }
            else
            {
                a=(new DataFunc()).getSearchReg(name);
            }
        } catch (SQLException ex) {
            Logger.getLogger(CustSearchController.class.getName()).log(Level.SEVERE, null, ex);
        }
        custName.setCellValueFactory(new PropertyValueFactory<>("Name"));
        for(Customer b: a)
        {
            data.add(b);
        }
        Cust.setItems(data);
        Cust.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                Customer cc = Cust.getSelectionModel().getSelectedItem();
                try {
                    setLabels(cc);
                    updateVeh(cc.getCustomerID());
                } catch (SQLException ex) {
                    Logger.getLogger(CustSearchController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }
    public void setLabels(Customer cc)
    {
        LBname.setText(cc.getName());
        LBaddr1.setText(cc.getAddressL1());
        LBaddr2.setText(cc.getAddressL2());
        LBpost.setText(cc.getPostCode());
        LBemail.setText(cc.getEmail());
        LBphone.setText(cc.getPhoneNo());
        LBtype.setText(cc.getType());
    }
    public void updateVeh(int ID) throws SQLException
    {
        clear();
        viewReg1.setCellValueFactory(new PropertyValueFactory<>("Reg"));
        viewType.setCellValueFactory(new PropertyValueFactory<>("type"));
        viewModel.setCellValueFactory(new PropertyValueFactory<>("model"));
        viewFuel.setCellValueFactory(new PropertyValueFactory<>("fuel"));
        viewMake.setCellValueFactory(new PropertyValueFactory<>("make"));
        DataFunc dd=new DataFunc();
        ArrayList<VehList> aa=new ArrayList<>();
        aa=dd.getVeh(ID);
        for(VehList b: aa)
        {
            data1.add(b);
            updateBook(b.getReg());
        }
        InfoVeh.setItems(data1);
    }
    public void clear()
    {
        InfoVeh.getItems().clear();
        BookInfo.getItems().clear();
        PartInfo.getItems().clear();
    }
    public void updateBook(String reg) throws SQLException
    {
        
        //System.out.println(Reg);
        ViewID.setCellValueFactory(new PropertyValueFactory<>("bookID"));
        viewEmp.setCellValueFactory(new PropertyValueFactory<>("empID"));
        viewCost.setCellValueFactory(new PropertyValueFactory<>("cost"));
        VviewDate.setCellValueFactory(new PropertyValueFactory<>("date"));

        DataFunc tt = null;
        ArrayList<BookList> a = new ArrayList<>();
        tt = new DataFunc();
        a = tt.getAllBook(reg);
        for (BookList b : a) {
            data2.add(b);
            updatePart(b.getBookID());
            //updateBill(b.getBookID());
            System.out.println(b.getDate());
        }
        BookInfo.setItems(data2);
    }
    public void updatePart(int id) throws SQLException
    {
        PartName.setCellValueFactory(new PropertyValueFactory<>("name"));
        PartDesc.setCellValueFactory(new PropertyValueFactory<>("desc"));
        PartDate.setCellValueFactory(new PropertyValueFactory<>("install"));
        DataFunc dd=null;
        ArrayList <PartList> a=new ArrayList<>();
        dd=new DataFunc();
        a=dd.getPart(id);
        for(PartList b:a)
        {
            data3.add(b);
        }
        PartInfo.setItems(data3);
    }
}
