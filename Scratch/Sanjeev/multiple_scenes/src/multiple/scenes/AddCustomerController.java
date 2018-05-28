package multiple.scenes;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.layout.Pane;

/**
 * FXML Controller class
 *
 * @author P Sanjeev v
 */
public class AddCustomerController implements Initializable {
    
    /**
     * Initializes the controller class.
     * @param event
     * @throws java.io.IOException
     */
    @FXML
    public void AddCust(ActionEvent event) throws IOException
    {
        Pane NewContent=FXMLLoader.load(getClass().getResource("AddCustomer.fxml"));
        
        
        FXMLDocumentController.changeContentPane(NewContent);
    }
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
}
