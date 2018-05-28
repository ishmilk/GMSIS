/*
 * Part class
 * 
 * 
 */
package parts.logic;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import static java.time.temporal.ChronoUnit.DAYS;
import java.util.Locale;

                 
         //        LocalDate dateInStock = LocalDate.parse(rs.getString("dateInStock"), formatter);


public class Part {
    
    private int partID;
    private String partName;
    private String partDescr;
    private double partCost;
    private boolean warrantyExpired;
    
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private LocalDate dateInStock;
    private String dateInStockStr;
    private LocalDate dateInstalled;
    
    private int partBookingID;
    private String registeredTo;

    

    
    public Part(int ID, String name, String description, double cost, LocalDate dateInStock) {
      
        
        this.partID = ID;
        this.partName = name;
        this.partDescr = description;
        this.partCost = cost;
        this.warrantyExpired = false;
        
        this.dateInStockStr = dateInStock.format(formatter);
        this.dateInStock = LocalDate.parse(dateInStockStr, formatter);
        this.dateInstalled = null;
        this.partBookingID = 0;
        this.registeredTo = "";
   
    }

    
    public void searchByReg(String reg) {
        
        
    }
    
    public void searchByName(String name) {
        
        
    }

    public void setPartName(String partName) {
        this.partName = partName;
    }

    public void setPartDescr(String partDescr) {
        this.partDescr = partDescr;
    }

    public void setPartID(int partID) {
        this.partID = partID;
    }

    public void setDateInstalled(LocalDate dateInstalled) {
        this.dateInstalled = dateInstalled;
        if (dateInstalled != null) {
            this.warrantyExpired = checkWarranty();
        }
    }

    public void setDateInStock(LocalDate dateInStock) {
        this.dateInStock = dateInStock;      
    }

    private void setWarrantyExpired() {
        this.warrantyExpired = checkWarranty();
    }
    
    public void setPartCost(double partCost) {
        this.partCost = partCost;
    }
    
    public void setPartBookingID(int partBookingID) {
        this.partBookingID = partBookingID;
    }
    
    public void setRegisteredTo(String registeredTo) {
        this.registeredTo = registeredTo;
    }
    
    
    public String getPartName() {
        return partName;
    }

    public String getPartDescr() {
        return partDescr;
    }

    public int getPartID() {
        return partID;
    }

    public LocalDate getDateInstalled() {
        return dateInstalled;
    }

    public String getDateInStockStr() {
        return dateInStockStr;
    }
    
    public LocalDate getDateInStock() {
        return dateInStock;
    }

    public double getPartCost() {
        return partCost;
    }

    public boolean getWarrantyExpired() {
        return warrantyExpired;
    }
    
     public int getPartBookingID() {
        return partBookingID;
    }
     
    public String getRegisteredTo() {
        return registeredTo;
    }
    
    // ********* HELPER METHODS *********** //
     private boolean checkWarranty() {
         
        LocalDate currentDate = LocalDate.now();
        long daysBetween = DAYS.between(currentDate, this.dateInstalled);
        if (currentDate.isLeapYear() && daysBetween > 365) {           
            return false;
        }
        else if (!currentDate.isLeapYear() && daysBetween > 364){
            return false;
        }
        else { return true; }
    }
     
     
}