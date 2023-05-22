import java.sql.*;
import java.util.ArrayList;

public class Order {
    private int id;
    private String name;
    private int customerID;
    private ArrayList<Orderline> orderlines;
    private ArrayList<Box> boxes;

    public Order() {
        orderlines = new ArrayList<Orderline>();
    }

    public Order(int id, String name, int customerID) {
        this.id = id;
        this.name = name;
        this.customerID = customerID;
        orderlines = new ArrayList<Orderline>();
    }

    public Order(int id) {
        this.id = id;
        orderlines = new ArrayList<Orderline>();
    }

    public ArrayList<Box> getBoxes() {
        return boxes;
    }

    public int getId() {
        return id;
    }

    public int getCustomer_ID() {
        return customerID;
    }

    public String getName() {
        return name;
    }

    public ArrayList<Orderline> getOrderlines() {
        return orderlines;
    }
    public Orderline getOrderline(int id){
        Orderline o = null;
        for (Orderline orderline: orderlines) {
            if (orderline.getProduct().getId() == id){
                 o = orderline;
                break;
            }
        }
        return o;
    }

    public void placeProductsInBoxes() {
        boxes = new ArrayList<>();
        ArrayList<Product> products = new ArrayList<>();
        for (Orderline orderline: orderlines) {
            products.add(orderline.getProduct());
        }
        boxes.add(new Box(products));
    }

    public void setOrderlines(ArrayList<Orderline> orderlines) {
        this.orderlines = orderlines;
    }

    public void addOrderline(Orderline orderline) {
        orderlines.add(orderline);
    }

    @Override
    public String toString() {
        String orderString = "Order: " + name + "\n";
        if (orderlines.size() == 0) {
        orderString += "No products in order";
        } else {
            for (Orderline orderline : orderlines) {
                orderString += orderline.toString() + "\n";
            }
        }
        return orderString;
    }
}
