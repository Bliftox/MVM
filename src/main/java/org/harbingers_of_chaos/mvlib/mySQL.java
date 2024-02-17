package org.harbingers_of_chaos.mvlib;

import org.apache.commons.lang3.ObjectUtils;

import java.sql.*;

import static org.harbingers_of_chaos.mvb.application.ApplicationHandler.nickname;
import static org.harbingers_of_chaos.mvm.MystiVerseModServer.LOGGER;



/*
 *
 * Many thanks to the developer of this database, without it I would have suffered and continued to play with crutches.
 * I'll leave this here <3
 * https://github.com/anzuath/DeathNote/blob/main/DeathNoteInternals/src/main/java/com/github/steeldev/deathnote/util/Database.java
 *
 */

public class mySQL {
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
        statement.executeUpdate("CREATE TABLE IF NOT EXISTS player (Id INTEGER PRIMARY KEY, application_Int INTEGER, nickname TEXT, user_id BIGINT, IP TEXT)");
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

    public static void addPlayer(int id, int application_Int, String nickname, String user_id) throws SQLException {
        Statement statement = dbConnection.createStatement();
        statement.setQueryTimeout(30);

        statement.executeUpdate(String.format("INSERT INTO player VALUES('%d','%d','%s','%s',null)", id, application_Int, nickname,user_id));
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
            data = rs.getInt("application_Int");
        return data;
    }

    public static int getPlayerApp(String nickname) throws SQLException {
        Statement statement = dbConnection.createStatement();
        statement.setQueryTimeout(30);
        ResultSet rs = statement.executeQuery(String.format("SELECT * FROM player WHERE nickname = '%s'", nickname));
        int data = 0;
        while (rs.next())
            data = rs.getInt("application_Int");
        return data;
    }
    public static String getPlayerNickToIP(String nickname)  {
        try{Statement statement = dbConnection.createStatement();
        statement.setQueryTimeout(30);
        ResultSet rs = statement.executeQuery(String.format("SELECT * FROM player WHERE nickname = '%s'", nickname));
        String data = "";
        while (rs.next())
            data = rs.getString("IP");
            return data;
        } catch (SQLException e) {
            LOGGER.warn("[MVM]ConnectPlayer:getPlayerNickToIP:"+e);
        }
        return "";
    }
    public static boolean hasPlayerIp(String ip) {
        try(PreparedStatement statement = dbConnection.prepareStatement(String.format("SELECT * FROM player WHERE IP = '%s'", ip))) {
            statement.setQueryTimeout(30);
            LOGGER.info("[MVM]ConnectPlayer:hasPlayerIp:"+ip);
            try(ResultSet rs = statement.executeQuery()){
                if(rs.next()){
                    return true;
                }
            } catch (SQLException e) {
                LOGGER.warn("[MVM]ConnectPlayer:hasPlayerIp:statement:"+e);
            }
        } catch (SQLException e) {
            LOGGER.warn("[MVM]ConnectPlayer:hasPlayerIp:PreparedStatement:"+e);
        }
        return false;
    }
    public static boolean hasPlayerNick(String nickname)  {
        try(PreparedStatement statement = dbConnection.prepareStatement("SELECT * FROM player WHERE nickname = '"+nickname+"'")) {
            statement.setQueryTimeout(30);
            LOGGER.info("[MVM]ConnectPlayer:hasPlayerNick:"+nickname);
            try(ResultSet rs = statement.executeQuery()){
                if(rs.next()){
                    return true;
                }
            } catch (SQLException e) {
                LOGGER.warn("[MVM]ConnectPlayer:hasPlayerNick:statement:"+e);
            }
        } catch (SQLException e) {
            LOGGER.warn("[MVM]ConnectPlayer:hasPlayerNick:PreparedStatement:"+e);
        }

        return false;

    }
    public static boolean hasPlayerDiscordId(long discordId) throws SQLException {
        Statement statement = dbConnection.createStatement();
        statement.setQueryTimeout(30);
        ResultSet rs = statement.executeQuery(String.format("SELECT * FROM player WHERE user_id = '%d'", discordId));
        return rs.next();
    }

    public static Object getPlayerIpToNick(String ip)  {
        long data = 0;
        try {
            Statement statement = dbConnection.createStatement();
            statement.setQueryTimeout(30);
            ResultSet rs = statement.executeQuery(String.format("SELECT * FROM player WHERE IP = '%s'", ip));
            while (rs.next())
                data = rs.getLong("nickname");
        } catch (SQLException e) {
            LOGGER.warn("[MVM]ConnectPlayer:hasPlayerNick:PreparedStatement:"+e);
        }
        return data;
    }
    public static void setPlayerIp(String ip,long user_id) throws SQLException {
        Statement statement = dbConnection.createStatement();
        statement.setQueryTimeout(30);
        statement.executeUpdate(String.format("UPDATE player SET IP = '%s' WHERE user_id = '%s'", ip, user_id));
    }

}