/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vehicles.Logic;

import javafx.scene.control.Alert;

/**
 *
 * @author Amy Dowse
 */
public class Alerts 
{
    
    //Helper methods for where there is incorrect data/input
    //Seperate alter methods for different types for ease of chaninging 
    //e.g.  if you want to change 'nocustomer' to error rather than altert - only change one, not all of them 
    
    public static void invalidDate()
    {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Invalid Date");
        alert.setHeaderText("The entered real date(s) that are in the incorrect format");
        alert.setContentText("Please enter dates as: dd/mm/yyyy");
        alert.showAndWait();
    }
    
    public static void noCustomer()
    {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Missing Customer");
        alert.setHeaderText("There is no selected customer");
        alert.setContentText("Please search for and select the owner of the vehicle you are trying to save");
        alert.showAndWait();
    }
    
    public static void missingData()
    {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Missing Data");
        alert.setHeaderText("You have not entered all needed data");
        alert.setContentText("Please fill in the missing data and press 'SAVE' again");
        alert.showAndWait();
    }
    
    public static void repeatedReg()
    {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Repeated Registration");
        alert.setHeaderText("The registration you have entered is already registerd");
        alert.setContentText("Please check the registration you are entering");
        alert.showAndWait();
    }
    
    public static void deleteSuccessful(String reg)
    {
       Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
       alert.setTitle("Delete Successful");
       alert.setHeaderText("Delete Successful");
       alert.setContentText("You have deleted " + reg);
       alert.showAndWait();
    }
}
