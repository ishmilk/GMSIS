/**
 * FXML AddPartsScreenController class
 * 
 */

package parts.logic;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TablePosition;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;



public class ManageStockScreenController implements Initializable {

    
    @FXML private TableView<DistinctPart> distinctPartsTable;
    @FXML private TableColumn<DistinctPart, String> partNameCol;
    @FXML private TableColumn<DistinctPart, Integer> quantityCol;
   
    @FXML private TableView<Part> selectedPartTable;
    @FXML private TableColumn<Part, String> partIDCol;
    @FXML private TableColumn<Part, String> partNameCol2;
    @FXML private TableColumn<Part, String> partDescrCol;  
    @FXML private TableColumn<Part, String> dateInStockCol;
    @FXML private TableColumn<Part, Double> partCostCol;
    
    
    public static final ArrayList<String> DISTINCT_NAMES = new ArrayList(Arrays.asList(
        "Spark Plug",
        "Oil Filter",
        "Battery",
        "Tire",
        "Alternator",
        "Fuel Pump",
        "Clutch",
        "Water Pump",
        "Headlight",
        "Windshield Wiper Blades"));
  
//    @FXML private ComboBox<String> distinctPartsCmb;
    @FXML
    private TextField partsNameTxt;
 
    @FXML private TextField partCostTxt;
    @FXML private TextArea partDescriptionTxt;
    @FXML private DatePicker partDateInStockPckr;
    
    @FXML private Button addStockBtn;
    @FXML private Button saveChangesBtn;
    @FXML private Button deleteBtn;
    @FXML private Button clearBtn;
    @FXML private Button showUsedPartsBtn;
    
    private ArrayList<DistinctPart> distinctParts; 
    private ArrayList<Part> partsByName; 
    
    private ObservableList<TablePosition> selectedCells;
    private ResultSet rs;
    
 //   private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
    private DateTimeFormatter formatterDPkr = DateTimeFormatter.ofPattern("dd/MM/yyyy");
   
    @Override
    public void initialize(URL url, ResourceBundle rb) {
       saveChangesBtn.setVisible(false);
       partsNameTxt.setEditable(false);
       partDescriptionTxt.setEditable(false);
       partCostTxt.setEditable(false);
       partDateInStockPckr.setEditable(false);
        try {
            populateTable1();
        } catch (SQLException ex) {
            
        }
        defineEventControls();
    }   
    

     private void populateTable1() throws SQLException {     
            
            distinctParts = DBQueries2.fetchAllDistinctParts(DISTINCT_NAMES);
            
            if (distinctParts.isEmpty()) { 
                System.out.println("parts empty!");
            }
                    
            distinctPartsTable.setItems(FXCollections.observableArrayList(distinctParts));      
            distinctPartsTable.setEditable(false);
            
            partNameCol.setCellValueFactory(new PropertyValueFactory<>("partName"));
            quantityCol.setCellValueFactory(new PropertyValueFactory<>("quantity"));
          
        
    }
     
    private void populateTable2() throws SQLException {    
           
            selectedCells = distinctPartsTable.getSelectionModel().getSelectedCells();
                
            if (selectedCells != null && !selectedCells.isEmpty()) {
                TablePosition pos = selectedCells.get(0);
            
                int row = pos.getRow();
                distinctPartsTable.getSelectionModel().select(row); // select whole row on click

                DistinctPart selectedPart = distinctPartsTable.getItems().get(row);
                try {
                    partsByName = DBQueries2.fetchParts(selectedPart.getPartName());
                } 
                catch (SQLException ex) {

                }
            
                selectedPartTable.setItems(FXCollections.observableArrayList(partsByName));
                partIDCol.setCellValueFactory(new PropertyValueFactory<>("partID"));
                partNameCol2.setCellValueFactory(new PropertyValueFactory<>("partName"));
                partDescrCol.setCellValueFactory(new PropertyValueFactory<>("partDescr"));
                partCostCol.setCellValueFactory(new PropertyValueFactory<>("partCost"));
                dateInStockCol.setCellValueFactory(new PropertyValueFactory<>("dateInStock"));
            
            }
    }
     
