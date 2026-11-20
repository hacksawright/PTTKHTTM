package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DAO {
    public DAO() {}

    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new ExceptionInInitializerError(e);
        }
    }

    protected Connection openConnection() throws SQLException {
        String url = env("SHOPMAN_DB_URL", "jdbc:mysql://localhost:3306/pttkht?characterEncoding=UTF-8&connectionTimeZone=Asia/Ho_Chi_Minh");
        return DriverManager.getConnection(url, env("SHOPMAN_DB_USER", "root"), env("SHOPMAN_DB_PASSWORD", "1234"));
    }

    private static String env(String key, String fallback) {
        String value = System.getenv(key);
        return value == null ? fallback : value;
    }
}
