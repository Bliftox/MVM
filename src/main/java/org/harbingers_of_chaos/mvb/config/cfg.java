package org.harbingers_of_chaos.mvb.config;

import io.github.cdimascio.dotenv.Dotenv;

public class cfg {
    public static String TOKEN = "MTE4ODE4NjA0MDU4MjgwMzQ4Nw.Gxa24r.c_FGyGGbj4P4vcoZp3eji6E2zl2nkXZ3mAUlV0";
    public static String password = "iUpyXD5+cl7+IfGia8eoILz5";
    public static String url = "jdbc:mysql://u44248_pUYk55cLWH:iUpyXD5%2Bcl7%2BIfGia8eoILz5@db1.apexnodes.xyz:3306/s44248_Application";
    public static String user = "u44248_pUYk55cLWH";
    public static Character prefix = '!';

    private static final Dotenv dotenv = Dotenv.load();

    public static String get(String key) {
        return dotenv.get(key.toUpperCase());
    }

}
