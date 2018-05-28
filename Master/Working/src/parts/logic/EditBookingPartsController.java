/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package parts.logic;

import diagnosis_repairs.logic.Booking;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.format.DateTimeFormatter;
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
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TablePosition;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;

/**
 * FXML Controller class
 *
 * @author saphirepencil
 */
public class EditBookingPartsController implements Initializable {

    @FXML private TableView<Part> partsThisBookingTable;
    @FXML private TableColumn<Part, String> partNameCol;
    @FXML private TableColumn<Part, Double> partCostCol;
    
    @FXML private Label bookingInfoLbl;
    @FXML private Label bookingDateLbl;
    @FXML private Label vehicleRegLbl;
    
    @FXML private Button savePartsToBookingBtn;
    @FXML private Button unlinkSelectedBtn;
    @FXML private Button addMorePartsBtn;
   
    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private Booking booking;
    
    private Part selectedPart;  
    private ObservableList<TablePosition> selectedCells;
    
    ArrayList<Part> linkedParts = new ArrayList<>();
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        unlinkSelectedBtn.setVisible(false);
        
        booking = (Booking) diagnosis_repairs.logic.CodeBank.getStoredObject();
        bookingInfoLbl.setText(Integer.toString(booking.getID()));
        vehicleRegLbl.setText(booking.getVehicleRegistration());
        bookingDateLbl.setText(booking.getDate().format(formatter));
        
        try {
            populateTable();
        } catch (SQLException ex) {}
    }    
    
    private void populateTable() throws SQLException {
        int bookingID = booking.getID() ;
        linkedParts = DBQueries2.getPartsThisBooking(bookingID);
        partsThisBookingTable.setItems(FXCollections.observableArrayList(linkedParts));
        partNameCol.setCellValueFactory(new PropertyValueFactory<>("partName"));
        partCostCol.setCellValueFactory(new PropertyValueFactory<>("partCost"));
        
        partsThisBookingTable.setOnMouseClicked(new EventHandler<MouseEvent>() {
            
            @Override
            public void handle(MouseEvent click) {
                if (click.getClickCount() == 1) {
                    unlinkSelectedBtn.setVisible(true);
                    selectedCells = partsThisBookingTable.getSelectionModel().getSelectedCells();;
                    if (selectedCells != null && !selectedCells.isEmpty()) {
                        TablePosition pos = selectedCells.get(0);
                        int row = pos.getRow();
                        partsThisBookingTable.getSelectionModel().select(row); 
                        selectedPart = partsThisBookingTable.getItems().get(row);
                    }   
                }
            }          
        });
    }

    

    @FXML
    private void savePartsToBooking(ActionEvent event) throws IOException {
        
        Pane newContent = FXMLLoader.load(getClass().getResource("/diagnosis_repairs/gui/CompleteBooking.fxml"));
        common.FXMLDocumentController.changeContentPane(newContent);
    }

    @FXML
    private void unlinkBookingFromPart(ActionEvent event) throws SQLException {
        DBQueries2.unlinkBookingFromPart(selectedPart);
        linkedParts = DBQueries2.getPartsThisBooking(booking.getID());
        partsThisBookingTable.setItems(FXCollections.observableArrayList(linkedParts));
        partsThisBookingTable.refresh();
        
        selectedPart = null;
        unlinkSelectedBtn.setVisible(false);
    }

    @FXML
    private void addPartsToBooking(ActionEvent event) throws IOException {
        Pane spawnBookingPane = FXMLLoader.load( getClass().getResource("/parts/GUI/addPartToBooking.fxml" ));
        common.FXMLDocumentController.changeContentPane(spawnBookingPane);
    }
    
}
