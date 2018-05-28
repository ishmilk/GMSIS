/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package parts.logic;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;

/**
 * FXML Controller class
 *
 * @author saphirepencil
 */
public class UsedPartsScreen2Controller implements Initializable {

    
    @FXML private Button goBackBtn;
    @FXML private TableView<Part> partsTable;   
    @FXML private TableColumn<Part, Integer> partIDCol;   
    @FXML private TableColumn<Part, String> partNameCol;   
    @FXML private TableColumn<Part, String> partDescrCol;   
    @FXML private TableColumn<Part, String> dateInstalledCol;   
    @FXML private TableColumn<Part, Double> partCostCol;
    
    
    private ArrayList<Part> parts;
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            populateTable();
        } catch (SQLException ex) {}
    }  
    
     private void populateTable() throws SQLException {     
            
            parts = DBQueries2.fetchUsedParts();
            
            if (parts.isEmpty()) { 
                System.out.println("parts empty!");
            }
                    
            partsTable.setItems(FXCollections.observableArrayList(parts));      
            partsTable.setEditable(false);
            
            partIDCol.setCellValueFactory(new PropertyValueFactory<>("partID"));
            partNameCol.setCellValueFactory(new PropertyValueFactory<>("partName"));
            partDescrCol.setCellValueFactory(new PropertyValueFactory<>("description"));
            dateInstalledCol.setCellValueFactory(new PropertyValueFactory<>("dateInstalled"));
            partCostCol.setCellValueFactory(new PropertyValueFactory<>("cost"));
            
          
        
    }

    @FXML
    private void goBack(ActionEvent event) throws IOException {
        Pane newContent = FXMLLoader.load(getClass().getResource("manageStockScreen.fxml"));
        common.FXMLDocumentController.changeContentPane(newContent);
        
    }
    
}
