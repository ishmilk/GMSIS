/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package customers.gui;
import customers.logic.*;
import common.DatabaseConnector;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import vehicles.Logic.vehicle;

/**
 *
 * @author P Sanjeev v
 */
public class DataFunc {

    Connection c;
    Statement st;
    ResultSet rs;

    public DataFunc() throws SQLException {

    }

    public void DelCust(int ID) throws SQLException {
        c = DatabaseConnector.activateConnection();
        st = c.createStatement();
        String query = "DELETE FROM 'Customer' WHERE customerID == ?";
        PreparedStatement preparedStmt = (PreparedStatement) c.prepareStatement(query);
        preparedStmt.setInt(1, ID);
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Delete Customer");
        alert.setHeaderText(null);
        if (!preparedStmt.execute()) {
            alert.setContentText("Succesfully deleted Customer Account1");
        } else {
            alert.setContentText("Unable to delete Customer Account. \nPlease Try Again");
        }
        alert.showAndWait();
        c.close();

    }

    public ArrayList<Customer> GetAllCust() throws SQLException {
        c = DatabaseConnector.activateConnection();
        c.setAutoCommit(false);
        st = c.createStatement();
        ArrayList<Customer> all = new ArrayList<>();

        rs = st.executeQuery("SELECT * FROM 'Customer'");
        while (rs.next()) {
            //System.out.println(rs.getString("name"));
            all.add(new Customer(rs.getInt("customerID"), rs.getString("name"), rs.getString("addressLine1"), rs.getString("addressLine2"), rs.getString("postCode"), rs.getString("phoneNumber"), rs.getString("email"), rs.getString("type")));

        }
        c.close();
        return all;

    }

    
    public ArrayList<Customer> GetAllBus() throws SQLException {
        c = DatabaseConnector.activateConnection();
        c.setAutoCommit(false);
        st = c.createStatement();
        ArrayList<Customer> all = new ArrayList<>();

        rs = st.executeQuery("SELECT * FROM 'Customer' WHERE type=='Business'");
        while (rs.next()) {
            //System.out.println(rs.getString("name"));
            all.add(new Customer(rs.getInt("customerID"), rs.getString("name"), rs.getString("addressLine1"), rs.getString("addressLine2"), rs.getString("postCode"), rs.getString("phoneNumber"), rs.getString("email"), rs.getString("type")));

        }
        c.close();
        return all;

    }
    public ArrayList<Customer> GetAllIndiv() throws SQLException {
        c = DatabaseConnector.activateConnection();
        c.setAutoCommit(false);
        st = c.createStatement();
        ArrayList<Customer> all = new ArrayList<>();

        rs = st.executeQuery("SELECT * FROM 'Customer' WHERE type=='Individual'");
        while (rs.next()) {
            //System.out.println(rs.getString("name"));
            all.add(new Customer(rs.getInt("customerID"), rs.getString("name"), rs.getString("addressLine1"), rs.getString("addressLine2"), rs.getString("postCode"), rs.getString("phoneNumber"), rs.getString("email"), rs.getString("type")));

        }
        c.close();
        return all;

    }
    
    
    public Customer GetByID(int ID) throws SQLException {
        c = DatabaseConnector.activateConnection();
        c.setAutoCommit(false);
        st = c.createStatement();
        rs = st.executeQuery("SELECT * FROM 'Customer' WHERE customerID==" + ID + ";");
        Customer cc = null;
        while (rs.next()) {
            int newID = rs.getInt("customerID");
            String newName = rs.getString("name");
            String newAddr1 = rs.getString("addressLine1");
            String newAddr2 = rs.getString("addressLine2");
            //String newCity=rs.getString("City");
            String newPost = rs.getString("postCode");
            String newEmail = rs.getString("email");
            String newPhone = rs.getString("phoneNumber");
            String newtype = rs.getString("type");
            cc = new Customer(newID, newName, newAddr1, newAddr2, newPost, newEmail, newPhone, newtype);
        }
        c.close();
        return cc;
    }

