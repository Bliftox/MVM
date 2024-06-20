package org.harbingers_of_chaos.mvlib;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import org.jetbrains.annotations.Nullable;

import java.security.SecureRandom;

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

//        if (codeIpBiMap.inverse().containsKey(new String[] {ip,ds_id})) {
//            return QueuingResult.ACCOUNT_QUEUED;
//        }
//        LOGGER.info(ip);
        if (codeIpBiMap.inverse().getOrDefault(ip, null) == null) {
            codeIpBiMap.put(randomId(),ip);
            IpIdBiMap.put(ip,ds_id);
        }
        return QueuingResult.SUCCESS;
    }

    @Nullable
    public String getCode(String ip) {
        return codeIpBiMap.inverse().getOrDefault(ip, null);
    }

    public LinkingResult tryLinkAccount(String code, String discordId) {

        if (MySQL.hasPlayerIp(MySQL.getPlayerIp2Id(discordId))) {
            return LinkingResult.ACCOUNT_LINKED;
        }

        if (!codeIpBiMap.containsKey(code)) {
            return LinkingResult.INVALID_CODE;
        }

        String ip = codeIpBiMap.get(code);
        codeIpBiMap.remove(code);
        if (!IpIdBiMap.get(ip).equals(discordId)) {
            return LinkingResult.INVALID_CODE;
        }

        if (MySQL.hasPlayerIp(ip)) {
            return LinkingResult.REPEAT_IP;
        }
        MySQL.setPlayerIp(ip, discordId);

        return LinkingResult.SUCCESS;
    }
    private String randomId() {
        StringBuilder builder = new StringBuilder();

        for (int i = 0; i < 8; i++) {
            int num = random.nextInt(36);
            char c = (char) (num < 10 ? '0' + num : 'a' + num - 10);
            builder.append(c);
        }

        return builder.toString();
    }
}
