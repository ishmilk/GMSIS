/**
 * FXML AddPartsScreenController class
 * 
 */

package parts.logic;

import diagnosis_repairs.Booking;
import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TablePosition;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.collections.transformation.SortedList;



public class AddPartToBookingController2 implements Initializable {
    
    @FXML private TextField bookingInfoTxt;   
    @FXML private TextField regTxt;  
    @FXML private TextField bookingDateTxt;
    
    @FXML private Label bookingInfoLbl;
    @FXML private Label vehicleRegLbl;
    @FXML private Label bookingDateLbl;
    
    private Booking booking;
    
    @FXML private TableView<DistinctPart> distinctPartsTable;
    @FXML private TableColumn<DistinctPart, String> partNameCol;
    @FXML private TableColumn<DistinctPart, Integer> quantityCol;
   
    @FXML private TableView<Part> selectedPartTable;
    @FXML private TableColumn<Part, String> partIDCol;
    @FXML private TableColumn<Part, String> partNameCol2;
    @FXML private TableColumn<Part, String> partDescrCol;  
    @FXML private TableColumn<Part, String> dateInStockCol;
    @FXML private TableColumn<Part, Double> partCostCol;
    
    
    @FXML private TableView<Part> partsThisBookingTable;
    @FXML private TableColumn<Part, String> partNameCol3;
    @FXML private TableColumn<Part, String> partDescrCol2;

    @FXML private Button savePartsToBookingBtn;
    @FXML private Button removePartBtn;
    @FXML private Button addPartBtn;
    
    private final ArrayList<String> DISTINCT_NAMES = new ArrayList(Arrays.asList("Spark Plug",
        "Oil Filter",
        "Battery",
        "Tire",
        "Alternator",
        "Fuel Pump",
        "Clutch",
        "Water Pump",
        "Headlight",
        "Windshield Wiper Blades"));
    private Hashtable<String, ArrayList<Part>> selected = new Hashtable<>();

    private ArrayList<DistinctPart> distinctParts; 
    private ArrayList<Part> partsByName = new ArrayList<>(); 
    private static ArrayList<Part> partsThisBookingList = new ArrayList<>();
    
    private ArrayList<Part> temp = new ArrayList<>();
    private ArrayList<DistinctPart> tempD = new ArrayList<>();
    
    
    private Part selectedPart1;
    private Part selectedPart2;
    
    private ObservableList<TablePosition> selectedCells;
    private ResultSet rs;
    

    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
   
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        booking = (Booking) diagnosis_repairs.CodeBank.getStoredObject();
        
