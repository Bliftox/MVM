package org.harbingers_of_chaos.mvlib.discord;

import com.mojang.datafixers.kinds.App;
import net.dv8tion.jda.api.entities.Member;
import org.harbingers_of_chaos.mvb.Application;

import java.util.List;

public interface DataBase {

        void saveApplication(String applicationId, List<String> fields);

        List<String> getApplicationFields(String applicationId);

}
