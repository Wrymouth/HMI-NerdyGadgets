import java.sql.*;
public class DatabaseConnection {
    private static String url = "jdbc:mysql://localhost:3306/hmi";
    private static String driverName = "com.mysql.cj.jdbc.Driver";
    private static String username = "root";
    private static String password = "";
    private static Connection conn;

    public Connection getConnection() {
        try {
            Class.forName(driverName);
            try {
                conn = DriverManager.getConnection(url, username, password);
            } catch (SQLException ex) {
                // log an exception. fro example:
                System.out.println("Failed to create the database connection.");
                System.out.println(ex.getMessage());
            }
        } catch (ClassNotFoundException ex) {
            // log an exception. for example:
            System.out.println("Driver not found.");
            System.out.println(ex.getMessage());
        }
        return conn;
    }
}
