/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package parts.logic;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;

/**
 * FXML Controller class
 *
 * @author saphirepencil
 */
public class AddNewPartScreenController implements Initializable {

    @FXML private TextArea partDescriptionTxt;
    @FXML private DatePicker partDateInStockPckr;
    @FXML private TextField partCostTxt;
    
    private final ObservableList<String> options = 
    FXCollections.observableArrayList(
        "Spark Plug",
        "Oil Filter",
        "Battery",
        "Tire",
        "Alternator",
        "Fuel Pump",
        "Clutch",
        "Water Pump",
        "Headlight",
        "Windshield Wiper Blades");
  
    @FXML private ComboBox<String> distinctPartsCmb;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        distinctPartsCmb.setItems(options);  
    }    
    
    @FXML
    private void saveNewPart(ActionEvent event) throws IOException {
        
        String name, descr;             
        int ID;
        Double cost;
        LocalDate inStock;
        Part part;
        
        ID = DBQueries2.assignID();
                
        if (distinctPartsCmb.getValue() != null && !distinctPartsCmb.getValue().trim().isEmpty()) {
                    name = distinctPartsCmb.getValue();
                }
                else {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setContentText("Please enter the part name!");
                    alert.show();
                    return;
                    
                }
                
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
                part = new Part(ID, name, descr, cost, inStock);   
                DBQueries2.addPart(part);
                
                Pane newContent = FXMLLoader.load(getClass().getResource("manageStockScreen.fxml"));
                common.FXMLDocumentController.changeContentPane(newContent);
    }
    
    @FXML
    public void goBack() throws IOException {
        Pane newContent = FXMLLoader.load(getClass().getResource("manageStockScreen.fxml"));
        common.FXMLDocumentController.changeContentPane(newContent);
    }
    
}
