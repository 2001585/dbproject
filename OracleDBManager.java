import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class OracleDBManager {
    private static final String ORACLE_URL = "jdbc:oracle:thin:@localhost:1521:xe";
    private static final String USERNAME = "system";
    private static final String PASSWORD = "1234";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(ORACLE_URL, USERNAME, PASSWORD);
    }
}
