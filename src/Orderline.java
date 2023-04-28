public class Orderline {
    private int amount;
    private Product product;

    public Orderline(int amount, Product product) {
        this.amount = amount;
        this.product = product;
    }

    public Orderline(Product product) {
        this(1, product);
    }

    public Product getProduct() {
        return product;
    }

    public int getAmount() {
        return amount;
    }
}
