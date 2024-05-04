package org.harbingers_of_chaos.mvlib.discord;

import java.util.List;

public interface DataBase {

        void saveApplication(String applicationId, String memberId, List<String> fields);

        void savePlayer(String applicationId, String memberId, String nickname);

        static void connection() {}
        static void disconnect() {}
        static void createDB() {}
        List<String> getApplicationFields(String applicationId);

}
