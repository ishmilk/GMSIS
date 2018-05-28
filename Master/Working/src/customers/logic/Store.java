/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package customers.logic;

import vehicles.Logic.vehicle;

/**
 *
 * @author P Sanjeev v
 */
public class Store 
{
    static Customer c;
    private static vehicle v;

    public static void setC(Customer c) 
    {
        Store.c = c;
    }

    public static Customer getC() 
    {
        return c;
    }

    public static vehicle getV() {
        return v;
    }

    public static void setV(vehicle aV) {
        v = aV;
    }
    
}
