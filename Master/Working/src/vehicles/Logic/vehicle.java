/*
 * Author: Amy Dowse
 */
package vehicles.Logic;

import java.time.LocalDate;
import java.util.Date;

public class vehicle 
{
    private boolean hasWarrenty;
    private final String classification;
    private final String registration;
    private  String make;
    private  String model;
    private final double engineSize;
    private final String fuel;
    private String colour;
    private LocalDate MOT;
    private LocalDate service;
    private double mileage;
    
    //constructor - creates a new vehicle object  
    public vehicle(boolean warrentyIn, String classificationIn, String regIn, String makeIn, String modelIn, double engineIn, String fuelIn, String colourIn, LocalDate MOTIn, LocalDate serviceIn, double mileageIn)
    {
        hasWarrenty = warrentyIn;
        classification = classificationIn;
        registration = regIn;
        make = makeIn;
        model = modelIn;
        engineSize = engineIn;
        fuel = fuelIn;
        colour = colourIn;
        MOT = MOTIn;
        service = serviceIn;
        mileage = mileageIn;
    }
    
     
  
    //GETTER METHODS
    
    public boolean getWarranty()
    {
        return hasWarrenty;
    }
    
    public String getClassification()
    {
        return classification;
    }
    
    public String getReg()
    {
        return registration;
    }
    
    public String getMake()
    {
        return make;
    }
    
    public String getModel()
    {
        return model;
    }
    
    public double getEngineSize()
    {
        return engineSize;
    }
    
    public String getFuel()
    {
        return fuel;
    }
    
    public String getColour()
    {
        return colour;
    }
    
   public LocalDate getMOT()
   {
       return MOT;
   }
    
   public LocalDate getService()
   {
       return service;
   }
    
    public double getMileage()
    {
        return mileage;
    }
     
     
     
     //SETTER METHODS 
    
    
    public void setMake(String newMake)
    {
        make = newMake;
    }
     
    public void setModel(String newModel)
    {
        model = newModel;
    }
    
    
    public void setMileage(double newMileage)
    {
        mileage = newMileage;
    }
    
    public void setMOT(LocalDate newMOT)
    {
        MOT = newMOT;
    }
    
    public void setService(LocalDate newService)
    {
        service = newService;
    }
    
    public void setColour(String newColour)
    {
        colour = newColour;
    }
    
    public void setWarranty(Boolean newWarrantyState)
    {
        hasWarrenty = newWarrantyState; 
    }
    
 
   
}
