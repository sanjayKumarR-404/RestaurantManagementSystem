package db;

import gui.MenuItem;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OrderDAO {

    private static final String URL = "jdbc:mysql://localhost:3306/restaurant_db";
    private static final String USER = "root";       // or your DB user
    private static final String PASSWORD = "Sa20082005!"; // update if needed

    private static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    // OrderDAO.java
    public static List<MenuItem> getAllMenuItems() {
        List<MenuItem> itemList = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT item_name, price FROM menu")) {

            while (rs.next()) {
                String name = rs.getString("item_name");
                double price = rs.getDouble("price");
                itemList.add(new MenuItem(name, price));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return itemList;
    }


    // Add a new menu item
    public static void addMenuItem(MenuItem item) {
        String sql = "INSERT INTO menu (item_name, price) VALUES (?, ?)";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, item.getName());
            stmt.setDouble(2, item.getPrice());
            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Delete a menu item by name
    public static void deleteMenuItem(String name) {
        String sql = "DELETE FROM menu WHERE item_name = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, name);
            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Insert an order into the orders table
    public static void insertOrder(String itemName, int qty, double itemTotal) {
        String sql = "INSERT INTO orders (item_name, quantity, item_total) VALUES (?, ?, ?)";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, itemName);
            stmt.setInt(2, qty);
            stmt.setDouble(3, itemTotal);
            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    // Clear all orders from the orders table
    public static void clearOrdersTable() {
        String sql = "DELETE FROM orders";
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
