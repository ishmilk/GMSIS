/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package customers.logic;

/**
 *
 * @author P Sanjeev v
 */
public class Customer 
{
    int CustomerID;
    String Name;
    String addressL1;
    String addressL2;
    //String City;
    String PostCode;
    String PhoneNo;
    String Email;
    String Type;
    /**
     *
     * @param CustomerID
     * @param Name
     * @param addressL1
     * @param addressL2
     * @param City
     * @param PostCode
     * @param PhoneNo
     * @param Email
     * @param Type
     */
    
   
    
    public Customer(int CustomerID, String Name, String addressL1, String addressL2, String PostCode, String PhoneNo, String Email, String Type) {
        this.CustomerID = CustomerID;
        this.Name = Name;
        this.addressL1 = addressL1;
        this.addressL2 = addressL2;
        this.PostCode = PostCode;
        this.PhoneNo = PhoneNo;
        this.Email = Email;
        this.Type = Type;
    }

    public int getCustomerID() {
        return CustomerID;
    }

    public void setCustomerID(int CustomerID) {
        this.CustomerID = CustomerID;
    }

    public String getName() {
        return Name;
    }

    public void setName(String Name) {
        this.Name = Name;
    }

    public String getAddressL1() {
        return addressL1;
    }

    public void setAddressL1(String addressL1) {
        this.addressL1 = addressL1;
    }

    public String getAddressL2() {
        return addressL2;
    }

    public void setAddressL2(String addressL2) {
        this.addressL2 = addressL2;
    }

   

    public String getPostCode() {
        return PostCode;
    }

    public void setPostCode(String PostCode) {
        this.PostCode = PostCode;
    }

    public String getPhoneNo() {
        return PhoneNo;
    }

    public void setPhoneNo(String PhoneNo) {
        this.PhoneNo = PhoneNo;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String Email) {
        this.Email = Email;
    }

    public String getType() {
        return Type;
    }

    public void setType(String Type) {
        this.Type = Type;
    }

    
    
}
