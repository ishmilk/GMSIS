/*
 * 
 * Controller for loading the correct screen according to which button was pressed 
 */
package common;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.Window;
import vehicles.Logic.vehicleTemplate;

public class FXMLDocumentController implements Initializable {
    
    public static Pane ContentPane;
    @FXML Pane Content;
    
    @FXML private ComboBox Criti1 = new ComboBox();
    @FXML private ComboBox Criti2 = new ComboBox();
    @FXML private ComboBox cbTemplate = new ComboBox();
    @FXML private ComboBox cbType = new ComboBox();
    @FXML private TextField SearchBox = new TextField();
    @FXML private Button btnLogout = new Button();
    @FXML private Button btnSearch ;
    
    @FXML private Button btnUsers = new Button();
    @FXML private Button btnStock = new Button();
    
    @FXML private Button btnCustomer ;
    @FXML private Button btnVehicle ;
    @FXML private Button btnBooking ;
    @FXML private Button btnParts ;
    
    /**
     *
     * @param event
     * @throws java.io.IOException
     */
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) 
    {
        Criti1.getItems().addAll("Customer", "Vehicle", "Booking", "Parts");
        ContentPane = this.Content;
        
        //decides if the user is an admin or not 
        if( CurrentUser.getInstance().getisAdmin() )
        {
            
            btnUsers.setDisable( false ) ;
            btnStock.setDisable( false ) ;
            
        }
        
        else
        {
            
            btnUsers.setDisable( true ) ;
            btnStock.setDisable( true ) ;
            
        }
    }
    
    //Load different criteria 2 options depending on criteria 1 selected 
    @FXML
    public void selectCriti1()
    {
        SearchBox.setText("");
        
        switch((String)Criti1.getValue())
        {
            case("Customer"):   Criti2.getItems().clear();
                                Criti2.getItems().addAll("Name", "Registration");
                                break;
            
            case("Vehicle"):    Criti2.getItems().clear();
                                Criti2.getItems().addAll("Registration", "Make", "Type", "Template"); 
                                break;
            
            case("Booking"):    Criti2.getItems().clear();        
                                Criti2.getItems().addAll("Name", "Registration", "Make", "Template");
                                break;
            
            case("Parts"):      Criti2.getItems().clear();
                                Criti2.getItems().addAll("Name", "Registration"); 
                                break;
            
                    
        }
        
    }
    
    //when the search button in pressed, go to the correct screen 
    @FXML
    public void showSearch() throws IOException
    {
       //checks all of the infromation has been entered for a search  
        if((!SearchBox.getText().equals("") || !cbTemplate.getSelectionModel().isEmpty() ) && !Criti1.getSelectionModel().isEmpty() && !Criti2.getSelectionModel().isEmpty())
        {
            
            /* increase the window height */
            Window stage = btnSearch.getScene().getWindow() ;
            stage.setHeight( 740 ) ;
            
            switch((String)Criti1.getValue())
            {
                case("Customer"):
                    searchCustomer();
                    break;
                
                case("Vehicle"):          
                    searchVehicle();
                    break;
                    
                case("Booking"):
                    searchBooking();
                    break;
                    
                case("Parts"):
                    searchPart(); 
                    break;
               
            }//end of swtich 
        }//end of if
        else
        {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Unable to Search");
            alert.setHeaderText("The search criteria is not complete");
            alert.setContentText("Please select an area to search, criteria for the search and enter the value you are searching for");
            alert.showAndWait();
        }
        
    }
    
    
//CUSTOMER -----------------------------------------------------------------------------------------------------------------------------------------------------    

    public void searchCustomer() throws IOException
    {
        Context.getInstance().setSearch(SearchBox.getText());
        Context.getInstance().setCriteria((String) Criti2.getValue());

        //GO TO THE FXML
       Pane newLoadedPane = FXMLLoader.load(getClass().getResource("/customers/gui/CustSearch.fxml"));
      changeContent(newLoadedPane);
        
    }
    
//--------------------------------------------------------------------------------------------------------------------------------------------------------------  

    
    
