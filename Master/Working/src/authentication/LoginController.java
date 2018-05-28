                        
                    /*─────────────────┐
                    │      author : ismail.      │
                    └─────────────────*/

package authentication;

import common.CurrentUser;
import common.DatabaseConnector;
import java.net.URL;
import java.sql.*;               // imports Java SQL package 
import java.util.ResourceBundle;
/******************************************************************************/
import javafx.fxml.*           ; // imports JavaFX .fxml package
import javafx.scene.*          ; // imports JavaFX scene package
import javafx.scene.image.*    ; // imports JavaFX image scene package
import javafx.scene.control.*  ; // imports JavaFX scene UI control package
import javafx.scene.paint.*    ; // imports JavaFX scene colour + gradients package
import javafx.stage.*          ; // imports JavaFX stage package
import javafx.event.*          ; // imports JavaFX event package
import javafx.scene.input.*    ; // imports JavaFX scene package


public class LoginController implements Initializable
{
    
    // declare variables with the same name as components in .fxml file to link
    
    /*
     * NOTE : when using variables linked with .fxml files,
     *        you MUST override with the override statement '@FXML'
     *        above the variable
    */
    
    @FXML private Label         confirmationOutput                ;
    @FXML private TextField     txtLoginID  = new TextField()     ;
    @FXML private PasswordField txtPassword = new PasswordField() ;
    @FXML private Button        btnLogin    = new Button();
   
    private boolean       userIsAnAdmin ;
    
    @Override
    public void initialize( URL location, ResourceBundle resources ) 
    {
        
        /* allows to log in VIA pressing enter / return button */
        txtPassword.setOnKeyPressed( new EventHandler<KeyEvent>()
        {
            
            @Override
            public void handle( KeyEvent keyEvent ) 
            {
                
                // check if enter / return key is pressed
                if ( keyEvent.getCode() == KeyCode.ENTER )  
                {

                    btnLogin.fire() ;

                }

            }
            
        });
        
    }
    
    public void attemptLogIn() throws Exception
    {
        
        // open a connection
        Connection c = DatabaseConnector.activateConnection();
        
        // automatically commits for me
        c.setAutoCommit( true ); 
        
        // spawn object to store results of query
        ResultSet rs ;
        
        // when creating a statement object, you MUST use a connection object to call the instance method
        Statement stmt = c.createStatement();
        
        rs = stmt.executeQuery("SELECT employeeID , password , isAdmin FROM Employee WHERE employeeID = '" + txtLoginID.getText() + "' AND password = '" + txtPassword.getText() + "'" );
        
        if( rs.next() == false )
        {
            
            confirmationOutput.setTextFill( Color.rgb( 255 , 0 , 0 ) );
            //confirmationOutput.setText( "NOPE" );
            confirmationOutput.setText( "Login or Password are incorrect - please try again" );
            
            // clear the txt boxes + reset the cursor to txtLoginID
            txtLoginID.positionCaret( 0 ) ;
            
            // clear txt boxes for user
            txtPassword.clear() ;
            
        }
        
        else
        {
            
            confirmationOutput.setTextFill( Color.rgb( 0 , 255 , 0 ) );
            confirmationOutput.setText( "Credentials Accepted" );
            
            // wait for 2 secs
            //Thread.sleep( 5000 );
            
            userIsAnAdmin = rs.getBoolean( "isAdmin" ) ;
            
            CurrentUser.getInstance().setUserName(txtLoginID.getText());
            CurrentUser.getInstance().setisAdmin(userIsAnAdmin);
             
            
            Parent root = FXMLLoader.load( getClass().getResource( "/common/mainScreen.fxml" ) );
            
            Stage stage = (Stage) btnLogin.getScene().getWindow();
            // do what you have to do
            stage.close();
            
            
            Stage window = new Stage() ;
            //window.getIcons().add(  new Image( "/diagnosis_repairs/gui/pepe.jpg" )  ) ;
            //Scene scene  = new Scene( root , 988 , 700 ) ;
            Scene scene = new Scene( root );
            window.setScene( scene );
            window.setWidth( 1000 ) ;
            window.setHeight( 200 ) ;
            window.setResizable( false );
            window.show();
                
            // clear txt boxes for user
            txtLoginID.clear()  ;
            txtPassword.clear() ;
            
        }
        
        c.close();   
        
    }

}