import java.sql.*;
import java.util.ArrayList;

public class Order {
    private int id;
    private String name;
    private ArrayList<Orderline> orderlines;

    public Order() {
        orderlines = new ArrayList<Orderline>();
    }

    public Order(int id, String name) {
        this.id = id;
        this.name = name;
        orderlines = new ArrayList<Orderline>();
    }

    public Order(int id) {
        this.id = id;
        orderlines = new ArrayList<Orderline>();
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public ArrayList<Orderline> getOrderlines() {
        return orderlines;
    }

    public void placeProductsInBoxes() {

    }

    public void setOrderlines(ArrayList<Orderline> orderlines) {
        this.orderlines = orderlines;
    }

    public void addOrderline(Orderline orderline) {
        orderlines.add(orderline);
    }
}