      private void defineEventControls() {
        
        distinctPartsTable.getSelectionModel().setCellSelectionEnabled(true); 
        distinctPartsTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        
       
        distinctPartsTable.setOnMouseClicked(new EventHandler<MouseEvent>() {
            
            @Override
            public void handle(MouseEvent click) {
                if (click.getClickCount() == 1) {
                    
                    try {
                        populateTable2();
                    } catch (SQLException ex) {
                        
                    }
                }
            }          
        });
        
        selectedPartTable.setOnMouseClicked(new EventHandler<MouseEvent>() {
            
            @Override
            public void handle(MouseEvent click) {
                if (click.getClickCount() == 1) {
                    saveChangesBtn.setVisible(true);
                    partsNameTxt.setEditable(true);
                    partDescriptionTxt.setEditable(true);
                    partCostTxt.setEditable(true);
                    partDateInStockPckr.setEditable(true);
                    selectedCells = selectedPartTable.getSelectionModel().getSelectedCells();
                    if (selectedCells != null && !selectedCells.isEmpty()) {
                        TablePosition pos = selectedCells.get(0);

                        int row = pos.getRow();
                        selectedPartTable.getSelectionModel().select(row); // select whole row on click

                        Part selectedPart = selectedPartTable.getItems().get(row);

                        partsNameTxt.setText(selectedPart.getPartName());
                        partDescriptionTxt.setText(selectedPart.getPartDescr());
                        partCostTxt.setText(Double.toString(selectedPart.getPartCost()));
                        partDateInStockPckr.setValue(selectedPart.getDateInStock());
                    }
                }
            }
                
        });
          
        partsNameTxt.textProperty().addListener((observable, oldValue, newValue) -> {
            saveChangesBtn.setVisible(true);
        });

        
        partDescriptionTxt.textProperty().addListener((observable, oldValue, newValue) -> {
            saveChangesBtn.setVisible(true);
        });
        
        partCostTxt.textProperty().addListener((observable, oldValue, newValue) -> {
            saveChangesBtn.setVisible(true);
        });
        
        partDateInStockPckr.valueProperty().addListener((observable, oldValue, newValue) -> {
            saveChangesBtn.setVisible(true);
        });
    }

    
    @FXML
    void saveChanges(ActionEvent event) throws SQLException {
            
            String name, descr;             
            int partID, row;
            Double cost;
            LocalDate inStock;
            Part part;
            
            saveChangesBtn.setVisible(false);
           
             if (selectedCells != null && partsNameTxt.getText() != null) {
    
                for (TablePosition pos : selectedCells) {
             
                    row = pos.getRow();
                    part = selectedPartTable.getItems().get(row);
                    partID = part.getPartID();
                    name = partsNameTxt.getText();

                    if (partDescriptionTxt.getText() != null && !partDescriptionTxt.getText().isEmpty()) {
                        descr = partDescriptionTxt.getText();
                    }
                    else {
                        Alert alert = new Alert(Alert.AlertType.WARNING);
                        alert.setContentText("Please enter the description!");
                        alert.show();
                        return;
                    }
                    if (partCostTxt.getText() != null && !partCostTxt.getText().trim().isEmpty()) {
                        cost = Double.parseDouble(partCostTxt.getText());
                    }
                    else {
                        Alert alert = new Alert(Alert.AlertType.WARNING);
                        alert.setContentText("Please enter part cost!");
                        alert.show();
                        return;
                    }
                    if (partDateInStockPckr.getValue() != null) {
                        inStock = partDateInStockPckr.getValue();
                    }
                    else {
                        Alert alert = new Alert(Alert.AlertType.WARNING);
                        alert.setContentText("Please enter date in stock!");
                        alert.show();
                        return;
                    }

                    part = new Part(partID, name, descr, cost, inStock);
                    DBQueries2.updateSTOCKPart(part);
              
                    }
                    
                }
                else {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setContentText("Please select part to apply changes!");
                    alert.show();
                    return;
                }
  
                clearTextFields(); 
                selectedPartTable.refresh();
                populateTable2();
                defineEventControls();
    }

    @FXML
    void deletePart(ActionEvent event) throws SQLException {
        TablePosition pos = selectedPartTable.getSelectionModel().getSelectedCells().get(0);                 
        int row = pos.getRow();
        Part selectedPart = selectedPartTable.getItems().get(row);
        DBQueries2.removePart(selectedPart.getPartID());
        
        clearTextFields();
        populateTable2();
        defineEventControls();
    }

    @FXML
    public void addNewPart() throws IOException, SQLException {
        if (partsNameTxt.getText().isEmpty()) {
            Pane newContent = FXMLLoader.load(getClass().getResource("/GUI/addNewPartScreen.fxml"));
            common.FXMLDocumentController.changeContentPane(newContent);
            return;
        }
        
        String name = partsNameTxt.getText();
        String descr;             
        int partID;
        Double cost;
        LocalDate inStock;
        Part part;
        
        partID = DBQueries2.assignID();
                
       
        if (partDescriptionTxt.getText() != null && !partDescriptionTxt.getText().trim().isEmpty()) {
            descr = partDescriptionTxt.getText();
        }
        else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setContentText("Please enter the description!");
            alert.show();
            return;
        }

        if (partCostTxt.getText() != null && !partCostTxt.getText().trim().isEmpty()) {
            cost = Double.parseDouble(partCostTxt.getText());
        }
        else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setContentText("Please enter part cost!");
            alert.show();
            return;
        }

        if (partDateInStockPckr.getValue() != null) {
            inStock = partDateInStockPckr.getValue();
        }
        else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setContentText("Please enter date in stock!");
            alert.show();
            return;
        }
        part = new Part(partID, name, descr, cost, inStock);   
        DBQueries2.addPart(part);

        clearTextFields();
        populateTable1();
        partsByName.clear();
        selectedPartTable.setItems(FXCollections.observableArrayList(partsByName));
        selectedPartTable.refresh();
    
    }
    
    @FXML
    private void clearTextFields() {

        partsNameTxt.setText("");
        partDescriptionTxt.setText("");
        partCostTxt.setText("");
        partDateInStockPckr.setValue(null);
        saveChangesBtn.setVisible(false);

    }
     
    @FXML
    public void showUsedParts() throws IOException {
        Pane newContent = FXMLLoader.load(getClass().getResource("GUI/usedPartsScreen2.fxml"));
        common.FXMLDocumentController.changeContentPane(newContent);
        
    }
   
    
} 
