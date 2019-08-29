package com.LBModelProject.service;

import org.springframework.stereotype.Service;

import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;

/**
 * @author 口是心非
 */

@Service
public class ServerService {

    /**
     * 检测规定时间内对应端口是否启动
     * @param host
     * @param port
     * @return
     */
    public int checkServer(String host, int port) {
        try {
            Socket socket = new Socket();
            SocketAddress add = new InetSocketAddress(host,port);
            socket.connect(add,500);
            socket.close();
            return 1;
        } catch (Exception e) {
            return 0;
        }
    }
}
