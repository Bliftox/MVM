package org.harbingers_of_chaos.mvlib;

import net.fabricmc.loader.api.FabricLoader;
import org.harbingers_of_chaos.mvlib.config.Config;
import org.harbingers_of_chaos.mvlib.discord.DataBase;

import java.nio.file.Path;
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

    public static void connection(String dbPath){
        try {
            if(Config.instance.mySQLConfig.enabled) {
                dbConnection = DriverManager.getConnection(
                        Config.instance.mySQLConfig.url, Config.instance.mySQLConfig.user, Config.instance.mySQLConfig.password);
            }else{
                Class.forName("org.sqlite.JDBC");
                Path path = FabricLoader.getInstance().getConfigDir().resolve("mvm");
                String url = "jdbc:sqlite:" + path.resolve(dbPath).toString();
                dbConnection = DriverManager.getConnection(url);
            }
            LOGGER.info("Connected to database");
        } catch (SQLException | ClassNotFoundException e) {
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

            statement.executeUpdate("CREATE TABLE IF NOT EXISTS application (applicationId TEXT, ds_id TEXT, nickname TEXT," +
                    " fieldOne TEXT, fieldTwo TEXT, fieldThree TEXT, fieldFour TEXT, obrab TEXT)");
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS player (application_Int TEXT, nickname TEXT, ds_id TEXT, IP TEXT)");
        } catch (SQLException e) {
            LOGGER.warn("[MVM]CreateDBt:"+e);
        }
    }


    @Override
    public void saveApplication(String applicationId, String memberId, List<String> fields) {
        try (Statement statement = dbConnection.createStatement()){
            statement.setQueryTimeout(30);
            statement.executeUpdate(String.format("INSERT INTO application VALUES('%s','%s','%s','%s','%s','%s','%s','true')", applicationId, memberId, fields.get(0), fields.get(1), fields.get(2), fields.get(3), fields.get(4)));
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

            ResultSet rs = statement.executeQuery("SELECT nickname,fieldOne,fieldTwo,fieldThree,fieldFour FROM application WHERE applicationId = '"+applicationId+"'");
            if(rs.next()) {
                for(int i = 1; i < 5; i++) {
                    fields.add(rs.getString(i));
                }
            }
        } catch (SQLException e) {
            LOGGER.warn("[MVM]getApplicationFields:"+e);
        }
        return fields;
    }

    public String getApplicationUserId(String applicationId) {
        try (Statement statement = dbConnection.createStatement()){
            statement.setQueryTimeout(30);

            ResultSet rs = statement.executeQuery("SELECT ds_id FROM application WHERE applicationId = '"+applicationId+"'");
            if(rs.next()) {
                return rs.getString(1);
            }
        } catch (SQLException e) {
            LOGGER.warn("[MVM]getApplicationFields:"+e);
        }
        return "";
    }
    public boolean hasApplicationUserId(String UserId) {
        try (Statement statement = dbConnection.createStatement()){
            statement.setQueryTimeout(30);

            ResultSet rs = statement.executeQuery("SELECT COUNT(*) FROM application WHERE ds_id = '"+UserId+"'");
            if(rs.next()) {
                if (rs.getInt(1) > 0) {
                    return true;
                }
            }
        } catch (SQLException e) {
            LOGGER.warn("[MVM]hasApplicationUserId:"+e);
        }
        return false;
    }
    public boolean hasOrabotUserId(String UserId) {
        try (Statement statement = dbConnection.createStatement()){
            statement.setQueryTimeout(30);

            ResultSet rs = statement.executeQuery("SELECT obrab FROM application WHERE ds_id = '"+UserId+"'");
            if(rs.next()) {
                return rs.getBoolean(1);
            }
        } catch (SQLException e) {
            LOGGER.warn("[MVM]hasOrabotUserId:"+e);
        }
        return false;
    }
    public boolean hasApplication(String applicationId) {
        try (Statement statement = dbConnection.createStatement()){
            statement.setQueryTimeout(30);

            ResultSet rs = statement.executeQuery("SELECT COUNT(*) FROM application WHERE applicationId = '"+applicationId+"'");
            if(rs.next()) {
                if (rs.getInt(1) > 0) {
                    return true;
                }
            }
        } catch (SQLException e) {
            LOGGER.warn("[MVM]hasApplication:"+e);
        }
        return false;
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
    public static String getPlayerId2Nickname(String nickname) {
        try (Statement statement = dbConnection.createStatement()){
            statement.setQueryTimeout(30);

            ResultSet rs = statement.executeQuery("SELECT ds_id FROM player WHERE nickname = '"+nickname+"'");
            if(rs.next()) {
                return rs.getString(1);
            }

        } catch (SQLException e) {
            LOGGER.warn("[MVM]ConnectPlayer:getPlayerNickname2Ip:" + e);
        }
        return "";
    }
    public static String getPlayerId2Ip(String ip) {
        try (Statement statement = dbConnection.createStatement()){
            statement.setQueryTimeout(30);

            ResultSet rs = statement.executeQuery("SELECT ds_id FROM player WHERE IP = '"+ip+"'");
            if(rs.next()) {
                return rs.getString(1);
            }

        } catch (SQLException e) {
            LOGGER.warn("[MVM]ConnectPlayer:getPlayerNickname2Ip:" + e);
        }
        return "";
    }
    public static String getPlayerIp2Id(String id) {
        try (Statement statement = dbConnection.createStatement()){
            statement.setQueryTimeout(30);

            ResultSet rs = statement.executeQuery("SELECT IP FROM player WHERE ds_id = '"+id+"'");
            if(rs.next()) {
                return rs.getString(1);
            }

        } catch (SQLException e) {
            LOGGER.warn("[MVM]ConnectPlayer:getPlayerNickname2Ip:" + e);
        }
        return "";
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
    public static String getPlayerNickname2Id(String id) {
        try (Statement statement = dbConnection.createStatement()){
            statement.setQueryTimeout(30);

            ResultSet rs = statement.executeQuery("SELECT nickname FROM player WHERE ds_id = '"+id+"'");
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
            LOGGER.info("set "+ip);
            statement.executeUpdate(String.format("UPDATE player SET IP = '%s' WHERE ds_id = '%s'", ip, ds_id));
        } catch (SQLException e) {
            LOGGER.warn("[MVM]ConnectPlayer:getPlayerIp:" + e);
        }
    }

    public void setObrab(String applicationId) {
        try (Statement statement = dbConnection.createStatement()){
            statement.setQueryTimeout(30);

            statement.executeUpdate(String.format("UPDATE application SET obrab =  'false' WHERE applicationId = '%s'", applicationId));
        } catch (SQLException e) {
            LOGGER.warn("[MVM]ConnectPlayer:getPlayerIp:" + e);
        }
    }

}