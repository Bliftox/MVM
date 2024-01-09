package org.harbingers_of_chaos.mvb.profiles;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mojang.authlib.GameProfile;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.UUID;

public class profiles {
    String nickname;
    UUID uuid;
//    private static GameProfile Url(String urlString) throws Exception {
//        BufferedReader reader = null;
//        try {
//            URL url = new URL(urlString);
//            reader = new BufferedReader(new InputStreamReader(url.openStream()));
//            StringBuffer buffer = new StringBuffer();
//            int read;
//            char[] chars = new char[1024];
//            while ((read = reader.read(chars)) != -1)
//                buffer.append(chars, 0, read);
//
//            return buffer.toString();
//        } finally {
//            if (reader != null)
//                reader.close();
//        }
//    }
    public static JsonObject JsonUrl(String urlString) throws Exception {
        BufferedReader reader = null;
        try {
            URL url = new URL("https://api.mojang.com/users/profiles/minecraft/"+urlString);
            URLConnection request = url.openConnection();
            request.setRequestProperty("Content-Type", "application/json; utf-8");

            return new JsonParser().parse(new InputStreamReader((InputStream) request.getContent())).getAsJsonObject();
        } finally {
            if (reader != null)
                reader.close();
        }
    }
}
