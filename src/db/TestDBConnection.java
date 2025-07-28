package db;

import java.sql.Connection;

public class TestDBConnection {
    public static void main(String[] args) {
        Connection conn = DBConnection.getConnection();
        if (conn != null) {
            System.out.println("✅ Connected successfully!");
        } else {
            System.out.println("❌ Connection failed.");
        }
    }
}
