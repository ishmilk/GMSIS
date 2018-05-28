                        
                    /*─────────────────┐
                    │      author : ismail.      │
                    └─────────────────*/

package common;

import javafx.application.*    ; // imports JavaFX application package
import javafx.fxml.*           ; // imports JavaFX .fxml package
import javafx.event.*          ; // imports JavaFX events package
import javafx.scene.*          ; // imports JavaFX scene package
import javafx.scene.image.*    ; // imports JavaFX image scene package
import javafx.scene.control.*  ; // imports JavaFX scene UI control package
import javafx.scene.layout.*   ; // imports JavaFX scene layout package
import javafx.stage.*          ; // imports JavaFX stage package


// to use JavaFX, the class must inherit from 'Application'
public class Main extends Application
{
    
    public static void main( String GMSIS[] ) throws Exception
    {
        // the launch() precedure sets up the program as a JavaFX application    
        launch ( GMSIS ) ;
        // the procedure start() is called after launch()
    }

    @Override // http://stackoverflow.com/questions/94361/when-do-you-use-javas-override-annotation-and-why
    public void start ( Stage primaryStage ) throws Exception
    {
        /*
            * stage = the window
            * scene = everything inside stage
        */
        
        // load the JavaFX xml file used ( make the stage )
        Parent root = FXMLLoader.load( getClass().getResource( "/authentication/Login.fxml" ) );
              
        // .setTitle() allows you to set stage title
        primaryStage.setTitle( "GM-SIS" );
        
        // set the window icon
        //primaryStage.getIcons().add(  new Image( "/diagnosis_repairs/gui/pepe.jpg" )  ) ;
        
        // create a new scene obj
        Scene theScene = new Scene( root , 575 , 320 ) ;
        
        // .setScene() allows you to spawn new scene obj into stage
        primaryStage.setScene( theScene );
        
        // .getStylesheets() + .add() allow you to insert custom stylesheets
        theScene.getStylesheets().add("/common/loginStylesheet.css") ;
        
        // .show() allows you to see your stage
        primaryStage.show();
        
    }
        
}
