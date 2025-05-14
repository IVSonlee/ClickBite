import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

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

            // Create orders table
            stmt.execute("""
                        CREATE TABLE IF NOT EXISTS orders (
                        id INTEGER PRIMARY KEY AUTOINCREMENT,
                        customer_email TEXT NOT NULL,
                        item_name TEXT NOT NULL,
                        quantity INTEGER NOT NULL,
                            price REAL NOT NULL,
                        FOREIGN KEY (customer_email) REFERENCES users(email)
                        )
                    """);
             // Create contact_messages table       
            stmt.execute("""
                        CREATE TABLE IF NOT EXISTS contact_messages (
                        id INTEGER PRIMARY KEY AUTOINCREMENT, 
                        name TEXT NOT NULL, 
                        email TEXT NOT NULL, 
                        message TEXT NOT NULL, 
                        timestamp DATETIME DEFAULT CURRENT_TIMESTAMP
                    )""");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static int getUserCount() {
        String sql = "SELECT COUNT(*) AS total FROM users";
        try (Connection conn = getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql);
                ResultSet rs = pstmt.executeQuery()) {

            if (rs.next()) {
                return rs.getInt("total");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static Map<String, String> getAllContactMessages() {
        Map<String, String> messages = new LinkedHashMap<>();
        String sql = "SELECT name, message FROM contact_messages";
        try (Connection conn = getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                messages.put(rs.getString("name"), rs.getString("message"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return messages;
    }

    public static void saveContactMessage(String name, String email, String message) {
        String sql = "INSERT INTO contact_messages (name, email, message) VALUES (?, ?, ?)";
        try (Connection conn = getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, name);
            pstmt.setString(2, email);
            pstmt.setString(3, message);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void saveOrder(String customerEmail, String itemName, int quantity, double price) {
        String sql = "INSERT INTO orders (customer_email, item_name, quantity, price) VALUES (?, ?, ?, ?)";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, customerEmail);
            pstmt.setString(2, itemName);
            pstmt.setInt(3, quantity);
            pstmt.setDouble(4, price);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void removeOrderById(String orderId) {
        String sql = "DELETE FROM orders WHERE id = ?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, orderId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static java.util.List<Map<String, Object>> getOrdersByCustomerEmail(String email) {
        String sql = "SELECT id, item_name, quantity, price FROM orders WHERE customer_email = ?";
        java.util.List<Map<String, Object>> orders = new ArrayList<>();
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, email);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                Map<String, Object> order = new HashMap<>();
                order.put("id", rs.getInt("id"));
                order.put("item_name", rs.getString("item_name"));
                order.put("quantity", rs.getInt("quantity"));
                order.put("price", rs.getDouble("price"));
                orders.add(order);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return orders;
    }

}