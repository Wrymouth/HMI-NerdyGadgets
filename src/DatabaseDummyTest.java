//This file is only meant for testing the database connection
//and this file should not be added to the class diagram

import java.sql.*;

public class DatabaseDummyTest {
    DatabaseConnection dbConnection = new DatabaseConnection(); //Create connection object
    private Connection conn;
    private ResultSet results;

    public DatabaseDummyTest() {
        this.conn = dbConnection.getConnection(); //Get access to connection
        try {
            Statement stmt = conn.createStatement();
            this.results = stmt.executeQuery("SELECT * FROM products"); //Write query
            results.next(); //Moves result cursor
            System.out.println(results.getString("name")); //Returns name from column
        } catch(SQLException ex) {
            System.out.println("Creating query failed!");
            System.out.println(ex.getMessage());
        }
    }
}
