package org.harbingers_of_chaos.mvb;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import static org.harbingers_of_chaos.mvb.Main.*;
import static org.harbingers_of_chaos.mvb.config.cfg.*;

public class DatabaseManager {
    private static Connection connection;

    public static void connect() {
        try {
            Class.forName("db1.apexnodes.xyz:3306");
            connection = DriverManager.getConnection(url, user, password);
            log.info("Connected to the database");
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    public static void disconnect() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                log.info("Disconnected from the database");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static Connection getConnection() {
        return connection;
    }
}
