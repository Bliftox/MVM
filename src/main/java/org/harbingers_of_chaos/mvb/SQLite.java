package org.harbingers_of_chaos.mvb;

import org.harbingers_of_chaos.mvb.config.cfg;
import org.harbingers_of_chaos.mvm.Config;

import java.sql.*;



/*
 *
 * Many thanks to the developer of this database, without it I would have suffered and continued to play with crutches.
 * I'll leave this here <3
 * https://github.com/anzuath/DeathNote/blob/main/DeathNoteInternals/src/main/java/com/github/steeldev/deathnote/util/Database.java
 *
 */

public class SQLite {
    static Connection dbConnection;

    public static void getConnection() throws SQLException {
        dbConnection = DriverManager.getConnection(Config.INSTANCE.sqLite.url, Config.INSTANCE.sqLite.user,Config.INSTANCE.sqLite.password);
    }

    public static void closeConnection() throws SQLException {
        dbConnection.close();
    }

    public static void createDB() throws SQLException {
        Statement statement = dbConnection.createStatement();
        statement.setQueryTimeout(30);

        statement.executeUpdate("CREATE TABLE IF NOT EXISTS players (application_Int INTEGER, username TEXT, user_id TEXT, years INTEGER, sex INTEGER, bio TEXT, why_we TEXT)");
    }

    public static void addPlayer(String name) throws SQLException {
        Statement statement = dbConnection.createStatement();
        statement.setQueryTimeout(30);

        statement.executeUpdate(String.format("INSERT INTO players VALUES('%s', 0)", name));
    }

    public static void updatePlayerData(String name, long unix) throws SQLException {
        Statement statement = dbConnection.createStatement();
        statement.setQueryTimeout(30);
        statement.executeUpdate(String.format("UPDATE players SET unix = %d WHERE name = '%s'", unix, name));
    }

    public static long getPlayerUnix(String name) throws SQLException {
        Statement statement = dbConnection.createStatement();
        statement.setQueryTimeout(30);
        ResultSet rs = statement.executeQuery(String.format("SELECT * FROM players WHERE name = '%s'", name));
        long data = 0;
        while (rs.next())
            data = rs.getLong("unix");
        return data;
    }

    public static int getPlayerIsRegistered(String name) throws SQLException {
        Statement statement = dbConnection.createStatement();
        statement.setQueryTimeout(30);
        ResultSet rs = statement.executeQuery(String.format("SELECT * FROM players WHERE name = '%s'", name));
        String data = "";
        while (rs.next())
            data = rs.getString("name");

        if (data == null) {
            return 0;
        } else if (!data.equals(name)) {
            return 0;
        }
        return 1;
    }
}