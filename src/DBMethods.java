import java.sql.*;
import java.util.ArrayList;

public class DBMethods {
    private static DatabaseConnection dbConnection = new DatabaseConnection();
    private static Connection conn = dbConnection.getConnection();

    public static ArrayList<Order> dbFetchAllOrders() {
        ArrayList<Order> orders = new ArrayList<Order>();
        try {
            Statement stmt = conn.createStatement();
            ResultSet results = stmt.executeQuery("SELECT * FROM orders"); //Write query
            while (results.next()) {
                int orderId = results.getInt("order_id");
                String orderName = "Order " + orderId;
                orders.add(new Order(orderName));
            }
        } catch(SQLException ex) {
            System.out.println("Creating query failed!");
            System.out.println(ex.getMessage());
        }
        return orders;
    }
}
