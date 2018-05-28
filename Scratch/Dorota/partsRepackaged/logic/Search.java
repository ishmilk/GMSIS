/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package parts.logic;

public class Search 
{
    private String customerName;
    private String registration;
    private int customerID;
    
    public Search(String name, String reg, int ID) {
        customerName = name;
        registration = reg;
        customerID = ID;
    }
    
    public void setName(String newName) {
        customerName = newName;
    }
    
    public void setRegistration(String newReg) {
        registration = newReg;
    }
    
    public void setID(int newID) {
        customerID = newID;
    }
    
    public String getName() {
        return customerName;
    }
    
    public String getReg() {
        return registration;
    }
    
    public int getID() {
        return customerID;
    }
            
}
