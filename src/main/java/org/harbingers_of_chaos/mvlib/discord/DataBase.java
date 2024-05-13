package org.harbingers_of_chaos.mvlib.discord;

import java.sql.SQLException;
import java.util.List;

public interface DataBase {

        void savePlayer(String applicationId, String memberId, String nickname);

        static void connection() {}
        static void disconnect() {}
        static void createDB() {}


        void saveApplication(String applicationId, String memberId, List<String> fields);

        List<String> getApplicationFields(String applicationId);

        String getApplicationUserId(String applicationId);

        boolean hasApplication(String applicationId);

        boolean hasApplicationUserId(String userId);
}
