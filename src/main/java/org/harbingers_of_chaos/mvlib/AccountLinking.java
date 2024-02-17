package org.harbingers_of_chaos.mvlib;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import org.jetbrains.annotations.Nullable;

import java.security.SecureRandom;
import java.sql.SQLException;
import java.util.Optional;

import static org.harbingers_of_chaos.mvm.MystiVerseModServer.LOGGER;

public class AccountLinking {
    public enum LinkingResult {
        INVALID_CODE,
        ACCOUNT_LINKED,
        SUCCESS
    }

    public enum UnlinkingResult {
        ACCOUNT_UNLINKED,
        SUCCESS
    }

    public enum QueuingResult {
        ACCOUNT_QUEUED,
        ACCOUNT_LINKED,
        SUCCESS
    }

    private final BiMap<String, String> codeIpBiMap = HashBiMap.create();

    private final SecureRandom random = new SecureRandom();
    public QueuingResult tryQueueForLinking(String ip){

        if (codeIpBiMap.inverse().containsKey(ip)) {
            return QueuingResult.ACCOUNT_QUEUED;
        }

        codeIpBiMap.put(randomId(), ip);
        return QueuingResult.SUCCESS;
    }

    @Nullable
    public String getCode(String ip) {
        return codeIpBiMap.inverse().getOrDefault(ip, null);
    }

    public LinkingResult tryLinkAccount(String code, Long discordId) throws SQLException {

        if (!codeIpBiMap.containsKey(code)) {
            return LinkingResult.INVALID_CODE;
        }

        String ip = codeIpBiMap.get(code);
        codeIpBiMap.remove(code);
        mySQL.setPlayerIp(ip, discordId);
        LOGGER.warn("set");
        return LinkingResult.ACCOUNT_LINKED;
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
