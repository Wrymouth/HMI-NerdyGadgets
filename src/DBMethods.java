import javax.swing.*;
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
                orders.add(new Order(orderId, orderName));
            }
        } catch(SQLException ex) {
            System.out.println("Creating query failed!");
            System.out.println(ex.getMessage());
        }
        return orders;
    }

    public static ArrayList<Product> dbFetchAllProducts() {
        ArrayList<Product> products = new ArrayList<>();
        try {
            Statement stmt = conn.createStatement();
            ResultSet results = stmt.executeQuery("SELECT * FROM products"); //Write query
            while(results.next()) {
                int id = results.getInt("product_id");
                String productName = results.getString("name");
                int quantity = results.getInt("quantity");
                products.add(new Product(id, productName, quantity));
            }
        } catch(SQLException ex) {
            System.out.println("Creating query failed!");
            System.out.println(ex.getMessage());
        }
        return products;
    }

    public static ArrayList<Orderline> dbFetchOrderLines(Order order) {
        ArrayList<Orderline> orderlines = new ArrayList<>();
        try {
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM orderlines WHERE order_id = ?");
            if(order.getId() != null) { //Check if the id id null
                stmt.setInt(1, order.getId());
                ResultSet results = stmt.executeQuery();
                while(results.next()) {
                    int productId = results.getInt("product_id");
                    int amount = results.getInt("amount");
                    orderlines.add(new Orderline(amount, dbFetchProduct(productId)));
                }
            } else {
                System.out.println("ID mag niet null zijn!");
            }
        } catch(SQLException ex) {
            System.out.println("Creating query failed!");
            System.out.println(ex.getMessage());
        }
        return orderlines;
    }

    public static Product dbFetchProduct(int productId) {
        try {
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM products WHERE product_id = ?");
            stmt.setInt(1, productId);
            ResultSet results = stmt.executeQuery();
            while (results.next()) {
                String productName = results.getString("name");
                int quantity = results.getInt("quantity");
                return new Product(productName, quantity);
            }
        } catch(SQLException ex) {
            System.out.println("Creating query failed!");
            System.out.println(ex.getMessage());
        }
        return null;
    }

    public static boolean dbAddOrder(Order order) {
        try {
            PreparedStatement stmt = conn.prepareStatement("INSERT INTO orders () VALUES ()");
            stmt.executeUpdate();
            return true;
        } catch(SQLException ex) {
            System.out.println("Creating query failed!");
            System.out.println(ex.getMessage());
            return false;
        }
    }

    public static boolean dbAddOrderline(Orderline orderline, Order order) {
        try {
            PreparedStatement stmt = conn.prepareStatement("INSERT INTO orderlines (order_id, product_id, amount) VALUES (?, ?, ?)");
            stmt.setInt(1, dbGetLastOrderID());
            stmt.setInt(2, orderline.getProduct().getId());
            stmt.setInt(3, orderline.getAmount());
            stmt.executeUpdate();
            return true;
        } catch(SQLException ex) {
            System.out.println("Creating query failed!");
            System.out.println(ex.getMessage());
            return false;
        }
    }

    public static int dbGetLastOrderID() {
        try {
            PreparedStatement stmt = conn.prepareStatement("SELECT order_id FROM orders ORDER BY order_id DESC");
            ResultSet results = stmt.executeQuery();
            results.next();
            return results.getInt("order_id");
        } catch (SQLException ex) {
            System.out.println("Executing query failed!");
            System.out.println(ex.getMessage());
            return 0;
        }
    }
}
