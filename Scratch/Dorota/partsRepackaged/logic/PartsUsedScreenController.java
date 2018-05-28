/**
 * FXML PartsScreenController class
 * Editable table cells based on:
 * http://stackoverflow.com/questions/18374626/javafx-2-tableview-return-selected-item
 * 
 */
package parts.logic;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TablePosition;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.util.Callback;



public class PartsUsedScreenController implements Initializable {

    @FXML private TextField partID;
    @FXML private TextField partName;
    @FXML private TextArea partDescription;
    @FXML private DatePicker partDateInStock;
    @FXML private DatePicker partDateInstalled;
    @FXML private TextField partRegisteredTo;
    @FXML private TextField partCost;
    @FXML private TextField partBookingID;
    
    @FXML private TableView<Part> partsTable;
    @FXML private TableColumn<Part, String> partNameCol; 
    @FXML private TableColumn<Part, Double> partCostCol;
    @FXML private TableColumn<Part, String> partDateInStockCol;
    @FXML private TableColumn<Part, String> partDateInstalledCol;
    
    @FXML private Button clearBtn;   
    @FXML private Button saveChangesBtn;
    @FXML private Button deleteBtn;
    @FXML private Button addPartBtn;   
 
  
    
    private ObservableList<Part> parts = FXCollections.observableArrayList();  
    private ResultSet rs;
   
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        populateTable();
        defineEventControls();
        setEditableCells();
    }   
   
    private void populateTable() {     
            parts = DBQueries2.fetchAllParts();
            if (parts.isEmpty()) { 
                System.out.println("parts empty!");
            }
                    
            partsTable.setItems(parts);
            partsTable.setEditable(true);
            
        //    partNameCol.setCellFactory((TableColumn<Part, String> p) -> new CheckBoxTableCell<Part, String>());
    
            partNameCol.setCellValueFactory(new PropertyValueFactory<>("partName"));
            partCostCol.setCellValueFactory(new PropertyValueFactory<>("partCost"));
            partDateInStockCol.setCellValueFactory(new PropertyValueFactory<>("dateInStock"));
            partDateInstalledCol.setCellValueFactory(new PropertyValueFactory<>("dateInstalled"));
        
    }
    
  
   
    private void defineEventControls() {
    
        // ********** TEXTFIELDS: fetch selected row *********** //
    //  partID.setEditable(false);
        partsTable.getSelectionModel().setCellSelectionEnabled(true); 
        partsTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
       
        partsTable.setOnMouseClicked(new EventHandler<MouseEvent>() {
            
            @Override
            public void handle(MouseEvent click) {
                if (click.getClickCount() == 1) {
                    
                    ObservableList<TablePosition> selectedCells = partsTable.getSelectionModel().getSelectedCells();
                        for (TablePosition pos : selectedCells) {
                            int row = pos.getRow();
                            partsTable.getSelectionModel().select(row);
                            Part selectedPart = partsTable.getItems().get(row);
                 
                            partID.setText(Integer.toString(selectedPart.getPartID()));
                            partName.setText(selectedPart.getPartName());
                            partDescription.setText(selectedPart.getPartDescr());
    //                        partDateInStock.setValue(selectedPart.getDateInStock());
                            partDateInstalled.setValue(selectedPart.getDateInstalled());
                            partCost.setText(Double.toString(selectedPart.getPartCost()));
                            partBookingID.setText(Integer.toString(selectedPart.getPartBookingID()));
                        }
                    }
                }
        });

        
    }
    
    
    public void setEditableCells() {
    // ********** TABLE CONTROLS: selectable cells ************ //

             Callback<TableColumn<Part, String>, TableCell<Part, String>> cellFactoryS =
                new Callback<TableColumn<Part,String>, TableCell<Part, String>>() {
                    public TableCell call(TableColumn p) {
                        return new EditingCellStr();
                    }
                }; 
             
             Callback<TableColumn<Part, Double>, TableCell<Part, Double>> cellFactoryD =
                new Callback<TableColumn<Part, Double>, TableCell<Part, Double>>() {
                    public TableCell call(TableColumn p) {
                        return new EditingCellDbl();
                    }
                };  
                    
            partNameCol.setCellFactory(cellFactoryS);
            partCostCol.setCellFactory(cellFactoryD);
 
    }
   
    
    @FXML
    public void addNewPart() throws IOException {
        
        Pane newContent = FXMLLoader.load(getClass().getResource("addPartScreen.fxml"));
        common.FXMLDocumentController.changeContentPane(newContent);
        
    }
   
    
    @FXML
    public void saveChanges(ActionEvent e) 
                            throws IOException, SQLException {
        
            String idStr, name, descr, registered;             
            int ID;
            Double cost;
            LocalDate inStock, installed;
            Part part;
             
                ID = Integer.parseInt(partID.getText());
                name = partName.getText();
                descr = partDescription.getText();
                cost = Double.parseDouble(partCost.getText());
                inStock = partDateInStock.getValue();
                installed = partDateInstalled.getValue();
                registered = partRegisteredTo.getText();
                
                int bookingID = Integer.parseInt(partBookingID.getText());
                part = new Part(ID, name, descr, cost, inStock);
                if (installed != null) { part.setDateInstalled(installed); }
                if (!registered.equals("")) { part.setRegisteredTo(registered); }
                
                DBQueries2.updateSTOCKPart(part);
                clearTextFields();
     
                parts.removeAll(parts);
                populateTable();
                defineEventControls();
   
    }
    
    @FXML
    private void clearTextFields() {
        
        partID.setText("");
        partName.setText("");
        partDescription.setText("");
        partDateInStock.setValue(null);
        partDateInstalled.setValue(null);
        partRegisteredTo.setText("");
        partCost.setText("");
        partBookingID.setText("");
    }
    
    @FXML
    private void deleteEntry() {
        TablePosition pos = partsTable.getSelectionModel().getSelectedCells().get(0);                 
        int row = pos.getRow();
        Part selectedPart = partsTable.getItems().get(row);
        DBQueries2.removePart(selectedPart.getPartID());
        
        clearTextFields();
        parts.removeAll(parts);
        populateTable();
        defineEventControls();
    }
} 