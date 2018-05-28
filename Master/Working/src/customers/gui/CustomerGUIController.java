/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package customers.gui;

import vehicles.Logic.vehicle;
import common.FXMLDocumentController;
import customers.logic.BillList;
import customers.logic.BookList;
import customers.logic.Customer;
import customers.logic.DataFunc;
import customers.logic.PartList;
import customers.logic.Store;
import customers.logic.VehList;
import diagnosis_repairs.logic.Booking;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Optional;
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
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;

/**
 * FXML Controller class
 *
 * @author P Sanjeev v
 */
public class CustomerGUIController implements Initializable {

    //static Customer cc;
    Customer ct;
    @FXML
    private TableView<Customer> InfoTable;
    @FXML
    private TableColumn<Customer, Integer> TableID;
    @FXML
    private TableColumn<Customer, String> TableName;
    @FXML
    private TableColumn<Customer, String> TableEmail;
    @FXML
    private TableColumn<Customer, String> TablePhone;
    ObservableList<Customer> data = FXCollections.observableArrayList();
    ObservableList<VehList> data1 = FXCollections.observableArrayList();
    ObservableList<BookList> data2 = FXCollections.observableArrayList();
    ObservableList<PartList> data3 = FXCollections.observableArrayList();
    ObservableList<BillList> data4 = FXCollections.observableArrayList();
    @FXML
    private TextField txtName;
    @FXML
    private TextField txtAddr1;
    @FXML
    private TextField txtAddr2;
    @FXML
    private TextField txtPost;
    @FXML
    private TextField txtPhone;
    @FXML
    private TextField txtEmail;
    @FXML
    private RadioButton type1;
    @FXML
    private RadioButton type2;
    @FXML
    private TableView<BookList> BookInfo;
    @FXML
    private TableColumn<Booking, Integer> ViewID;
    @FXML
    private TableColumn<Booking, ?> viewEmp;
    @FXML
    private TableColumn<Booking, ?> viewCost;

