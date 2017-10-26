import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBWarker {
    private static final String URL = "jdbc:mysql://localhost:3306/mylibrary";
    private static final String USER = "root";
    private static final String PASS = "root";
    private static Connection connection;

    static   {
        try {
            connection = DriverManager.getConnection(URL, USER, PASS);
            if (!connection.isClosed()) {
                System.out.println("connection is established");
            }

        } catch (SQLException e) {
            System.out.println("can't load class driver");
        }
    }

    public Connection getConnection() {
        return connection;
    }
}