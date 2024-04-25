package org.harbingers_of_chaos.mvb;

import net.dv8tion.jda.api.utils.data.DataObject;

import java.lang.reflect.Member;
import java.util.List;

public  class Application {
        private final List<String> fields;
        private final Member member;

        public Application(List<String> fields, Member member) {
                this.fields = fields;
                this.member = member;
        }




}
