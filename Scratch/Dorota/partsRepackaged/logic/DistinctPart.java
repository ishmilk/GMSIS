/*

 */
package parts.logic;


public class DistinctPart {
   
    private int quantity;
    private String partName;
    private boolean wasSelected;
    
    
    
    public DistinctPart(String name, int quantity) {
        this.partName = name;
        this.quantity = quantity;   
    }

    
    public int getQuantity() {
        return quantity;
    }

    public String getPartName() {
        return partName;
    }
    
    public void removeOne() {
        this.quantity--;
    }
    
    public void addOne() {
        this.quantity++;
    }

}