    public ArrayList<VehList> getVeh(int ID) throws SQLException
    {
        c=DatabaseConnector.activateConnection();
        c.setAutoCommit(false);
        st=c.createStatement();
        String query="SELECT * FROM 'Vehicle' WHERE customerID=="+ID+";";
        ArrayList<VehList> ans=new ArrayList<>();
        rs=st.executeQuery(query);
        while(rs.next())
        {
            ans.add(new VehList(rs.getString("registration"),rs.getString("make"),rs.getString("model"),rs.getString("fuelType"),rs.getString("classification")));
        }
        c.close();
        return ans;
        
    }
    public ArrayList<vehicle> getAllVeh(int ID) throws SQLException {
        c = DatabaseConnector.activateConnection();
        c.setAutoCommit(false);
        int i = 0;
        st = c.createStatement();
        ArrayList<vehicle> all = new ArrayList<>();
        //System.out.println("reaches database");
        rs = st.executeQuery("SELECT* FROM 'Vehicle' WHERE customerID==" + ID + ";"); //WHERE customerID=="+ID+";");
        while (rs.next()) {
               // System.out.println("exists");
                int warrantyInt = rs.getInt("warranty");

                int customerID = rs.getInt("customerID");

                String MOT = rs.getString("MOTDate");
                String service = rs.getString("serviceDate");

                LocalDate MOTDate = null;
                LocalDate serviceDate = null;
                boolean warranty = false;

                if (warrantyInt == 1) {
                    warranty = true;
                }
                //System.out.println(ID +rs.getString("motDate"));
                try
                {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
                 MOTDate = LocalDate.parse(MOT,formatter);
                serviceDate = LocalDate.parse(service,formatter);
                }
                catch(Exception e)
                {
                
                }
            vehicle x=new vehicle(warranty, rs.getString("classification"), rs.getString("registration"), rs.getString("make"), rs.getString("model"), rs.getDouble("engineSize"), rs.getString("fuelType"), rs.getString("colour"), MOTDate, serviceDate, rs.getDouble("mileage"));
                  all.add(x);
                System.out.print(rs.getString("colour"));
                
              //System.out.println("exists");
        }
        c.close();

        return all;
    }

    public ArrayList<BookList> getAllBook(String ID) throws SQLException {
        c = DatabaseConnector.activateConnection();
        c.setAutoCommit(false);
        st = c.createStatement();
        ArrayList<BookList> all = new ArrayList<>();

        rs = st.executeQuery("SELECT bookingID,date,cost,employeeID FROM 'Booking' WHERE vehicleRegistration=='" + ID + "'");
        while (rs.next()) {
            all.add(new BookList(rs.getInt("bookingID"), rs.getString("date"), rs.getInt("cost"), rs.getInt("employeeID")));
        }
        c.close();
        return all;
    }
    
    public int AddCust(Customer cc) throws SQLException {

        c = DatabaseConnector.activateConnection();
        //c.setAutoCommit( false );
        //"+cc.getCustomerID()+",
        st = c.createStatement();
        String query = "INSERT INTO Customer ( name,addressLine1,addressLine2,postCode,email,phoneNumber,type)"
                + "VALUES ('" + cc.getName() + "','" + cc.getAddressL1() + "','" + cc.getAddressL2() + "','" + cc.getPostCode() + "','" + cc.getEmail() + "','" + cc.getPhoneNo() + "','" + cc.getType() + "')";
        int confirm = st.executeUpdate(query);
        if (confirm != 0) {
            //System.out.println("Completed");
        } else {
            //System.out.println("Failed");
        }
        query="SELECT customerID FROM 'Customer' WHERE name=='"+cc.getName()+"';";
        rs=st.executeQuery(query);
        int ans= rs.getInt("customerID");
        c.close();
        return ans;
       
        
    }

