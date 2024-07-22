package org.harbingers_of_chaos.mvlib;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.server.network.ServerPlayerEntity;

import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.*;
import java.util.Arrays;

import org.harbingers_of_chaos.mvlib.api.SQLApi;
import org.harbingers_of_chaos.mvlib.config.Config;

import static org.harbingers_of_chaos.mvm.MystiVerseModServer.LOGGER;

public class SQL implements SQLApi {
    static Connection dbConnection;

    public static void connection(){
        try {
            if(Config.instance.mySQLConfig.enabled) {
                dbConnection = DriverManager.getConnection(
                        Config.instance.mySQLConfig.url, Config.instance.mySQLConfig.user, Config.instance.mySQLConfig.password);
            }else{
                Class.forName("org.sqlite.JDBC");
                Path path = FabricLoader.getInstance().getConfigDir().resolve("mvm");
                String url = "jdbc:sqlite:" + path.resolve("database.db");
                dbConnection = DriverManager.getConnection(url);
            }
            LOGGER.info("[MVM] Connected to database");
        } catch (SQLException | ClassNotFoundException e) {
            LOGGER.warn("[MVM] Connection : ", e);
        }
    }

    public static void disconnect(){
        try {
            dbConnection.close();
        } catch (SQLException e) {
            LOGGER.warn("[MVM] CloseConnection : ", e);
        }
    }

    public static void createDB(){
        try (Statement statement = dbConnection.createStatement()){
            statement.setQueryTimeout(30);

//            statement.executeUpdate("CREATE TABLE IF NOT EXISTS application (applicationId TEXT, ds_id TEXT, nickname TEXT," +
//                    " fieldOne TEXT, fieldTwo TEXT, fieldThree TEXT, fieldFour TEXT, obrab TEXT)");
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS player (application_Id TEXT, nickname TEXT, id TEXT, IP TEXT, password INTEGER)");
        } catch (SQLException e) {
            LOGGER.warn("[MVM] CreateDBt : ", e);
        }
    }


    public static void addPlayer(String name) {
        try (Statement statement = dbConnection.createStatement()){
            LOGGER.info("[MVM] Add player : {}", name);
            statement.setQueryTimeout(30);
            statement.executeUpdate("INSERT INTO player VALUES(null,'"+name+"',null,null,null)");
        } catch (SQLException e) {
            LOGGER.warn("[MVM] SavePlayer : ", e);
        }
    }
    public static boolean hasPlayer(String name) {
        try (Statement statement = dbConnection.createStatement()){
            statement.setQueryTimeout(30);
            ResultSet rs = statement.executeQuery("SELECT COUNT(*) FROM player WHERE nickname = '"+name+"'");
            LOGGER.info("[MVM] Has player : {}", rs.getInt(1));
            return rs.getInt(1) <= 0;
        } catch (SQLException e) {
            LOGGER.warn("[MVM] SQL:getPassword:", e);
        }
        return false;
    }


    public static int getPassword(String name) {
        try (Statement statement = dbConnection.createStatement()){
            statement.setQueryTimeout(30);
            ResultSet rs = statement.executeQuery("SELECT password FROM player WHERE nickname = '"+name+"'");
            if(rs.next()) return rs.getInt(1);
        } catch (SQLException e) {
            LOGGER.warn("[MVM] SQL:getPassword:", e);
        }
        return 0;
    }
    public static boolean hasPassword(String name) {
        try (Statement statement = dbConnection.createStatement()){
            statement.setQueryTimeout(30);

            ResultSet rs = statement.executeQuery("SELECT password FROM player WHERE nickname = '"+name+"'");
            return rs.getString(1) != null;
        } catch (SQLException e) {
            LOGGER.warn("[MVM] SQL:getPassword:", e);
        }
        return false;
    }
    public static void setPassword(ServerPlayerEntity player, String password) {
        try (Statement statement = dbConnection.createStatement()){
            statement.setQueryTimeout(30);
            int pass = Arrays.hashCode(MessageDigest.getInstance("SHA-256")
                    .digest(password.getBytes(StandardCharsets.UTF_8)));
            statement.executeUpdate("UPDATE player SET password = "+pass+" WHERE nickname = '"+player.getEntityName()+"'");
            LOGGER.info("[MVM] Set password to {}", pass);
            AuthAccount.setAuth(player,password);
        } catch (SQLException | NoSuchAlgorithmException e) {
            LOGGER.warn("[MVM] ConnectPlayer:getPlayerIp:", e);
        }
    }


    public static boolean hasIP(String name, String ip) {
        try (Statement statement = dbConnection.createStatement()){
            statement.setQueryTimeout(30);

            ResultSet rs = statement.executeQuery("SELECT IP FROM player WHERE nickname = '"+name+"'");
            if(rs.next()) return ip.equals(rs.getString(1));

        } catch (SQLException e) {
            LOGGER.warn("[MVM] SQL:getPassword:", e);
        }
        return false;
    }
    public static void setIP(String name, String ip) {
        try (Statement statement = dbConnection.createStatement()){
            statement.setQueryTimeout(30);

            LOGGER.info("[MVM] Set {} IP to {}",name, ip);
            statement.executeUpdate("UPDATE player SET IP = '"+ip+"' WHERE nickname = '"+name+"'");
        } catch (SQLException e) {
            LOGGER.warn("[MVM] SQL.setIP:", e);
        }
    }

}