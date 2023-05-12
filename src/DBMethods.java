import java.sql.*;
import java.util.ArrayList;

public class DBMethods {
    private static DatabaseConnection dbConnection = new DatabaseConnection();
    private static Connection conn = dbConnection.getConnection();

    public static ArrayList<Order> fetchAllOrders() {
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

    public static ArrayList<Product> fetchAllProducts() {
        ArrayList<Product> products = new ArrayList<>();
        try {
            Statement stmt = conn.createStatement();
            ResultSet results = stmt.executeQuery("SELECT * FROM products"); //Write query
            while(results.next()) {
                int id = results.getInt("product_id");
                String productName = results.getString("name");
                int quantity = results.getInt("quantity");
                int volume = results.getInt("volume");
                int positionX = results.getInt("positionX");
                int positionY = results.getInt("positionY");
                products.add(new Product(id, productName, quantity, volume, positionX, positionY));
            }
        } catch(SQLException ex) {
            System.out.println("Creating query failed!");
            System.out.println(ex.getMessage());
        }
        return products;
    }

    public static ArrayList<Orderline> fetchOrderlines(Order order) {
        ArrayList<Orderline> orderlines = new ArrayList<>();
        try {
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM orderlines WHERE order_id = ?");
            stmt.setInt(1, order.getId());
            ResultSet results = stmt.executeQuery();
            while (results.next()) {
                int productId = results.getInt("product_id");
                int amount = results.getInt("amount");
                orderlines.add(new Orderline(amount, fetchProduct(productId)));
            }
        } catch(SQLException ex) {
            System.out.println("Creating query failed!");
            System.out.println(ex.getMessage());
        }
        return orderlines;
    }

    public static Product fetchProduct(int productId) {
        try {
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM products WHERE product_id = ?");
            stmt.setInt(1, productId);
            ResultSet results = stmt.executeQuery();
            while (results.next()) {
                int id = results.getInt("product_id");
                String productName = results.getString("name");
                int quantity = results.getInt("quantity");
                int volume = results.getInt("volume");
                int positionX = results.getInt("positionX");
                int positionY = results.getInt("positionY");
                return new Product(id, productName, quantity, volume, positionX, positionY);
            }
        } catch(SQLException ex) {
            System.out.println("Creating query failed!");
            System.out.println(ex.getMessage());
        }
        return null;
    }

    public static boolean addOrder(Order order) {
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

    public static boolean updateOrder(Order order, ArrayList<Orderline> orderlines) {
        try {
            PreparedStatement stmt = conn.prepareStatement("DELETE FROM orderlines WHERE order_id = ?");
            stmt.setInt(1, order.getId());
            stmt.executeUpdate();
            for (Orderline orderline : orderlines) {
                DBMethods.addOrderline(orderline, order.getId());
            }
            return true;
        } catch(SQLException ex) {
            System.out.println("Creating query failed!");
            System.out.println(ex.getMessage());
            return false;
        }
    }

    public static boolean addOrderline(Orderline orderline, int orderId) {
        try {
            PreparedStatement stmt = conn.prepareStatement("INSERT INTO orderlines (order_id, product_id, amount) VALUES (?, ?, ?)");
            stmt.setInt(1, orderId);
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

    public static int getLastOrderID() {
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
