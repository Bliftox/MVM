package org.harbingers_of_chaos.mvlib.discord;

import java.util.List;

public interface DataBase {

        void saveApplication(String applicationId, List<String> fields);

        List<String> getApplicationFields(String applicationId);

}
