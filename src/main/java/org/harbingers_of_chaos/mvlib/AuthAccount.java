package org.harbingers_of_chaos.mvlib;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

public class AuthAccount {

    private static final BiMap<String, String> IpIdBiMap = HashBiMap.create();
    public AuthAccount() {}
    public AuthAccount(String account, String password) {}
}
