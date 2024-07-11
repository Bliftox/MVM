package org.harbingers_of_chaos.mvweb;

import org.apache.catalina.Context;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.Wrapper;
import org.apache.catalina.startup.Tomcat;
import org.harbingers_of_chaos.mvlib.config.Config;

import static org.harbingers_of_chaos.mvm.MystiVerseModServer.LOGGER;

public class Website {

    private static Tomcat tomcat;
    public static void main(String[] args) throws LifecycleException {
        new Website();
    }
    public static void startup() {
        tomcat = new Tomcat();
        tomcat.setPort(Config.instance.web.port);
        tomcat.getConnector();

        Context ctx = tomcat.addContext("", null);
        Wrapper servlet = Tomcat.addServlet(ctx, "myServlet", new MyServletV2());
        servlet.setLoadOnStartup(1);
        servlet.addMapping("/*");

        try {
            tomcat.start();
        } catch (LifecycleException e) {
            LOGGER.warn("[WebSite] ", e);

        }
        LOGGER.info("[WebSite] Starting WebSite");
    }
}
