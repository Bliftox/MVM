package org.harbingers_of_chaos.mvb;

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
        dbConnection = DriverManager.getConnection(Config.INSTANCE.sqLite.url, Config.INSTANCE.sqLite.user, Config.INSTANCE.sqLite.password);
    }

    public static void closeConnection() throws SQLException {
        dbConnection.close();
    }

    public static void createDB() throws SQLException {
        Statement statement = dbConnection.createStatement();
        statement.setQueryTimeout(30);

        statement.executeUpdate("CREATE TABLE IF NOT EXISTS application (application_Int INTEGER PRIMARY KEY, username TEXT, user_id BIGINT, nickname TEXT, years INTEGER, sex TEXT, bio TEXT, why_we TEXT)");
        statement.executeUpdate("CREATE TABLE IF NOT EXISTS player (Id INTEGER PRIMARY KEY, application_Int INTEGER, nickname TEXT)");
    }

    public static void addApplication(int application_Int, String username, String user_id, String nickname, int years, String sex, String bio, String why_we) throws SQLException {
        Statement statement = dbConnection.createStatement();
        statement.setQueryTimeout(30);

        statement.executeUpdate(String.format("INSERT INTO application VALUES('%d','%s','%s','%s','%d','%s','%s','%s')", application_Int, username, user_id, nickname, years, sex, bio, why_we));
    }

    public static long getApplicationUser_id(int appInt) throws SQLException {
        Statement statement = dbConnection.createStatement();
        statement.setQueryTimeout(30);
        ResultSet rs = statement.executeQuery(String.format("SELECT * FROM application WHERE application_Int = '%d'", appInt));
        long data = 0;
        while (rs.next())
            data = rs.getLong("user_id");
        return data;
    }

    public static String getApplicationNickname(int appInt) throws SQLException {
        Statement statement = dbConnection.createStatement();
        statement.setQueryTimeout(30);
        ResultSet rs = statement.executeQuery(String.format("SELECT * FROM application WHERE application_Int = '%d'", appInt));
        String data = "";
        while (rs.next())
            data = rs.getString("nickname");
        return data;
    }

    public static void addPlayer(int id, int application_Int, String nickname) throws SQLException {
        Statement statement = dbConnection.createStatement();
        statement.setQueryTimeout(30);

        statement.executeUpdate(String.format("INSERT INTO player VALUES('%d','%d','%s')", id, application_Int, nickname));
    }

    public static String getPlayerNickname(int id) throws SQLException {
        Statement statement = dbConnection.createStatement();
        statement.setQueryTimeout(30);
        ResultSet rs = statement.executeQuery(String.format("SELECT * FROM player WHERE Id = '%d'", id));
        String data = "";
        while (rs.next())
            data = rs.getString("nickname");
        return data;
    }

    public static int getPlayerApp(int id) throws SQLException {
        Statement statement = dbConnection.createStatement();
        statement.setQueryTimeout(30);
        ResultSet rs = statement.executeQuery(String.format("SELECT * FROM player WHERE Id = '%d'", id));
        int data = 0;
        while (rs.next())
            data = rs.getInt("nickname");
        return data;
    }
}