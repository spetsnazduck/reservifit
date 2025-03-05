import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static final String URL = "jdbc:postgresql://ep-proud-cherry-a80jkr7w-pooler.eastus2.azure.neon.tech/reservifit?sslmode=require";
    private static final String USER = "reservifit_owner";
    private static final String PASSWORD = "npg_V8XkPJLYeWS2";

    public static Connection connect() {
        try {
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (SQLException e) {
            System.out.println("Connection failed: " + e.getMessage());
            return null;
        }
    }
}
