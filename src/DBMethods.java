import java.sql.*;
import java.util.ArrayList;

public class DBMethods {
    private static DatabaseConnection dbConnection = new DatabaseConnection();
    private static Connection conn = dbConnection.getConnection();

    public static ArrayList<Order> fetchUnprocessedOrders() {
        ArrayList<Order> orders = new ArrayList<Order>();
        try {
            Statement stmt = conn.createStatement();
            ResultSet results = stmt.executeQuery("SELECT * FROM orders WHERE processed = 0"); // Write query
            while (results.next()) {
                int orderId = results.getInt("order_id");
                String orderName = "Order " + orderId;
                int customerID = results.getInt("customerID");
                orders.add(new Order(orderId, orderName, customerID));
            }
        } catch (SQLException ex) {
            System.out.println("Creating query failed!");
            System.out.println(ex.getMessage());
        }
        return orders;
    }

    public static ArrayList<Product> fetchAllProducts() {
        ArrayList<Product> products = new ArrayList<>();
        try {
            Statement stmt = conn.createStatement();
            ResultSet results = stmt.executeQuery("SELECT * FROM products"); // Write query
            while (results.next()) {
                int id = results.getInt("product_id");
                String productName = results.getString("name");
                int quantity = results.getInt("quantity");
                int volume = results.getInt("volume");
                int positionX = results.getInt("positionX");
                int positionY = results.getInt("positionY");
                products.add(new Product(id, productName, quantity, volume, positionX, positionY));
            }
        } catch (SQLException ex) {
            System.out.println("Creating query failed!");
            System.out.println(ex.getMessage());
        }
        return products;
    }

    //fetches products that are still in stock
    public static ArrayList<Product> fetchProductsInStorage() {
        ArrayList<Product> products = new ArrayList<>();
        try {
            Statement stmt = conn.createStatement();
            ResultSet results = stmt.executeQuery("SELECT * FROM products WHERE quantity > 0"); // Write query
            while (results.next()) {
                int id = results.getInt("product_id");
                String productName = results.getString("name");
                int quantity = results.getInt("quantity");
                int volume = results.getInt("volume");
                int positionX = results.getInt("positionX");
                int positionY = results.getInt("positionY");
                products.add(new Product(id, productName, quantity, volume, positionX, positionY));
            }
        } catch (SQLException ex) {
            System.out.println("Creating query failed!");
            System.out.println(ex.getMessage());
        }
        return products;
    }
    public static ArrayList<Customer> fetchAllCustomers() {
        ArrayList<Customer> customers = new ArrayList<>();
        try {
            Statement stmt = conn.createStatement();
            ResultSet results = stmt.executeQuery("SELECT * FROM customers"); // Write query
            while (results.next()) {
                int customerID = results.getInt("CustomerID");
                String name = results.getString("Name");
                String address = results.getString("Address");
                String ZIPcode = results.getString("ZIP-code");
                String city = results.getString("City");
                customers.add(new Customer(customerID, name, address, ZIPcode, city));
            }
        } catch (SQLException e) {
            System.out.println("Creating query failed!");
            System.out.println(e.getMessage());
        }
        return customers;
    }

    public static ArrayList<Orderline> fetchOrderlines(Order order) {
        ArrayList<Orderline> orderlines = new ArrayList<>();
        try {
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM orderlines WHERE order_id = ?");
            if (order.getId() != 0) { // Check if the id is null
                stmt.setInt(1, order.getId());
                ResultSet results = stmt.executeQuery();
                while (results.next()) {
                    int productId = results.getInt("product_id");
                    int amount = results.getInt("amount");
                    orderlines.add(new Orderline(amount, fetchProduct(productId)));
                }
            } else {
                System.out.println("ID mag niet null zijn!");
            }
        } catch (SQLException ex) {
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
        } catch (SQLException ex) {
            System.out.println("Creating query failed!");
            System.out.println(ex.getMessage());
        }
        return null;
    }

    public static Customer fetchCustomer(int ID) {
        try {
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM customers WHERE customerID = ?");
            stmt.setInt(1, ID);
            ResultSet results = stmt.executeQuery();
            while(results.next()) {
                int customerID = results.getInt("CustomerID");
                String name = results.getString("Name");
                String address = results.getString("Address");
                String ZIPcode = results.getString("ZIP-code");
                String city = results.getString("City");
                return new Customer(customerID, name, address, ZIPcode, city);
            }
        } catch(SQLException e) {
            System.out.println("Creating query failed!");
            System.out.println(e.getMessage());
        }
        return null;
    }

    public static Customer fetchCustomerByName(String selectedName) {
        try {
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM customers WHERE Name = ?");
            stmt.setString(1, selectedName);
            ResultSet results = stmt.executeQuery();
            while(results.next()) {
                int customerID = results.getInt("CustomerID");
                String name = results.getString("Name");
                String address = results.getString("Address");
                String ZIPcode = results.getString("ZIP-code");
                String city = results.getString("City");
                return new Customer(customerID, name, address, ZIPcode, city);
            }
        } catch(SQLException e) {
            System.out.println("Creating query failed!");
            System.out.println(e.getMessage());
        }
        return null;
    }

    public static boolean addOrder(Order order) {
        try {
            PreparedStatement stmt = conn.prepareStatement("INSERT INTO orders (CustomerID) VALUES (?)");
            stmt.setInt(1, order.getCustomerID());
            stmt.executeUpdate();
            return true;
        } catch (SQLException ex) {
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
        } catch (SQLException ex) {
            System.out.println("Creating query failed!");
            System.out.println(ex.getMessage());
            return false;
        }
    }

    public static boolean addOrderline(Orderline orderline, int orderID) {
        try {
            PreparedStatement stmt = conn
                    .prepareStatement("INSERT INTO orderlines (order_id, product_id, amount) VALUES (?, ?, ?)");
            if (orderID == 0) {
                stmt.setInt(1, getLastOrderID());
            } else {
                stmt.setInt(1, orderID);
            }
            stmt.setInt(2, orderline.getProduct().getId());
            stmt.setInt(3, orderline.getAmount());
            stmt.executeUpdate();
            return true;
        } catch (SQLException ex) {
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

    public static boolean deleteOrder(int id){
        try {
            PreparedStatement stmt = conn.prepareStatement("DELETE FROM orderlines WHERE order_id = ?");
            stmt.setInt(1, id);
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("executing query failed!");
            System.out.println(e.getMessage());
            return false;
        }
    }
}