    public void UpdateCust(Customer cc) throws SQLException {
        c = DatabaseConnector.activateConnection();
        st = c.createStatement();
        String query = "UPDATE 'Customer' SET name='" + cc.getName() + "',"
                + "addressLine1='" + cc.getAddressL1() + "',"
                + "addressLine2='" + cc.getAddressL2() + "',"
                + "postCode='" + cc.getPostCode() + "',"
                + "email='" + cc.getEmail() + "',"
                + "phoneNumber='" + cc.getPhoneNo() + "',"
                + "type='" + cc.getType() + "'"
                + "WHERE customerID=" + cc.getCustomerID();
        int confirm = st.executeUpdate(query);
        if (confirm != 0) {
            //System.out.println("Completed");
        } else {
            System.out.println("Failed");
        }
        c.close();
    }
    public ArrayList<PartList> getPart(int ID)throws SQLException
    {
         c = DatabaseConnector.activateConnection();
        c.setAutoCommit(false);
        st = c.createStatement();
        ArrayList<PartList> all = new ArrayList<>();
        String query="SELECT* FROM 'Parts' WHERE bookingID=="+ID+";";
        rs=st.executeQuery(query);
        while(rs.next())
        {
            all.add(new PartList(rs.getString("name"),rs.getString("description"),rs.getString("dateInstalled")));          
        }
        c.close();
        return all;
    }
    public ArrayList<BillList> getBill(int ID)throws SQLException
    {
         c = DatabaseConnector.activateConnection();
        c.setAutoCommit(false);
        st = c.createStatement();
        ArrayList<BillList> all = new ArrayList<>();
        String query="SELECT* FROM 'Bill' WHERE bookingID=="+ID+";";
        rs=st.executeQuery(query);
        while(rs.next())
        {
            all.add(new BillList(rs.getDouble("total"),rs.getString("status"),rs.getInt("billID")));          
        }
        c.close();
        return all;
    }
    public int getBillID(int ID)throws SQLException
    {
        c = DatabaseConnector.activateConnection();
        c.setAutoCommit(false);
        st = c.createStatement();
        String query="SELECT billID FROM 'Booking' WHERE  bookingID=="+ID+";";
        rs=st.executeQuery(query);
        
        int ans=rs.getInt("billID");
        c.close();
        return ans;
    }
    public ArrayList<Customer> getSearchCrit(String name) throws SQLException
    {
        c = DatabaseConnector.activateConnection();
        c.setAutoCommit(false);
        st = c.createStatement();
        String query="SELECT * FROM 'Customer' WHERE  name LIKE '%"+name+"%';";
        rs=st.executeQuery(query);
        ArrayList<Customer> a=new ArrayList<>();
        while(rs.next())
        {
            int newID = rs.getInt("customerID");
            String newName = rs.getString("name");
            String newAddr1 = rs.getString("addressLine1");
            String newAddr2 = rs.getString("addressLine2");
            //String newCity=rs.getString("City");
            String newPost = rs.getString("postCode");
            String newEmail = rs.getString("email");
            String newPhone = rs.getString("phoneNumber");
            String newtype = rs.getString("type");
            Customer cc = new Customer(newID, newName, newAddr1, newAddr2, newPost, newEmail, newPhone, newtype);
            a.add(cc);
            //System.out.println(cc.getName());
        }
        return a;
    }
    public ArrayList<Customer> getSearchCritBusiness(String name) throws SQLException
    {
        c = DatabaseConnector.activateConnection();
        c.setAutoCommit(false);
        st = c.createStatement();
        String query="SELECT * FROM 'Customer' WHERE  name LIKE '%"+name+"%' AND type=='Business';";
        rs=st.executeQuery(query);
        ArrayList<Customer> a=new ArrayList<>();
        while(rs.next())
        {
            int newID = rs.getInt("customerID");
            String newName = rs.getString("name");
            String newAddr1 = rs.getString("addressLine1");
            String newAddr2 = rs.getString("addressLine2");
            //String newCity=rs.getString("City");
            String newPost = rs.getString("postCode");
            String newEmail = rs.getString("email");
            String newPhone = rs.getString("phoneNumber");
            String newtype = rs.getString("type");
            Customer cc = new Customer(newID, newName, newAddr1, newAddr2, newPost, newEmail, newPhone, newtype);
            a.add(cc);
            //System.out.println(cc.getName());
        }
        return a;
    }
    public ArrayList<Customer> getSearchCritIndividual(String name) throws SQLException
    {
        c = DatabaseConnector.activateConnection();
        c.setAutoCommit(false);
        st = c.createStatement();
        String query="SELECT * FROM 'Customer' WHERE  name LIKE '%"+name+"%' AND type=='Individual';";
        rs=st.executeQuery(query);
        ArrayList<Customer> a=new ArrayList<>();
        while(rs.next())
        {
            int newID = rs.getInt("customerID");
            String newName = rs.getString("name");
            String newAddr1 = rs.getString("addressLine1");
            String newAddr2 = rs.getString("addressLine2");
            //String newCity=rs.getString("City");
            String newPost = rs.getString("postCode");
            String newEmail = rs.getString("email");
            String newPhone = rs.getString("phoneNumber");
            String newtype = rs.getString("type");
            Customer cc = new Customer(newID, newName, newAddr1, newAddr2, newPost, newEmail, newPhone, newtype);
            a.add(cc);
            //System.out.println(cc.getName());
        }
        return a;
    }

