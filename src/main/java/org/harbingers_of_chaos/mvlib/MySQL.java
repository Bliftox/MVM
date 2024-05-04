package org.harbingers_of_chaos.mvlib;

import org.harbingers_of_chaos.mvlib.discord.DataBase;
import org.harbingers_of_chaos.mvm.MystiVerseModServer;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

//import static org.harbingers_of_chaos.mvb.application.ApplicationHandler.nickname;
import static org.harbingers_of_chaos.mvm.MystiVerseModServer.LOGGER;



/*
 *
 * Many thanks to the developer of this database, without it I would have suffered and continued to play with crutches.
 * I'll leave this here <3
 * https://github.com/anzuath/DeathNote/blob/main/DeathNoteInternals/src/main/java/com/github/steeldev/deathnote/util/Database.java
 *
 */

public class MySQL implements DataBase {
    static Connection dbConnection;

    public static void connection(){
        try {
            dbConnection = DriverManager.getConnection(
                    Config.instance.mySQLConfig.url, Config.instance.mySQLConfig.user, Config.instance.mySQLConfig.password);
        } catch (SQLException e) {
            LOGGER.warn("[MVM]Connection:"+e);
        }
    }

    public static void disconnect(){
        try {
            dbConnection.close();
        } catch (SQLException e) {
            LOGGER.warn("[MVM]CloseConnection:"+e);
        }
    }

    public static void createDB(){
        try (Statement statement = dbConnection.createStatement()){
            statement.setQueryTimeout(30);

            statement.executeUpdate("CREATE TABLE IF NOT EXISTS application (applicationId TEXT, ds_id BIGINT, nickname TEXT," +
                    " fieldOne TEXT, fieldTwo TEXT, fieldThree TEXT, fieldFour TEXT, fieldFive TEXT)");
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS player (application_Int TEXT, nickname TEXT, ds_id TEXT, IP TEXT)");
        } catch (SQLException e) {
            LOGGER.warn("[MVM]CreateDBt:"+e);
        }
    }


    @Override
    public void saveApplication(String applicationId, String memberId, List<String> fields) {
        try (Statement statement = dbConnection.createStatement()){
            statement.setQueryTimeout(30);

            statement.executeUpdate(String.format("INSERT INTO application VALUES('%s','%s','%s','%s','%s','%s','%s','%s')", applicationId, memberId, fields.get(0), fields.get(1), fields.get(2), fields.get(3), fields.get(4), fields.get(5)));
        } catch (SQLException e) {
            LOGGER.warn("[MVM]saveApplication:"+e);
        }
    }

    @Override
    public void savePlayer(String applicationId, String memberId, String nickname) {
        try (Statement statement = dbConnection.createStatement()){
            statement.setQueryTimeout(30);

            statement.executeUpdate(String.format("INSERT INTO player VALUES('%s','%s','%s',null)", applicationId, nickname, memberId));
        } catch (SQLException e) {
            LOGGER.warn("[MVM]savePlayer:"+e);
        }
    }

    @Override
    public List<String> getApplicationFields(String applicationId) {
        List<String> fields = new ArrayList<>(6);
        try (Statement statement = dbConnection.createStatement()){
            statement.setQueryTimeout(30);

            ResultSet rs = statement.executeQuery("SELECT nickname,fieldOne,fieldTwo,fieldThree,fieldFour,fieldFive FROM application WHERE applicationId = '"+applicationId+"'");
            if(rs.next()) {
                for(int i = 1; i < 7; i++) {
                    fields.add(rs.getString(i));
                }
            }
        } catch (SQLException e) {
            LOGGER.warn("[MVM]getApplicationFields:"+e);
        }
        return fields;
    }

    public static boolean hasPlayerIp(String ip) {
        try (Statement statement = dbConnection.createStatement()){
            statement.setQueryTimeout(30);

            ResultSet rs = statement.executeQuery("SELECT COUNT(*) FROM player WHERE IP = '"+ip+"'");
            if(rs.next()) {
                if (rs.getInt(1) > 0) {
                    return true;
                }
            }
        } catch (SQLException e) {
            LOGGER.warn("[MVM]ConnectPlayer:hasPlayerIp:"+e);
        }
        return false;
    }
    public static boolean hasPlayerNick(String nickname)  {
        try (Statement statement = dbConnection.createStatement()){
            statement.setQueryTimeout(30);

            ResultSet rs = statement.executeQuery("SELECT COUNT(*) FROM player WHERE nickname = '"+nickname+"'");
            if(rs.next()) {
                if (rs.getInt(1) > 0) {
                    return true;
                }
            }
        } catch (SQLException e) {
            LOGGER.warn("[MVM]ConnectPlayer:hasPlayerNick:"+e);
        }
        return false;
    }

    public static String getPlayerNickname2Ip(String ip) {
        try (Statement statement = dbConnection.createStatement()){
            statement.setQueryTimeout(30);

            ResultSet rs = statement.executeQuery("SELECT nickname FROM player WHERE IP = '"+ip+"'");
            if(rs.next()) {
                return rs.getString(1);
            }

        } catch (SQLException e) {
            LOGGER.warn("[MVM]ConnectPlayer:getPlayerNickname2Ip:" + e);
        }
        return "";
    }
    public static void setPlayerIp(String ip,String ds_id) {
        try (Statement statement = dbConnection.createStatement()){
            statement.setQueryTimeout(30);

            statement.executeUpdate(String.format("UPDATE player SET IP = '%s' WHERE ds_id = '%s'", ip, ds_id));
        } catch (SQLException e) {
            LOGGER.warn("[MVM]ConnectPlayer:getPlayerIp:" + e);
        }
    }

}