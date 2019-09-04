package com.LBModelProject.domain;


import java.util.HashMap;
import java.util.Map;

/**
 * 创建一个全局的静态HashMap<ip, 1/0>，用于存储服务器状态
 * @author 97085
 */
public class ServerStatus {
    public static Map<String, Integer> getServerStatus() {
        return serverStatus;
    }

    public static void setServerStatus(HashMap<String, Integer> serverStatus) {
        ServerStatus.serverStatus = serverStatus;
    }

    public static void putElement(String key, Integer value){
        serverStatus.put(key, value);
    }

    public static Integer getElement(String key){
        return serverStatus.get(key);
    }

    public static boolean isContainKeys(String key){
        return serverStatus.containsKey(key);
    }

    private static Map<String, Integer> serverStatus = new HashMap<String, Integer>(16);


    public static final String[] STATUS1 = {"192.168.36.140","192.168.36.138","192.168.36.132","192.168.36.142","114.116.232.247","106.14.197.175"};
}
