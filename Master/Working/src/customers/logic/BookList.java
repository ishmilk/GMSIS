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
public class BookList 
{
    private int bookID;
    private String date;
    private int cost;
    private int empID;

    public int getBookID() {
        return bookID;
    }

    public void setBookID(int bookID) {
        this.bookID = bookID;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getCost() {
        return cost;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }

    public int getEmpID() {
        return empID;
    }

    public void setEmpID(int empID) {
        this.empID = empID;
    }

    public BookList(int bookID, String date, int cost, int empID) {
        this.bookID = bookID;
        this.date = date;
        this.cost = cost;
        this.empID = empID;
    }

}