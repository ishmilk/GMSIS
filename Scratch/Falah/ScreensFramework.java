/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package screensframework;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 *
 * 
 */
public class ScreensFramework extends Application {
    
    public static String screen1ID = "main";
    public static String screen1File = "Screen1.fxml";
    public static String screen2ID = "screen2";
    public static String screen2File = "Screen2.fxml";
    public static String screen3ID = "screen3";
    public static String screen3File = "Screen3.fxml";
    public static String screen4ID = "screen4";
    public static String screen4File = "Screen4.fxml";
    public static String screen5ID = "screen5";
    public static String screen5File = "Screen5.fxml";
    public static String screen6ID = "screen6";
    public static String screen6File = "Screen6.fxml";
    public static String screen7ID = "screen7";
    public static String screen7File = "Screen7.fxml";
    
    
    @Override
    public void start(Stage primaryStage) {
        
        ScreensController mainContainer = new ScreensController();
        mainContainer.loadScreen(ScreensFramework.screen1ID, ScreensFramework.screen1File);
        mainContainer.loadScreen(ScreensFramework.screen2ID, ScreensFramework.screen2File);
        System.out.println("loading screens");
        mainContainer.loadScreen(ScreensFramework.screen3ID, ScreensFramework.screen3File);
        mainContainer.loadScreen(ScreensFramework.screen4ID, ScreensFramework.screen4File);
        mainContainer.loadScreen(ScreensFramework.screen5ID, ScreensFramework.screen5File);
        mainContainer.loadScreen(ScreensFramework.screen6ID, ScreensFramework.screen6File);
        mainContainer.loadScreen(ScreensFramework.screen7ID, ScreensFramework.screen7File);
        
        mainContainer.setScreen(ScreensFramework.screen1ID);
        
        Group root = new Group();
        root.getChildren().addAll(mainContainer);
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /**
     * The main() method is ignored in correctly deployed JavaFX application.
     * main() serves only as fallback in case the application can not be
     * launched through deployment artifacts, e.g., in IDEs with limited FX
     * support. NetBeans ignores main().
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
}