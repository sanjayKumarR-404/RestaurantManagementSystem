package db;

import java.sql.Connection;

public class TestDBConnection {
    public static void main(String[] args) {
        try {
            Connection conn = DBConnection.getConnection();
            if (conn != null) {
                System.out.println("✅ Connected successfully!");
            } else {
                System.out.println("❌ Connection failed.");
            }
        } catch (Exception e) {
            System.out.println("❌ Exception while connecting: " + e.getMessage());
            e.printStackTrace(); // optional, for debugging
        }
    }
}
