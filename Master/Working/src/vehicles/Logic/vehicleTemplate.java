/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vehicles.Logic;

/**
 *
 * @author Amy
 * A class for the vehicle templates that are taken from the database 
 */
public class vehicleTemplate 
{
    private String make;
    private String model;
    private double engine;
    private String fuel;
    
    public vehicleTemplate(String make, String model, double engine, String fuel)
    {
        this.make = make;
        this.model = model;
        this.engine = engine;
        this.fuel = fuel;
    }
    
    public String showTemplate()
    {
        return (make  + ", " + model + ", " + engine + ", " + fuel);
    }
}
