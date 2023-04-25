import java.util.ArrayList;

public class Box {
    private ArrayList<Product> products;

    public Box(ArrayList<Product> products) {
        this.products = products;
    }

    public ArrayList<Product> getProducts() {
        return products;
    }
}
