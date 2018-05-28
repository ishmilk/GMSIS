/*
 * DBQueries class
 * 
 * 
 */
package parts.logic;

import common.DatabaseConnector;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.SortedList;

import diagnosis_repairs.logic.*;
import java.util.Optional;
import javafx.scene.control.ButtonType;


public class DBQueries2 {
    
    public static void updateSTOCKPart(Part part) throws SQLException{
        
        ResultSet rs;
        try {
            Connection c = DatabaseConnector.activateConnection();
            c.setAutoCommit(true); 
            Statement stmt = c.createStatement(); 
            
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
      
            String query = "UPDATE 'Parts' SET " +  
                           "name='" +  part.getPartName()  +
                           "',description='" +  part.getPartDescr() +
                           "',cost='" +  part.getPartCost() +
                           "',dateInStock='" +  part.getDateInStockStr() +        
                           "' WHERE partID='" +  part.getPartID()+ "'";
                    //       "AND description= '" + part.getPartDescr() + "'";
            rs = stmt.executeQuery(query);  
        //    int updateCount = stmt.getUpdateCount();
        //   System.out.println(updateCount);
    
            
            rs.close();
            stmt.close();
            c.close();
        }
        catch (SQLException e) {
            // e.printStackTrace();
        }
    }
    
   
    public static ArrayList<DistinctPart> fetchAllDistinctParts(ArrayList<String> distinctList) throws SQLException {
        
        ArrayList<DistinctPart> dparts = new ArrayList<>();
        ResultSet rs;
         try {
            
            Connection c = DatabaseConnector.activateConnection();
             
             System.out.println(c.getClientInfo());
             
            c.setAutoCommit(true);   
            
            for (String dname : distinctList) {
                PreparedStatement stmt = c.prepareStatement("SELECT COUNT(*) AS total FROM Parts WHERE name = '" + dname + "'" +
                                                            " AND bookingID='0' AND dateInstalled IS NULL");
                rs = stmt.executeQuery();    
                while(rs.next()) { 
                    int quantity = rs.getInt("total");
                    if (quantity > 0) {
                        DistinctPart dp = new DistinctPart(dname,quantity);
                        dparts.add(dp);
                    }
                }
              
                stmt.close();
                rs.close();
            }
            c.close();
            
        }
    
        catch (SQLException e){
                  e.printStackTrace();
        }
  //       SortedList<DistinctPart> sortedParts = sortDPartsList(dparts);
         return dparts;
    }
     public static ArrayList<Part> fetchParts(String name) throws SQLException {
        
        ArrayList<Part> parts = new ArrayList<>();
        ResultSet rs;
         try {
            Connection c = DatabaseConnector.activateConnection();
            c.setAutoCommit(true);   
            
            
                PreparedStatement stmt = c.prepareStatement("SELECT * FROM Parts WHERE name = '" + name + "'" +
                                                            " AND bookingID='0' AND dateInstalled IS NULL");
                rs = stmt.executeQuery();    
         
                while(rs.next()) { 
                    int partID = rs.getInt("partID");
                    String partName = rs.getString("name");
                    String partDescr = rs.getString("description");
                    double partCost = rs.getDouble("cost");
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                    LocalDate dateInStock = LocalDate.parse(rs.getString("dateInStock"), formatter);
                    Part dp = new Part(partID, partName, partDescr, partCost, dateInStock);
                    parts.add(dp);
                }          
                stmt.close();
                rs.close();
                c.close();
        }
    
        catch (SQLException e){
                 // e.printStackTrace();
        }
   //     SortedList<Part> sortedParts = sortPartsList(parts);
        return parts;
    }
    
