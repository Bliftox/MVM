package org.harbingers_of_chaos.mvlib.discord;

import java.util.List;

public interface DataBase {

        void saveApplication(String applicationId, String memberId, List<String> fields);
        void savePlayer(String applicationId, String memberId, String nickname);

        List<String> getApplicationFields(String applicationId);
        String getApplicationUserId(String applicationId);
        boolean hasApplicationUserId (String memberId);
        boolean hasApplication (String applicationId);


        static void connection() {}
        static void disconnect() {}
        static void createDB() {}

}