//VEHICLE -----------------------------------------------------------------------------------------------------------------------------------------------------  
    
    //when you search for a vehicle 
    public void searchVehicle() throws IOException
    {
        System.out.println("VEH");
        if(SearchBox.isVisible())
        {
            Context.getInstance().setSearch(SearchBox.getText());
        } 
        else 
        {
            Context.getInstance().setSearch((String) cbTemplate.getValue());
        }

        Context.getInstance().setCriteria((String) Criti2.getValue());

        if (((String) Criti2.getValue()).equals("Type")) 
        {
            if ((SearchBox.getText()).equals("Car") || (SearchBox.getText()).equals("Van") || (SearchBox.getText()).equals("Truck")) 
            {
                System.out.println("LOAD VEHI 1");
                Pane newLoadedPane = FXMLLoader.load(getClass().getResource("/vehicles/GUI/SearchVehicle.fxml"));
                changeContent(newLoadedPane);
            } 
            else 
            {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Unable to Search");
                alert.setHeaderText("The search criteria does not exist");
                alert.setContentText("Please enter a differnt search critera");
                alert.showAndWait();
            }
        } 
        else 
        {
            System.out.println("LOAD VEHI 2");
            Pane newLoadedPane = FXMLLoader.load(getClass().getResource("/vehicles/GUI/SearchVehicle.fxml"));
            changeContent(newLoadedPane);
        }
    }
       
//-------------------------------------------------------------------------------------------------------------------------------------------------------------- 
    
  
    
    
//BOOKING -----------------------------------------------------------------------------------------------------------------------------------------------------    
   
    public void searchBooking() throws IOException
    {
        
        System.out.println("BOOK");
        
        if( SearchBox.isVisible() )
        {
            
            Context.getInstance().setSearch( SearchBox.getText() ) ;
            
        } 
        
        else 
        {
            
            Context.getInstance().setSearch(  ( String ) cbTemplate.getValue()  ) ;
            
        }

        Context.getInstance().setCriteria(  ( String ) Criti2.getValue()  ) ;

        System.out.println("LOAD BOOK");
        
        Pane newLoadedPane = FXMLLoader.load( getClass().getResource( "/diagnosis_repairs/gui/SearchBooking.fxml" )  ) ;
        
        changeContent( newLoadedPane ) ;
        
    }
    
//--------------------------------------------------------------------------------------------------------------------------------------------------------------    
    
    
    
    
    
    
//Parts -----------------------------------------------------------------------------------------------------------------------------------------------------    

    public void searchPart() throws IOException
    {
        Context.getInstance().setSearch(SearchBox.getText());
        Context.getInstance().setCriteria((String) Criti2.getValue());

        Pane newLoadedPane = FXMLLoader.load(getClass().getResource("/parts/GUI/searchParts.fxml"));
        changeContent(newLoadedPane);
    }
    
//--------------------------------------------------------------------------------------------------------------------------------------------------------------
    
   
    
//DEALING WITH CRITERIA CHANGES------------------------------------------------------------------------------------------------------------------------------------

//showing templates if that is the search criteria 
    @FXML
    public void alterSearch()
    {
        SearchBox.setText("");
        try
        {
            if(Criti2.getValue().equals("Template"))
            {
                cbTemplate.setVisible(true);
                SearchBox.setVisible(false);
                showTemplates();

            }
            else if(Criti2.getValue().equals("Type"))
            {
                cbType.setVisible(true);
                SearchBox.setVisible(false);
                cbType.getItems().addAll("Car", "Van", "Truck");
            }
            else 
            {
                cbTemplate.setVisible(false);
                cbType.setVisible(false);
                SearchBox.setVisible(true);
            }
        }
        catch (NullPointerException e)
        {
            
        }
    }
    
    
    //showing templates when searching for a vehicle 
    public void showTemplates()
    {
        try
        {
            cbTemplate.getItems().clear();
            
            Connection c = common.DatabaseConnector.activateConnection();
            c.setAutoCommit( true ); 
            Statement stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM VehicleTemplates" );

            // load templates into drop down
            while ( rs.next() )
            {
                String make = rs.getString("make");
                String model = rs.getString("model");
                Double engine = rs.getDouble("engineSize");
                String fuel = rs.getString("fuelType");
                
                vehicleTemplate x = new vehicleTemplate(make, model, engine, fuel);
                
                cbTemplate.getItems().add(x.showTemplate());

            }

            // close connection
            c.close() ;
            
        }
        catch (SQLException e)
        {
            
        } 
    }    
    
 //-------------------------------------------------------------------------------------------------------------------------------------------   
    


