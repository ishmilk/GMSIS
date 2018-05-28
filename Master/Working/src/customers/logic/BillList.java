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
public class BillList 
{
    private double cost;
    //private double paid;
    private String settled;
    private int ID;

    public BillList(double cost, String settled, int ID) {
        this.cost = cost;
        this.settled = settled;
        this.ID = ID;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    public String getSettled() {
        return settled;
    }

    public void setSettled(String settled) {
        this.settled = settled;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    
}
