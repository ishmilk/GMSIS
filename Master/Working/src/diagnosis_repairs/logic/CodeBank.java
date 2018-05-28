                        
                    /*─────────────────┐
                    │      author : ismail.      │
                    └─────────────────*/

package diagnosis_repairs.logic ;

import java.util.*                   ; // imports Java UTIL package
import java.text.*                   ; // imports Java TEXT package

/******************************************************************************/

import javafx.scene.control.*        ; // imports JavaFX scene control package
import javafx.scene.control.Alert.*  ; // imports JavaFX scene alert package

public class CodeBank 
{
    
    // instance variables
    private static Object storedObj ;

    /* 
        idea from : http://code.makery.ch/blog/javafx-dialogs-official/
    */
    /* this method displays an error alert box with given title + content */
    public static void showErrorAlert( String TITLE , String CONTENT ) 
    {
        
        // create a new alert obj
        Alert errorAlert = new Alert( AlertType.ERROR );

        // set title with method --> .setTitle()
        errorAlert.setTitle( TITLE );

        // set header text with method --> .setHeaderText()
        errorAlert.setHeaderText( null );

        // set content text with method --> .setcontentText()
        errorAlert.setContentText( CONTENT );

        // don't allow any interations with any other window except the alert window
        errorAlert.showAndWait();

    }
    
    /* 
        idea from : http://code.makery.ch/blog/javafx-dialogs-official/
    */
    /* this method displays an confirmation alert box with given title + content */
    /* returns the option of which button pressed */
    public static Optional<ButtonType> showConfirmationAlert( String TITLE , String CONTENT ) 
    {
        
        // create a new alert obj
        Alert confirmationAlert = new Alert( AlertType.CONFIRMATION );

        // set title with method --> .setTitle()
        confirmationAlert.setTitle( TITLE );

        // set header text with method --> .setHeaderText()
        confirmationAlert.setHeaderText( null );

        // set content text with method --> .setcontentText()
        confirmationAlert.setContentText( CONTENT );

        // don't allow any interations with any other window except the alert window
        // return the result of the button press
        return confirmationAlert.showAndWait();

    }

    /* this helper method stores an object reference */
    public static boolean storeObject( Object objectToStore ) 
    {
        
        try                         // obj reference saved
        {
            
            storedObj = objectToStore ;
            System.out.println( "** OBJECT REFERENCE HAS BEEN SAVED **" ) ;
            return true ;
        
        }
        
        catch ( Exception e )       // flopped
        {
        
            e.printStackTrace() ;
            System.out.println( "** NO OBJECT REFERENCE HAS BEEN SAVED **" ) ;
            return false ;
        
        }
        
    }
    
    /* this helper method returns the stored object reference */
    public static Object getStoredObject()
    {
    
        System.out.println( "** OBJECT REFERENCE HAS BEEN RETURNED **" ) ;
        return storedObj ;
    
    }
    
    /* this helper method restricts a textfield obj to input decimal values ONLY */
    public static void applyDecimalRestriction( TextField TF )
    {
        
        DecimalFormat format = new DecimalFormat( "#.0" ) ;
        
        TF.setTextFormatter( new TextFormatter<>(c ->
        {
            
            if ( c.getControlNewText().isEmpty() )
            {
                
                return c ;
                
            }
            
            ParsePosition parsePosition = new ParsePosition( 0 );
            
            Object object = format.parse( c.getControlNewText() , parsePosition ) ;
            
            if ( object == null || parsePosition.getIndex() < c.getControlNewText().length() )
            {
                
                return null ;
                
            }
            
            else
            {
                
                return c ;
                
            }
            
        }));
        
    }

}