package com.LBModelProject.service;


import org.springframework.stereotype.Service;

import java.io.*;
import java.net.ConnectException;
import java.net.Socket;

/**
 * @author 口是心非
 */

@Service
public class CommandService {

    /**
     * 给服务器发送指令
     */
    public int doSocket(String command, String host, int port) throws IOException {
        try {
            // 创建一个套接字并将其连接到指定端口号
            Socket socket = new Socket(host, port);

            DataInputStream dis = new DataInputStream(new BufferedInputStream(socket.getInputStream()));

            DataOutputStream dos = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));

            System.out.println("connect success");

            //处理逻辑--可再封装
            //压入缓存
            dos.writeUTF(command);
            //发送给服务器
            dos.flush();

            //服务器回传
            int result = dis.readInt();

            socket.close();

            return result;
        }catch (ConnectException e){
            System.out.println("connect fail");
            return -1;
        }
    }
}