//BUTTONS------------------------------------------------------------------------------------------------------------------------------------
    
    @FXML
    public void showCust(ActionEvent event) throws IOException 
    {
        
        btnCustomer.setStyle( " -fx-background-color : #aecfe5 ; " ) ;
        btnVehicle.setStyle ( "" ) ;
        btnBooking.setStyle ( "" ) ;
        btnParts.setStyle   ( "" ) ;
        btnUsers.setStyle   ( "" ) ;
        
        Pane newLoadedPane = FXMLLoader.load(getClass().getResource("/customers/gui/CustomerGUI.fxml"));
        changeContent(newLoadedPane);
        
        /* increase the window height */
        Window stage = btnSearch.getScene().getWindow() ;
        stage.setHeight( 740 ) ;
        
    }

    @FXML
    public void showVehic(ActionEvent event) throws IOException 
    {
       
        btnCustomer.setStyle( "" ) ;
        btnVehicle.setStyle ( " -fx-background-color : #aecfe5 ; " ) ;
        btnBooking.setStyle ( "" ) ;
        btnParts.setStyle   ( "" ) ;
        btnUsers.setStyle   ( "" ) ;
        
        Pane newLoadedPane = FXMLLoader.load(getClass().getResource("/vehicles/GUI/Vehicle.fxml"));
        changeContent(newLoadedPane);
        
        /* increase the window height */
        Window stage = btnSearch.getScene().getWindow() ;
        stage.setHeight( 740 ) ;
    
    }

    @FXML
    public void showBooking( ActionEvent event ) throws IOException 
    {
        
        btnCustomer.setStyle( "" ) ;
        btnVehicle.setStyle ( "" ) ;
        btnBooking.setStyle ( " -fx-background-color : #aecfe5 ; " ) ;
        btnParts.setStyle   ( "" ) ;
        btnUsers.setStyle   ( "" ) ;
        
        Pane newLoadedPane = FXMLLoader.load(  getClass().getResource( "/diagnosis_repairs/gui/BookingPane.fxml" )  ) ;
        changeContent( newLoadedPane ) ;
        
        /* increase the window height */
        Window stage = btnSearch.getScene().getWindow() ;
        stage.setHeight( 740 ) ;
        
    }

    @FXML
    public void showPart(ActionEvent event) throws IOException 
    {
    
        btnCustomer.setStyle( "" ) ;
        btnVehicle.setStyle ( "" ) ;
        btnBooking.setStyle ( "" ) ;
        btnParts.setStyle   ( " -fx-background-color : #aecfe5 ; " ) ;
        btnUsers.setStyle   ( "" ) ;
        
        Pane newLoadedPane = FXMLLoader.load(getClass().getResource("/parts/GUI/manageStockScreen.fxml"));
        changeContent(newLoadedPane);
        
        /* increase the window height */
        Window stage = btnSearch.getScene().getWindow() ;
        stage.setHeight( 740 ) ;
    
    }
    
    @FXML
    public void showEmp(ActionEvent event) throws IOException 
    {
        
        btnCustomer.setStyle( "" ) ;
        btnVehicle.setStyle ( "" ) ;
        btnBooking.setStyle ( "" ) ;
        btnParts.setStyle   ( "" ) ;
        btnUsers.setStyle   ( " -fx-background-color : #aecfe5 ; " ) ;
        
        Pane newLoadedPane = FXMLLoader.load(getClass().getResource("/authentication/Employee.fxml"));
        changeContent(newLoadedPane);
        
        /* increase the window height */
        Window stage = btnSearch.getScene().getWindow() ;
        stage.setHeight( 740 ) ;
    
    }

    @FXML
    public void showDate(ActionEvent event) throws IOException 
    {
        Pane newLoadedPane = FXMLLoader.load(getClass().getResource("/vehicles/Date.fxml"));
        changeContent(newLoadedPane);
    }
    @FXML
    public  void changeContent(Pane NewContent)
    {
        Content.getChildren().clear();
        Content.getChildren().add(NewContent);
    }
    /**
     *
     * @param event
     */
    @FXML
    public void logOut(ActionEvent event) throws IOException 
    {
        Stage stage = (Stage) btnLogout.getScene().getWindow();
        stage.close();
        
        Parent root = FXMLLoader.load( getClass().getResource( "/authentication/Login.fxml" ) );
        Stage newStage = new Stage();
        newStage.setTitle( "GM-SIS" );
        Scene theScene = new Scene( root , 575 , 320 ) ;
        newStage.setScene( theScene );
        //newStage.getIcons().add(  new Image( "/diagnosis_repairs/gui/pepe.jpg" )  ) ;
        theScene.getStylesheets().add("/common/loginStylesheet.css") ;
        newStage.show(); 
    }
    
    
    public static void changeContentPane(Pane newContent)
    {
        ContentPane.getChildren().clear();
        ContentPane.getChildren().add(newContent);
    }
    

}//end of class
