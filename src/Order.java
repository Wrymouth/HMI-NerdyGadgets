import java.sql.*;
import java.util.ArrayList;

public class Order {
    private ArrayList<Orderline> products;

    public ArrayList<Orderline> getProducts() {
        return products;
    }

    public void placeProductsInBoxes() {

    }

    public void dbFetchOrderProducts() {
        DatabaseConnection dbConnection = new DatabaseConnection();
        Connection conn = dbConnection.getConnection();
        try {
            Statement stmt = conn.createStatement();
            ResultSet results = stmt.executeQuery("SELECT * FROM orderlines WHERE order_id = 1"); //Write query
            results.next(); //Moves result cursor
            while (results.next()) {
                // add product to products
            }
        } catch(SQLException ex) {
            System.out.println("Creating query failed!");
            System.out.println(ex.getMessage());
        }
    }
}
