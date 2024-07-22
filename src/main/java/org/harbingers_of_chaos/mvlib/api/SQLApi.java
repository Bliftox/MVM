package org.harbingers_of_chaos.mvlib.api;

public interface SQLApi {

    static void connection() {}
    static void disconnect() {}
    static void createDB() {}

    static void addPlayer(String nick) {}
    static boolean hasPlayer(String nick) {return false;}

    static int getPassword(String nick) {return 0;}
    static boolean hasPassword(String nick) {return false;}
    static void setPassword(String nick, String password) {}

    static boolean hasIP(String nick, String IP) {return false;}
    static void setIP(String nick, String IP) {}
}
