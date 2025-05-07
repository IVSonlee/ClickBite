import java.sql.*;

public class DatabaseManager {
    private static final String DB_URL = "jdbc:sqlite:clickbite.db";

    static {
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("SQLite JDBC driver not found", e);
        }
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL);
    }

    public static void initializeDatabase() {
        try (Connection conn = getConnection();
                Statement stmt = conn.createStatement()) {

            // Create users table
            stmt.execute("""
                    CREATE TABLE IF NOT EXISTS users (
                        email TEXT PRIMARY KEY,
                        fullName TEXT NOT NULL,
                        username TEXT NOT NULL,
                        street TEXT,
                        city TEXT,
                        postal TEXT,
                        password TEXT NOT NULL
                    )""");

            // Create admins table
            stmt.execute("""
                    CREATE TABLE IF NOT EXISTS admins (
                        email TEXT PRIMARY KEY,
                        fullName TEXT NOT NULL,
                        username TEXT NOT NULL,
                        role TEXT NOT NULL,
                        password TEXT NOT NULL
                    )""");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}