    @FXML
    private TableView<VehList> InfoVeh;
    @FXML
    private TableColumn<VehList, String> viewType;
    @FXML
    private TableColumn<VehList, String> viewMake;
    @FXML
    private TableColumn<VehList, String> viewModel;
    @FXML
    private TableColumn<VehList, String> viewFuel;
    @FXML
    private RadioButton Filter1;
    @FXML
    private RadioButton Filter2;
    @FXML
    private Label status;
    @FXML
    private TableColumn<?, ?> VviewDate;
    @FXML
    private TableColumn<?, ?> viewReg1;
    @FXML
    private ToggleGroup SearchType;
    @FXML
    private ToggleGroup type;
    @FXML
    private TableView<PartList> PartInfo;
    @FXML
    private TableColumn<?, ?> PartName;
    @FXML
    private TableColumn<?, ?> PartDesc;
    @FXML
    private TableColumn<?, ?> PartDate;
    @FXML
    private TableView<BillList> BillInfo;
    @FXML
    private TableColumn<?, ?> BillID;
    @FXML
    private TableColumn<?, ?> bIllCost;
    @FXML
    private TableColumn<?, ?> BillSettled;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        fill();
    }

    public void updatePart(int id) throws SQLException {
        PartName.setCellValueFactory(new PropertyValueFactory<>("name"));
        PartDesc.setCellValueFactory(new PropertyValueFactory<>("desc"));
        PartDate.setCellValueFactory(new PropertyValueFactory<>("install"));
        DataFunc dd = null;
        ArrayList<PartList> a = new ArrayList<>();
        dd = new DataFunc();
        a = dd.getPart(id);
        for (PartList b : a) {
            data3.add(b);
        }
        PartInfo.setItems(data3);
    }

    public void updateBill(int id) throws SQLException {
        BillID.setCellValueFactory(new PropertyValueFactory<>("ID"));
        bIllCost.setCellValueFactory(new PropertyValueFactory<>("cost"));
        //BillPaid.setCellValueFactory(new PropertyValueFactory<>("paid"));
        BillSettled.setCellValueFactory(new PropertyValueFactory<>("settled"));
        DataFunc tt = null;
        ArrayList<BillList> a = new ArrayList<>();
        tt = new DataFunc();
        a = tt.getBill(id);
        for (BillList b : a) {
            data4.add(b);

            //System.out.println(b.getDate());
        }
        BillInfo.setItems(data4);
    }

    public void updateBook(String Reg) throws SQLException {
        //BookInfo.getItems().clear();
        //System.out.println(Reg);
        ViewID.setCellValueFactory(new PropertyValueFactory<>("bookID"));
        viewEmp.setCellValueFactory(new PropertyValueFactory<>("empID"));
        viewCost.setCellValueFactory(new PropertyValueFactory<>("cost"));
        VviewDate.setCellValueFactory(new PropertyValueFactory<>("date"));

        DataFunc tt = null;
        ArrayList<BookList> a = new ArrayList<>();
        tt = new DataFunc();
        a = tt.getAllBook(Reg);
        for (BookList b : a) {
            data2.add(b);
            updatePart(b.getBookID());
            updateBill(b.getBookID());
            //System.out.println(b.getDate());
        }
        BookInfo.setItems(data2);
    }

    public void updateVeh(Customer cc) throws SQLException {

        InfoVeh.getItems().clear();
        //System.out.println(cc.getCustomerID());
        viewType.setCellValueFactory(new PropertyValueFactory<>("fuel"));
        viewMake.setCellValueFactory(new PropertyValueFactory<>("make"));
        viewModel.setCellValueFactory(new PropertyValueFactory<>("model"));
        viewFuel.setCellValueFactory(new PropertyValueFactory<>("fuel"));
        viewReg1.setCellValueFactory(new PropertyValueFactory<>("Reg"));
        DataFunc tt = null;
        ArrayList<VehList> a = new ArrayList<>();
        tt = new DataFunc();
        a = tt.getVeh(cc.getCustomerID());
        for (VehList b : a) {
            updateBook(b.getReg());
            data1.add(b);

            //System.out.println(b.getReg());
        }
        InfoVeh.setItems(data1);
        InfoVeh.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                VehList v = InfoVeh.getSelectionModel().getSelectedItem();
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Open Vehicle Record");
            alert.setHeaderText(null);
            alert.setContentText("Are you sure you want to open the Vehicle Record");

            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK) {
                //System.out.println("Clicked");
                try{
                    Store.setV(getVeh(v));
                    //System.out.println("Clicked");
                    Pane newLoadedPane = FXMLLoader.load(getClass().getResource("/vehicles/GUI/SearchVehicle_1.fxml"));
                        //Content.getChildren().add(newLoadedPane);
                    FXMLDocumentController.changeContentPane(newLoadedPane);
                }
                catch(Exception e){
                    e.printStackTrace();
                }
                
            }
            
        }
                
        });
    }
    public vehicle getVeh(VehList v) throws SQLException
    {
        vehicle vh=(new DataFunc().getVehicle(v.getReg()));
        return vh;
    }
    @FXML
    private void AddCust(ActionEvent event) throws SQLException, IOException {
        Pane newLoadedPane = FXMLLoader.load(getClass().getResource("AdvancedAdd.fxml"));
        //Content.getChildren().add(newLoadedPane);
        FXMLDocumentController.changeContentPane(newLoadedPane);
    }

    @FXML
    private void SaveCust(ActionEvent event) throws SQLException {
        if (txtEmail.getText().contains("@")) {

            if (txtPhone.getText().length() == 10 || txtPhone.getText().length() == 11) {
                String type;
                if (type2.isSelected()) {
                    type = "Business";
                } else {
                    type = "Individual";
                }
                try {
                    Customer ne = new Customer(ct.getCustomerID(), txtName.getText(), txtAddr1.getText(), txtAddr2.getText(), txtPost.getText(), txtPhone.getText(), txtEmail.getText(), type);
                    DataFunc dd = new DataFunc();
                    dd.UpdateCust(ne);
                    fill();
                } catch (NullPointerException e) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("No Customer");
                    alert.setHeaderText(null);
                    alert.setContentText("No Customer Selected. \n Please select a customer and then Save changes.");
                    alert.showAndWait();
                }
            } else {
                status.setVisible(true);
                status.setText("Enter a valid Phone number");
            }
        } else {
            status.setVisible(true);
            status.setText("Enter a valid Email ID");
        }
    }

    private void fill() {
        clear();
        InfoTable.getItems().clear();
        TableID.setCellValueFactory(new PropertyValueFactory<Customer, Integer>("CustomerID"));
        TableName.setCellValueFactory(new PropertyValueFactory<Customer, String>("Name"));
        TableEmail.setCellValueFactory(new PropertyValueFactory<Customer, String>("Email"));
        TablePhone.setCellValueFactory(new PropertyValueFactory<Customer, String>("PhoneNo"));
        DataFunc dd = null;
        ArrayList<Customer> a = null;
        try {
            dd = new DataFunc();
            a = dd.GetAllCust();
        } catch (SQLException ex) {
            //Logger.getLogger(CustomerMainController.class.getName()).log(Level.SEVERE, null, ex);
        }

        for (Customer b : a) {
            data.add(b);
            //IDList.add(b.getId());

            //System.out.println (b.getId()+"   "+ b.getName()+"   "+b.getPhone());
        }
        InfoTable.setItems(data);
        InfoTable.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                clear();
                Customer cc = InfoTable.getSelectionModel().getSelectedItem();
                ct = cc;
                try {
                    //System.out.println(cc.getCustomerID());
                    BookInfo.getItems().clear();
                    updateVeh(cc);
                } catch (SQLException ex) {
                    Logger.getLogger(CustomerGUIController.class.getName()).log(Level.SEVERE, null, ex);
                }
                //System.out.println(av.getId()+"\n"+av.getName()+"\n"+av.getPhone());
                DataFunc dd;
                try {
                    dd = new DataFunc();
                    //Customer cc=dd.GetByID(av.getCustomerID());
                    //Store.setC(cc);
                    txtName.setText(cc.getName());
                    txtAddr1.setText(cc.getAddressL1());
                    txtAddr2.setText(cc.getAddressL2());
                    txtPost.setText(cc.getPostCode());
                    txtEmail.setText(cc.getEmail());
                    txtPhone.setText(cc.getPhoneNo());
                    if (cc.getType().equals("Business")) {
                        type2.setSelected(true);
                    } else {
                        type1.setSelected(true);
                    }
                } catch (SQLException ex) {
                    Logger.getLogger(CustomerGUIController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }

    @FXML
    private void DelCust(ActionEvent event) throws SQLException {
        if (ct != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Delete Customer");
            alert.setHeaderText(null);
            alert.setContentText("Are you sure you want to delete this Account?");

            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK) {
                DataFunc dd = new DataFunc();
                dd.DelCust(ct.getCustomerID());
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Delete Customer");
            alert.setHeaderText(null);
            alert.setContentText("Please select a customer account to delete.");
        }
        fill();
    }

    @FXML
    private void showBusiness(ActionEvent event) {
        clear();
        InfoTable.getItems().clear();
        TableID.setCellValueFactory(new PropertyValueFactory<Customer, Integer>("CustomerID"));
        TableName.setCellValueFactory(new PropertyValueFactory<Customer, String>("Name"));
        TableEmail.setCellValueFactory(new PropertyValueFactory<Customer, String>("Email"));
        TablePhone.setCellValueFactory(new PropertyValueFactory<Customer, String>("PhoneNo"));
        DataFunc dd = null;
        ArrayList<Customer> a = null;
        try {
            dd = new DataFunc();
            a = dd.GetAllBus();
        } catch (SQLException ex) {
            //Logger.getLogger(CustomerMainController.class.getName()).log(Level.SEVERE, null, ex);
        }

        for (Customer b : a) {
            data.add(b);
            //IDList.add(b.getId());

            //System.out.println (b.getId()+"   "+ b.getName()+"   "+b.getPhone());
        }
        InfoTable.setItems(data);
        InfoTable.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                Customer cc = InfoTable.getSelectionModel().getSelectedItem();
                ct = cc;
                try {
                    //System.out.println(cc.getCustomerID());
                    updateVeh(cc);
                } catch (SQLException ex) {
                    Logger.getLogger(CustomerGUIController.class.getName()).log(Level.SEVERE, null, ex);
                }
                //System.out.println(av.getId()+"\n"+av.getName()+"\n"+av.getPhone());
                DataFunc dd;
                try {
                    dd = new DataFunc();
                    //Customer cc=dd.GetByID(av.getCustomerID());
                    //Store.setC(cc);
                    txtName.setText(cc.getName());
                    txtAddr1.setText(cc.getAddressL1());
                    txtAddr2.setText(cc.getAddressL2());
                    txtPost.setText(cc.getPostCode());
                    txtEmail.setText(cc.getEmail());
                    txtPhone.setText(cc.getPhoneNo());
                    if (cc.getType().equals("Business")) {
                        type2.setSelected(true);
                    } else {
                        type1.setSelected(true);
                    }

                } catch (SQLException ex) {
                    Logger.getLogger(CustomerGUIController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }

    private void clear() {
        ct = null;
        status.setVisible(false);
        type1.setSelected(false);
        type2.setSelected(false);
        txtName.setText("");
        txtAddr1.setText("");
        txtAddr2.setText("");
        txtPost.setText("");
        txtEmail.setText("");
        txtPhone.setText("");
        type1.setSelected(false);
        type2.setSelected(false);
        InfoVeh.getItems().clear();
        BookInfo.getItems().clear();
        PartInfo.getItems().clear();
        BillInfo.getItems().clear();
    }

    @FXML
    private void showIndiv(ActionEvent event) {
        clear();
        InfoTable.getItems().clear();
        TableID.setCellValueFactory(new PropertyValueFactory<Customer, Integer>("CustomerID"));
        TableName.setCellValueFactory(new PropertyValueFactory<Customer, String>("Name"));
        TableEmail.setCellValueFactory(new PropertyValueFactory<Customer, String>("Email"));
        TablePhone.setCellValueFactory(new PropertyValueFactory<Customer, String>("PhoneNo"));
        DataFunc dd = null;
        ArrayList<Customer> a = null;
        try {
            dd = new DataFunc();
            a = dd.GetAllIndiv();
        } catch (SQLException ex) {
            //Logger.getLogger(CustomerMainController.class.getName()).log(Level.SEVERE, null, ex);
        }

        for (Customer b : a) {
            data.add(b);
            //IDList.add(b.getId());

            //System.out.println (b.getId()+"   "+ b.getName()+"   "+b.getPhone());
        }
        InfoTable.setItems(data);
        InfoTable.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                Customer cc = InfoTable.getSelectionModel().getSelectedItem();
                ct = cc;
                try {
                    //System.out.println(cc.getCustomerID());
                    updateVeh(cc);
                } catch (SQLException ex) {
                    Logger.getLogger(CustomerGUIController.class.getName()).log(Level.SEVERE, null, ex);
                }
                //System.out.println(av.getId()+"\n"+av.getName()+"\n"+av.getPhone());
                DataFunc dd;
                try {
                    dd = new DataFunc();
                    //Customer cc=dd.GetByID(av.getCustomerID());
                    //Store.setC(cc);
                    txtName.setText(cc.getName());
                    txtAddr1.setText(cc.getAddressL1());
                    txtAddr2.setText(cc.getAddressL2());
                    txtPost.setText(cc.getPostCode());
                    txtEmail.setText(cc.getEmail());
                    txtPhone.setText(cc.getPhoneNo());
                    if (cc.getType().equals("Business")) {
                        type2.setSelected(true);
                    } else {
                        type1.setSelected(true);
                    }

                } catch (SQLException ex) {
                    Logger.getLogger(CustomerGUIController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }

}
