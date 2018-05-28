/*
 * Author: Amy Dowse
 * Warranty class 
 */
package vehicles.Logic;

import java.time.* ;

public class Warranty 
{
    
    private LocalDate date;
    private String companyName;
    private String companyAddressLine1;
    private String companyAddressLine2;
    private String companyAddressPostCode;
    private String vehicleRegistration;
    
    public Warranty()
    {
    
        /* DEFAULT CONSTRUCTOR */
    
    }
    
    public Warranty( LocalDate date , String companyName , String companyAddressLine1 , String companyAddressLine2 , String companyAddressPostCode , String vehicleRegistration )
    {
    
        this.date = date ;
        this.companyName = companyName ;
        this.companyAddressLine1 = companyAddressLine1 ;
        this.companyAddressLine2 = companyAddressLine2 ;
        this.companyAddressPostCode = companyAddressPostCode ;
        this.vehicleRegistration = vehicleRegistration ;
    
    }
    
    //GETTERS
    public LocalDate getDate()
    {
        return date;
    }
    
    public String getCompanyName()
    {
        return companyName;
    }
    
    public String getCompanyAddressLine1()
    {
        return companyAddressLine1;
    }
    
    public String getCompanyAddressLine2()
    {
        return companyAddressLine2;
    }
    
    public String getCompanyAddressPostCode()
    {
        return companyAddressPostCode;
    }
    
    public String getVehicleRegistration()
    {
        return vehicleRegistration;
    }
    
    
    
    //SETTERS
    public void setDate(LocalDate dateIn)
    {
        date = dateIn;
    }
    
    public void setCompanyName(String newName)
    {
        companyName = newName;
    }
    
    public void setCompanyAddressLine1(String newLine1)
    {
        companyAddressLine1 = newLine1;
    }
       
    public void setCompanyAddressLine2(String newLine2)
    {
        companyAddressLine2 = newLine2;
    }  
    
    public void setCompanyAddressPostCode(String newPostCode)
    {
        companyAddressPostCode = newPostCode;
    }
    
    public void setVehicleRegistration(String newReg)
    {
        vehicleRegistration = newReg;
    }
            
}
