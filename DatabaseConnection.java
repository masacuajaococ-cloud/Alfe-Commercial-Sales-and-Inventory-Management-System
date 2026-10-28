import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {

    private static final String URL =
        "jdbc:mysql://localhost:3306/alfe_commercial";

    private static final String USER = "root";
    private static final String PASSWORD = "";

    // Used by Dashboard.java
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    // Used by AddProduct.java
    public Connection connect() throws SQLException {
        return getConnection();
    }
}
