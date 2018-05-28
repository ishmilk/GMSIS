/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package customers.logic;

import java.sql.SQLException;
import java.util.ArrayList;
/**
 *
 * @author P Sanjeev v
 */
public class Checker 
{
    public static void main(String args[]) throws SQLException
    {
        DataFunc dd;
        dd = new DataFunc();
        /*
        Customer cc=dd.GetByID(-7);
        System.out.println(" Customer ID : "+cc.getCustomerID()+"\n"+
                           "Name : " + cc.getName() + "\n" +
                           "Address 1 : "+cc.getAddressL1() +"\n"+
                           "Address 2 : "+cc.getAddressL2()+"\n"+
                           "PostCode : "+cc.getPostCode()+"\n"+
                           "Email : "+cc.getEmail()+"\n"+
                           "Phone : "+cc.getPhoneNo()+"\n");*/
//        ArrayList <CustList> a=dd.GetAllCust();
//        for(CustList b:a)
//        {
//            System.out.println (b.getId()+"   "+ b.getName()+"   "+b.getPhone());
//        }
    }
}
