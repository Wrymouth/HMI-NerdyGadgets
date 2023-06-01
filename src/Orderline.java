public class Orderline {
    private int amount;
    private Product product;
    private int PositionX;
    private int positionY;

    public Orderline(int amount, Product product) {
        this.amount = amount;
        this.product = product;
    }

    public Orderline(Product product) {
        this(1, product);
    }

    public Orderline() {

    }

    public Product getProduct() {
        return product;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        if (amount > 0) {
            this.amount = amount;
        }
    }

    public int getPositionX() {
        return PositionX;
    }

    public void setPositionX(int positionX) {
        PositionX = positionX;
    }

    public int getPositionY() {
        return positionY;
    }

    public void setPositionY(int positionY) {
        this.positionY = positionY;
    }

    public String toString() {
        return "Orderline: " + product.getName() + " " + amount + "x";
    }
}