    public ArrayList<Customer> getSearchReg(String name) throws SQLException {
        c = DatabaseConnector.activateConnection();
        c.setAutoCommit(false);
        st = c.createStatement();
        ResultSet rst;
        Connection s=DatabaseConnector.activateConnection();
        ArrayList<Customer> a=new ArrayList<>();
        ArrayList<String> t=new ArrayList<>();
        String query="SELECT customerID FROM 'Vehicle' WHERE registration LIKE '%"+name+"%';";
        rs=st.executeQuery(query);
        while(rs.next())
        {
            
            query="SELECT * FROM 'Customer' WHERE customerID=='"+rs.getInt("customerID")+"';";
            //c.close();
            //c = DatabaseConnector.activateConnection();
            s.setAutoCommit(false);
            st = s.createStatement();
            
            rst=st.executeQuery(query);
            while(rst.next())
            {
                if(!t.contains(rst.getString("name")))
                {
                int newID = rst.getInt("customerID");
            String newName = rst.getString("name");
            String newAddr1 = rst.getString("addressLine1");
            String newAddr2 = rst.getString("addressLine2");
            //String newCity=rs.getString("City");
            String newPost = rst.getString("postCode");
            String newEmail = rst.getString("email");
            String newPhone = rst.getString("phoneNumber");
            String newtype = rst.getString("type");
                 Customer cc = new Customer(newID, newName, newAddr1, newAddr2, newPost, newEmail, newPhone, newtype);
                a.add(cc);
                t.add(newName);
                }
            }
        }
        return a;
    }
    public ArrayList<Customer> getSearchRegIndividual(String name) throws SQLException {
        c = DatabaseConnector.activateConnection();
        c.setAutoCommit(false);
        st = c.createStatement();
        ResultSet rst;
        Connection s=DatabaseConnector.activateConnection();
        ArrayList<Customer> a=new ArrayList<>();
        ArrayList<String> t=new ArrayList<>();
        String query="SELECT customerID FROM 'Vehicle' WHERE registration LIKE '%"+name+"%';";
        rs=st.executeQuery(query);
        while(rs.next())
        {
            
            query="SELECT * FROM 'Customer' WHERE customerID=='"+rs.getInt("customerID")+"' AND type=='Individual';";
            //c.close();
            //c = DatabaseConnector.activateConnection();
            s.setAutoCommit(false);
            st = s.createStatement();
            
            rst=st.executeQuery(query);
            while(rst.next())
            {
                if(!t.contains(rst.getString("name")))
                {
                int newID = rst.getInt("customerID");
            String newName = rst.getString("name");
            String newAddr1 = rst.getString("addressLine1");
            String newAddr2 = rst.getString("addressLine2");
            //String newCity=rs.getString("City");
            String newPost = rst.getString("postCode");
            String newEmail = rst.getString("email");
            String newPhone = rst.getString("phoneNumber");
            String newtype = rst.getString("type");
                 Customer cc = new Customer(newID, newName, newAddr1, newAddr2, newPost, newEmail, newPhone, newtype);
                a.add(cc);
                t.add(newName);
                }
            }
        }
        return a;
    }
     public ArrayList<Customer> getSearchRegBusiness(String name) throws SQLException {
        c = DatabaseConnector.activateConnection();
        c.setAutoCommit(false);
        st = c.createStatement();
        ResultSet rst;
        Connection s=DatabaseConnector.activateConnection();
        ArrayList<Customer> a=new ArrayList<>();
        ArrayList<String> t=new ArrayList<>();
        String query="SELECT customerID FROM 'Vehicle' WHERE registration LIKE '%"+name+"%' ;";
        rs=st.executeQuery(query);
        while(rs.next())
        {
            
            query="SELECT * FROM 'Customer' WHERE customerID=='"+rs.getInt("customerID")+"' AND type=='Business';";
            //c.close();
            //c = DatabaseConnector.activateConnection();
            s.setAutoCommit(false);
            st = s.createStatement();
            
            rst=st.executeQuery(query);
            while(rst.next())
            {
                if(!t.contains(rst.getString("name")))
                {
                int newID = rst.getInt("customerID");
            String newName = rst.getString("name");
            String newAddr1 = rst.getString("addressLine1");
            String newAddr2 = rst.getString("addressLine2");
            //String newCity=rs.getString("City");
            String newPost = rst.getString("postCode");
            String newEmail = rst.getString("email");
            String newPhone = rst.getString("phoneNumber");
            String newtype = rst.getString("type");
                 Customer cc = new Customer(newID, newName, newAddr1, newAddr2, newPost, newEmail, newPhone, newtype);
                a.add(cc);
                t.add(newName);
                }
            }
        }
        return a;
    }
}
