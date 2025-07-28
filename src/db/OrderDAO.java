package db;

import gui.MenuItem;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OrderDAO {

    // Use DBConnection.getConnection() throughout

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

    public static void addMenuItem(MenuItem item) {
        String sql = "INSERT INTO menu (item_name, price) VALUES (?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, item.getName());
            stmt.setDouble(2, item.getPrice());
            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void deleteMenuItem(String name) {
        String sql = "DELETE FROM menu WHERE item_name = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, name);
            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void insertOrder(String itemName, int qty, double itemTotal) {
        String sql = "INSERT INTO orders (item_name, quantity, item_total) VALUES (?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, itemName);
            stmt.setInt(2, qty);
            stmt.setDouble(3, itemTotal);
            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void clearOrdersTable() {
        String sql = "DELETE FROM orders";
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