        bookingInfoLbl.setText(Integer.toString(booking.getID()));
        vehicleRegLbl.setText(booking.getVehicleRegistration());
        bookingDateLbl.setText(booking.getDate().format(formatter));
        fillSelected();
        try {
            populateTable1();
        } catch (SQLException ex) {
            
        }
        defineEventControls();
    }   
    

     private void populateTable1() throws SQLException {     
            
            distinctParts = DBQueries2.fetchAllDistinctParts(DISTINCT_NAMES);
         //   filterOutOfStock(distinctParts);
            if (distinctParts.isEmpty()) { 
                System.out.println("parts empty!");
            }
                    
            distinctPartsTable.setItems(FXCollections.observableArrayList(distinctParts));  
            selectedPartTable.setItems(FXCollections.observableArrayList(partsByName));
            partsThisBookingTable.setItems(FXCollections.observableArrayList(partsThisBookingList));
            
            distinctPartsTable.setEditable(false);
            selectedPartTable.setEditable(false);
            partsThisBookingTable.setEditable(false);
            
            
            partNameCol.setCellValueFactory(new PropertyValueFactory<>("partName"));
            quantityCol.setCellValueFactory(new PropertyValueFactory<>("quantity"));
            
            partNameCol3.setCellValueFactory(new PropertyValueFactory<>("partName"));
            partDescrCol2.setCellValueFactory(new PropertyValueFactory<>("partDescr"));
            
          
        
    }
  
     
    private void populateTable2() throws SQLException {     
            selectedCells = distinctPartsTable.getSelectionModel().getSelectedCells();
            TablePosition pos = selectedCells.get(0);

            int row = pos.getRow();
            distinctPartsTable.getSelectionModel().select(row); // select whole row on click

            DistinctPart selectedPart = distinctPartsTable.getItems().get(row);
            if (selected.get(selectedPart.getPartName()).isEmpty()) {
                try {
                    partsByName = DBQueries2.fetchParts(selectedPart.getPartName());
                } 
                catch (SQLException ex) {}
                selected.put(selectedPart.getPartName(), partsByName);
            }
            else {
                partsByName = selected.get(selectedPart.getPartName());
            }
            
            
            selectedPartTable.setItems(FXCollections.observableArrayList(partsByName));
            partIDCol.setCellValueFactory(new PropertyValueFactory<>("partID"));
            partNameCol2.setCellValueFactory(new PropertyValueFactory<>("partName"));
            partDescrCol.setCellValueFactory(new PropertyValueFactory<>("partDescr"));
            partCostCol.setCellValueFactory(new PropertyValueFactory<>("partCost"));
            dateInStockCol.setCellValueFactory(new PropertyValueFactory<>("dateInStock"));
 
    }
     
    private void defineEventControls() {
        
        distinctPartsTable.getSelectionModel().setCellSelectionEnabled(true); 
        distinctPartsTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        
       
        distinctPartsTable.setOnMouseClicked(new EventHandler<MouseEvent>() {
            
            @Override
            public void handle(MouseEvent click) {
                if (click.getClickCount() == 1) {
                    
                    selectedPart1 = null;
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
                    ObservableList<TablePosition> selectedCells = selectedPartTable.getSelectionModel().getSelectedCells();;
                   if (!selectedCells.isEmpty()) {
                        TablePosition pos = selectedCells.get(0);
                        int row = pos.getRow();
                        selectedPartTable.getSelectionModel().select(row); // select whole row on click
                        selectedPart1 = selectedPartTable.getItems().get(row);
                   }
                }
            }
            
                
        });
        
        partsThisBookingTable.setOnMouseClicked(new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent click) {
                if (click.getClickCount() == 1) {
                    ObservableList<TablePosition> selectedCells = partsThisBookingTable.getSelectionModel().getSelectedCells();;
                   if (!selectedCells.isEmpty()) {
                        TablePosition pos = selectedCells.get(0);
                        int row = pos.getRow();
                        partsThisBookingTable.getSelectionModel().select(row); // select whole row on click
                        selectedPart2 = partsThisBookingTable.getItems().get(row);
                   }
                }
            }
        });
    }    
    
    @FXML
    private void addSelectedPart() throws SQLException {
        if (selectedPart1 != null) {
            
            partsThisBookingList.add(selectedPart1);
            partsThisBookingTable.setItems(FXCollections.observableArrayList(partsThisBookingList)); 
            partsThisBookingTable.refresh();
           
            partsByName.remove(selectedPart1);
            selectedPartTable.setItems(FXCollections.observableArrayList(partsByName));
            selectedPartTable.refresh();
            
            distinctParts = removeStock(distinctParts, selectedPart1);
            distinctPartsTable.setItems(FXCollections.observableArrayList(distinctParts));
            distinctPartsTable.refresh();
            
            selectedPart1 = null;
        }
        else {
            // TODO: alert user
            
        }
    }

    @FXML
    private void removeSelectedPart() {
        if ( selectedPart2 != null) {
            
            partsThisBookingList.remove(selectedPart2);
            partsThisBookingTable.setItems(FXCollections.observableArrayList(partsThisBookingList)); 
            partsThisBookingTable.refresh();
            
            partsByName.add(selectedPart2);
            selectedPartTable.setItems(FXCollections.observableArrayList(partsByName));
            selectedPartTable.refresh();

            distinctParts = addStock(distinctParts, selectedPart2);
            distinctPartsTable.setItems(FXCollections.observableArrayList(distinctParts));
            distinctPartsTable.refresh();
            
            selectedPart2 = null;
        }
        else {
            
            //TODO: alert user
        }
    }

    @FXML
    private void savePartsToBooking() throws IOException {
        int bookingID = booking.getID();
        LocalDate bookingDate = booking.getDate();
        DBQueries2.linkPartToBooking(partsThisBookingList, bookingID, bookingDate);
        Pane newContent = FXMLLoader.load(getClass().getResource("/diagnosis_repairs/CompleteBooking.fxml"));
        common.FXMLDocumentController.changeContentPane(newContent);
    }
    
    
    // *********  HELPER METHODS ************ //
        private void fillSelected() {
            for (String name : DISTINCT_NAMES) {
                selected.put(name, new ArrayList<Part>());
            }
        }
    
        private static ArrayList<DistinctPart> removeStock(ArrayList<DistinctPart> list, Part part) {
            ArrayList<DistinctPart> newDP = new ArrayList<>();
            for (DistinctPart dp : list) {                
                if (!dp.getPartName().equals(part.getPartName())) {
                    newDP.add(dp);
                }
                else {
                    if (dp.getQuantity() > 1) {
                        newDP.add(dp);
                        dp.removeOne();
                    }
                }
            }
            return newDP;
        }
        
        private ArrayList<DistinctPart> addStock(ArrayList<DistinctPart> list, Part part) {
            ArrayList<DistinctPart> newDP = new ArrayList<>();
            boolean exists = false;
            for (DistinctPart dp : list) {
                newDP.add(dp); 
                if (dp.getPartName().equals(part.getPartName())) {
                    dp.addOne();
                    exists = true;
                }              
            }
            if (!exists) {newDP.add(new DistinctPart(part.getPartName(), 1));}
            return newDP;
        }
        
      
    
} 

