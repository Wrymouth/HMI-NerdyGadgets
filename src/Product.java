public class Product {
    private int id;
    private String name;
    private int quantity;
    
    public Product(String name, int quantity) {
        this.name = name;
        this.quantity = quantity;
    }

    public int getId() {
        return id;
    }
    
    public String getName() {
        return name;
    }

    public int getQuantity() {
        return quantity;
    }
}
