/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package multiple.scenes;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.layout.Pane;

/**
 *
 * @author P Sanjeev v
 */
public class FXMLDocumentController implements Initializable {
    public static Pane ContentPane;
    @FXML
    Pane Content;

    /**
     *
     * @param event
     * @throws java.io.IOException
     */
    @FXML
    public void showCust(ActionEvent event) throws IOException {
        //Content.getChildren().clear();
        Pane newLoadedPane = FXMLLoader.load(getClass().getResource("content1.fxml"));
        //Content.getChildren().add(newLoadedPane);
        changeContent(newLoadedPane);
        //Content.getChildren().clear();
        //Content.getChildren().add(FXMLLoader.load(getClass().getResource("content1.fxml")));
        //System.out.println("Customer shown   "+getClass().getResource("\\content1.fxml")+Content.getChildren());
    }

    @FXML
    public void showVehic(ActionEvent event) throws IOException {
        //Content.getChildren().clear();
        Pane newLoadedPane = FXMLLoader.load(getClass().getResource("content2.fxml"));
        //Content.getChildren().add(newLoadedPane);
        changeContent(newLoadedPane);
    }

    @FXML
    public void showBooking(ActionEvent event) throws IOException {
        //Content.getChildren().clear();
        Pane newLoadedPane = FXMLLoader.load(getClass().getResource("content3.fxml"));
        //Content.getChildren().add(newLoadedPane);
        changeContent(newLoadedPane);
    }

    @FXML
    public void showPart(ActionEvent event) throws IOException {
        Pane newLoadedPane = FXMLLoader.load(getClass().getResource("content4.fxml"));
        //Content.getChildren().add(newLoadedPane);
        changeContent(newLoadedPane);
    }

    @FXML
    public void showSpecial(ActionEvent event) throws IOException {
        Pane newLoadedPane = FXMLLoader.load(getClass().getResource("content5.fxml"));
        //Content.getChildren().add(newLoadedPane);
        changeContent(newLoadedPane);
    }
    @FXML
    public  void changeContent(Pane NewContent)
    {
        Content.getChildren().clear();
        Content.getChildren().add(NewContent);
        //System.out.println("Content Changed");
    }
    /**
     *
     * @param event
     */
    @FXML
    public void logOut(ActionEvent event) {
        System.out.println("Logged Out");
        System.exit(0);
        
    }
    public static void changeContentPane(Pane newContent)
    {
        ContentPane.getChildren().clear();
        ContentPane.getChildren().add(newContent);
    }
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        ContentPane=this.Content;
    }

}
