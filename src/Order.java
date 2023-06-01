import java.sql.*;
import java.util.ArrayList;

public class Order {
    private int id;
    private String name;
    private int customerID;
    private ArrayList<Orderline> orderlines;
    private ArrayList<Box> boxes;
    private boolean processed;
    private int PositionX;
    private int PositionY;

    public Order() {
        orderlines = new ArrayList<Orderline>();
    }

    public Order(int id, String name, int customerID) {
        this.id = id;
        this.name = name;
        this.customerID = customerID;
        this.processed = false;
        orderlines = new ArrayList<Orderline>();
    }

    public Order(int customerID) {
        this.customerID = customerID;
        orderlines = new ArrayList<Orderline>();
    }

    public ArrayList<Box> getBoxes() {
        return boxes;
    }

    public int getId() {
        return id;
    }

    public int getCustomerID() {
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

    public boolean isProcessed() {
        return processed;
    }

    public void setProcessed(boolean processed) {
        this.processed = processed;
        DBMethods.updateOrder(this, this.orderlines);
    }

    public void placeProductsInBoxes() {
        // Create arraylist for holding all the products
        ArrayList<Product> products = new ArrayList<>();

        // Iterate through all order lines and place the product in the arraylist; do this for the ordered quantity
        for (Orderline orderline: this.orderlines) {
            for (int i = 0; i < orderline.getAmount(); i++) {
                products.add(orderline.getProduct());
            }
        }

        // Create an instance of the BinPacking class and let it place the products in boxes
        BinPacking packing = new BinPacking();
        this.boxes = packing.binPacking(products);
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

    public int getPositionX() {
        return PositionX;
    }

    public void setPositionX(int positionX) {
        PositionX = positionX;
    }

    public int getPositionY() {
        return PositionY;
    }

    public void setPositionY(int positionY) {
        PositionY = positionY;
    }
}
