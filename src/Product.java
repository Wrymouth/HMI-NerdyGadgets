public class Product {
    private int id;
    private String name;
    private int quantity;
    private int volume;
    private int positionX;
    private int positionY;


    public Product(int id, String name, int quantity, int volume, int positionX, int positionY) {
        this.id = id;
        this.name = name;
        this.quantity = quantity;
        this.volume = volume;
        this.positionX = positionX;
        this.positionY = positionY;
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

    public int getPositionX() {
        return positionX;
    }

    public int getPositionY() {
        return positionY;
    }
}