    public static SortedList<Part> fetchAllParts() {
        ObservableList<Part> parts = FXCollections.observableArrayList();
        ResultSet rs;
         try {
            Connection c = DatabaseConnector.activateConnection();
            c.setAutoCommit(true);           
            PreparedStatement stmt = c.prepareStatement("SELECT * FROM Parts");
            rs = stmt.executeQuery();    
         
            while(rs.next()) { 
                 int partID = rs.getInt("partID");
                 String partName = rs.getString("name");
                 String partDescr = rs.getString("description");
                 double partCost = rs.getDouble("cost");
                 int partBookingID = rs.getInt("bookingID");
//TODO:          String isReg = rs.getString("registeredTo");
                 
                 
                 DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
           //      formatter = formatter.withLocale(Locale.UK); 
                 
                 LocalDate dateInStock = LocalDate.parse(rs.getString("dateInStock"), formatter);

                 Part newPart = new Part(partID, partName, partDescr, partCost, dateInStock);
                 
                 if (rs.getObject("dateInstalled") != null && !rs.getString("dateInstalled").equals("") && !rs.getString("dateInstalled").equals("null")) {
                    LocalDate dateInstalled = LocalDate.parse(rs.getString("dateInstalled"), formatter);
                    newPart.setDateInstalled(dateInstalled);
                 }
                                
                 newPart.setPartBookingID(partBookingID);      
//TODO:          newPart.setRegisteredTo(isReg);
                 
                 parts.add(newPart);
                 
            }
           
            rs.close();
            stmt.close();
            c.close();
        }
    
         catch (SQLException e){
                 // e.printStackTrace();
         }
         SortedList<Part> sparts = sortPartsList(parts);
         return sparts;
    }
    
    public static ArrayList<Part> fetchUsedParts() {
        ArrayList<Part> parts = new ArrayList<>();
        ResultSet rs;
         try {
            Connection c = DatabaseConnector.activateConnection();
            c.setAutoCommit(true);           
            PreparedStatement stmt = c.prepareStatement("SELECT * FROM Parts WHERE dateInstalled IS NOT NULL");
            rs = stmt.executeQuery();    
         
            while(rs.next()) { 
                int partID = rs.getInt("partID");
                String partName = rs.getString("name");
                String partDescr = rs.getString("description");
                double partCost = rs.getDouble("cost");
                int partBookingID = rs.getInt("bookingID");
//TODO:         String isReg = rs.getString("registeredTo");

                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                LocalDate dateInStock = LocalDate.parse(rs.getString("dateInStock"), formatter);

                Part newPart = new Part(partID, partName, partDescr, partCost, dateInStock);
  
                
                LocalDate dateInstalled = LocalDate.parse(rs.getString("dateInstalled"), formatter);
                newPart.setDateInstalled(dateInstalled);
                parts.add(newPart);
                     
//TODO:          newPart.setRegisteredTo(isReg);             
            }          
            rs.close();
            stmt.close();
            c.close();
        }
    
         catch (SQLException e){
                 // e.printStackTrace();
         }
        
         return parts;
    }
    
    public static int assignID() {
        ResultSet rs;
        int maxID = 0;
        
        try {
            Connection c = DatabaseConnector.activateConnection();
            c.setAutoCommit(true); 
            Statement stmt = c.createStatement();  
            

            String query = "SELECT MAX(partID) AS partID FROM Parts";
            rs = stmt.executeQuery(query);  
            maxID = rs.getInt("partID");

            rs.close();
            stmt.close();
            c.close();
            
            
        }
         
        catch (SQLException e){
                 // e.printStackTrace();
        }
        
        return maxID;
    }
    
     public static int countParts(String name, String description) {
        ResultSet rs;
        int count = 0;
        
        try {
            Connection c = DatabaseConnector.activateConnection();
            c.setAutoCommit(true); 
            PreparedStatement stmt = c.prepareStatement("SELECT * from Parts where name = '" + name + "' AND " +
                                                        "description = '" + description + "'");
                                                              
            stmt.execute();
            rs = stmt.getResultSet();
            
            while (rs.next()) {
                count++;
            }

            stmt.close();
            c.close();
        }
        catch (SQLException e) {
             e.printStackTrace();
        }
        
        return count;
    }
     
    public static void linkPartToBooking(ArrayList<Part> parts, int bookingID, LocalDate bookingDate) {
        
         ResultSet rs;
         try {
            Connection c = DatabaseConnector.activateConnection();
            c.setAutoCommit(true);  
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            
            String bd = bookingDate.format(formatter);
            System.out.println(bd);
            for (Part part : parts) {
                PreparedStatement stmt = c.prepareStatement("UPDATE Parts SET bookingID='" +
                                                            bookingID + "', dateInstalled='" + bd +
                                                            "' WHERE partID='" + 
                                                            part.getPartID()+"'");
            stmt.executeUpdate(); 
            }
         }
        catch (SQLException e) {
             e.printStackTrace();
        }
    }
    
