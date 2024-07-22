package org.harbingers_of_chaos.mvlib;

import net.minecraft.server.network.ServerPlayerEntity;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static org.harbingers_of_chaos.mvm.MystiVerseModServer.LOGGER;

public class AuthAccount {

    private static final Map<String, Boolean> AccountAuthBiMap = new HashMap<String, Boolean>();

    public static void addPlayer(String account,String ip){
        AccountAuthBiMap.put(account, SQL.hasIP(account,ip));
        if(SQL.hasPlayer(account)) SQL.addPlayer(account);
        LOGGER.info(AccountAuthBiMap);
    }
    public static void setAuth(ServerPlayerEntity account, String password){

        try {
            if (SQL.getPassword(account.getEntityName()) == Arrays.hashCode(MessageDigest.getInstance("SHA-256")
                            .digest(password.getBytes(StandardCharsets.UTF_8)))){
                AccountAuthBiMap.put(account.getEntityName(), true);
                SQL.setIP(account.getEntityName(),account.getIp());
            }
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        LOGGER.info(AccountAuthBiMap);
    }
    public static void removePlayer(String account){
        AccountAuthBiMap.remove(account);
        LOGGER.info(AccountAuthBiMap);
    }
    public static boolean canPlay(String account){
        return AccountAuthBiMap.getOrDefault(account,false);
    }
}
