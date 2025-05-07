import java.sql.*;

public class DatabaseManager {
    private static final String DB_URL = "jdbc:sqlite:clickbite.db";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL);
    }

    public static void initializeDatabase() {
        try (Connection conn = getConnection(); Statement stmt = conn.createStatement()) {
            String createUsersTable = """
                    CREATE TABLE IF NOT EXISTS users (
                        email TEXT PRIMARY KEY,
                        fullName TEXT NOT NULL,
                        username TEXT NOT NULL,
                        street TEXT,
                        city TEXT,
                        postal TEXT,
                        password TEXT NOT NULL
                    );
                    """;

            stmt.execute(createUsersTable);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
