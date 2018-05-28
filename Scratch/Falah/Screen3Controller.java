/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package screensframework;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

/**
 * FXML Controller class
 *
 * @author Administrator
 */
public class Screen3Controller implements Initializable, ControlledScreen {
    ScreensController myController;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
     
    public void setScreenParent(ScreensController screenParent){
        myController = screenParent;
    }
    @FXML
    private void goToAddCustomer(ActionEvent event){
       myController.setScreen(ScreensFramework.screen4ID); // goes to add customer 
    }
    @FXML
    private void goToAddVehicle(ActionEvent event){
       myController.setScreen(ScreensFramework.screen5ID);
    }
    @FXML
    private void goToMakeBooking(ActionEvent event){
       myController.setScreen(ScreensFramework.screen6ID);
    }
    @FXML
    private void goToBookingInfo(ActionEvent event){
       myController.setScreen(ScreensFramework.screen7ID);
    }
    @FXML
    private void goToSearch(ActionEvent event){
       myController.setScreen(ScreensFramework.screen3ID);
    }
    @FXML
    private void goToStock(ActionEvent event){
       myController.setScreen(ScreensFramework.screen2ID);
    }
}
