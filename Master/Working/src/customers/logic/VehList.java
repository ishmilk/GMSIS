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
public class VehList 
{
    private String Reg;
    private String make;
    private String model;
    private String fuel;
    private String type;

    public VehList(String Reg, String make, String model, String fuel, String type) {
        this.Reg = Reg;
        this.make = make;
        this.model = model;
        this.fuel = fuel;
        this.type = type;
    }

    public String getReg() {
        return Reg;
    }

    public void setReg(String Reg) {
        this.Reg = Reg;
    }

    public String getMake() {
        return make;
    }

    public void setMake(String make) {
        this.make = make;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getFuel() {
        return fuel;
    }

    public void setFuel(String fuel) {
        this.fuel = fuel;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

}