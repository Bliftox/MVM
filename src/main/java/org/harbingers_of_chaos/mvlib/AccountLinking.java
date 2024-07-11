package org.harbingers_of_chaos.mvlib;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import org.jetbrains.annotations.Nullable;

import java.security.SecureRandom;
import java.util.Random;

import static org.harbingers_of_chaos.mvm.MystiVerseModServer.LOGGER;

public class AccountLinking {
    public enum LinkingResult {
        INVALID_CODE,
        ACCOUNT_LINKED,
        REPEAT_IP,
        SUCCESS
    }

    public enum QueuingResult {
        ACCOUNT_QUEUED,
        ACCOUNT_LINKED,
        SUCCESS
    }

    private static final BiMap<String, String> codeIpBiMap = HashBiMap.create();
    private static final BiMap<String, String> IpIdBiMap = HashBiMap.create();

    private final SecureRandom random = new SecureRandom();
    public QueuingResult tryQueueForLinking(String ip, String ds_id){

        if (MySQL.hasPlayerIp(ip)&&MySQL.getPlayerId2Ip(ip).equals(ds_id)) {
            return QueuingResult.ACCOUNT_LINKED;
        }
        if (codeIpBiMap.inverse().getOrDefault(ip, null) == null) {
            String code = randomId();
            LOGGER.info("[MVM] Code : {}", code);
            LOGGER.info("[MVM] Ip : {}", ip);
            LOGGER.info("[MVM] Ds_id : {}", ds_id);
            codeIpBiMap.put(code,ip);
            IpIdBiMap.put(ip,ds_id);
        }
        return QueuingResult.SUCCESS;
    }

    @Nullable
    public String getCode(String ip) {
        return codeIpBiMap.inverse().getOrDefault(ip, null);
    }

    public LinkingResult tryLinkAccount(String code, String discordId) {
        LOGGER.info("[LDBot] Trying to link account " + code + " to " + discordId);

        String ip = IpIdBiMap.inverse().getOrDefault(discordId, null);

        LOGGER.info("[LDBot] Has IP " + ip);
        LOGGER.info("[LDBot] Has Real IP " + MySQL.getPlayerIp2Id(discordId));
        if (MySQL.hasPlayerIp2Id(ip,discordId)) {
            return LinkingResult.ACCOUNT_LINKED;
        }

        if (!codeIpBiMap.containsKey(code)) {
            return LinkingResult.INVALID_CODE;
        }

        if (!IpIdBiMap.getOrDefault(ip,null).equals(discordId)) {
            LOGGER.info("[LDBot] Не тот акк");
            return LinkingResult.INVALID_CODE;
        }
        if (MySQL.hasPlayerIp(ip)) {
            return LinkingResult.REPEAT_IP;
        }
        MySQL.setPlayerIp(ip, discordId);
        codeIpBiMap.remove(code);
        IpIdBiMap.remove(ip);
        return LinkingResult.SUCCESS;
    }
    private String randomId() {
        return new Random().ints(48, 58)
                .limit(3)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
    }
}