    public static void addPart(Part part) {
        
        ResultSet rs;
        try {
            Connection c = DatabaseConnector.activateConnection();
            c.setAutoCommit(true); 
            PreparedStatement stmt = c.prepareStatement("INSERT INTO Parts(partID,name,description,cost,dateInStock,dateInstalled,bookingID)" +
                                                              "VALUES(?,?,?,?,?,?,?)"); 
            stmt.setString(1, null);
            stmt.setString(2, part.getPartName());
            stmt.setString(3, part.getPartDescr());
            stmt.setDouble(4, part.getPartCost());
            
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
         
            stmt.setString(5, part.getDateInStockStr());           //.format(formatter).toString()
            
            if (part.getDateInstalled() != null) {
                stmt.setString(6, part.getDateInstalled().toString());
            } 
             
            stmt.setInt(7, part.getPartBookingID());

            stmt.executeUpdate();
            stmt.close();
            c.close();
        }
        catch (SQLException e) {
             e.printStackTrace();
        }
    }
    
    
    public static void removePart(int partID) {
        ResultSet rs;
        Optional<ButtonType> result = CodeBank.showConfirmationAlert("Delete part ?" , "Are you sure you want to delete this part ?");
        if ( result.get() == ButtonType.OK )
        {
        
            try {
            Connection c = DatabaseConnector.activateConnection();
            c.setAutoCommit(true); 
            
            //   String query = "DELETE FROM Parts WHERE partID =" + partID;
            PreparedStatement stmt = c.prepareStatement("DELETE FROM Parts WHERE partID =" + partID);
            stmt.execute();
            
           
            stmt.close();
            c.close();
            
            }
            catch (SQLException e) {
                 e.printStackTrace();
            }

        }
        else
        {
        
            // do nothing
        
        }
        
    }
    
    public static ArrayList<Part> getPartsThisBooking(int bookingID) throws SQLException {
        
            Connection c = common.DatabaseConnector.activateConnection();
            c.setAutoCommit(true); 
            Statement stmt = c.createStatement();
            ResultSet rs;
            rs = stmt.executeQuery("SELECT * FROM Parts WHERE bookingID = '" + bookingID +"'");
            ArrayList<Part> partsList = new ArrayList<>(); 
        
        while (rs.next()) {
            int partID = rs.getInt("partID");
            String name = rs.getString("name");
            String descr = rs.getString("description");
            double cost = rs.getDouble("cost");  
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            LocalDate dateInStock = LocalDate.parse(rs.getString("dateInStock"), formatter);
            Part part = new Part(partID,name,descr,cost,dateInStock);    
            partsList.add(part) ;        
        }
        
        c.close() ;
        return partsList;
    }
    
    public static void unlinkBookingFromPart(Part part) throws SQLException {
        Connection c = common.DatabaseConnector.activateConnection();
        c.setAutoCommit(true); 
        
        PreparedStatement stmt = c.prepareStatement("UPDATE Parts SET bookingID='" + 0 +
                                                            "' WHERE partID='" + 
                                                            part.getPartID()+"'");
        stmt.executeUpdate();
        stmt.close();
        c.close();
        
    }

    
    // *********  HELPER METHODS ************ //
        private static SortedList<DistinctPart> sortDPartsList(ObservableList<DistinctPart> parts) {
             SortedList<DistinctPart> sortedList = new SortedList<>( parts, 
                (DistinctPart pt1, DistinctPart pt2) -> {
                    return pt1.getPartName().compareTo(pt2.getPartName());   
                });
             return sortedList;
        }
        
        private static SortedList<Part> sortPartsList(ObservableList<Part> parts) {
             SortedList<Part> sortedList = new SortedList<>( parts, 
                (Part pt1, Part pt2) -> {
                    return pt1.getDateInStock().compareTo(pt2.getDateInStock());   
                });
             return sortedList;
        }
}