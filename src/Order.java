import java.sql.*;
import java.util.ArrayList;

public class Order {
    private int id;
    private String name;
    private ArrayList<Orderline> products;

    public Order(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public ArrayList<Orderline> getProducts() {
        return products;
    }

    public void placeProductsInBoxes() {

    }

    public void setProducts(ArrayList<Orderline> products) {
        this.products = products;
    }
}
