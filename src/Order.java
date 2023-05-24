import java.sql.*;
import java.util.ArrayList;

public class Order {
    private int id;
    private String name;
    private ArrayList<Orderline> orderlines;
    public int ordercounter;

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

    public int getId() {
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
    public void removeOrderline(Orderline orderline){orderlines.remove(orderline);}

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
    public void CompleteOrder(){
        if(orderlines.size() == ordercounter){
            System.out.println("Order Complete");
            //code for robot to move to starting point:

        }
    }
}
