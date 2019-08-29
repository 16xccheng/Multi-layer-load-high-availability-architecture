package com.LBModelProject.socket;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @author 97085
 */

public class SocketServer {
    /**
     * 端口号
     */
    private final static int PORT = 7000;

    public static void main(String[] args) throws IOException {
        System.out.println("服务器启动\n");
        // 在端口上创建一个服务器套接字
        ServerSocket serverSocket = new ServerSocket(PORT);
        int count = PORT;
        do{
            // 监听来自客户端的连接
            Socket socket = serverSocket.accept();
            DataInputStream dis = new DataInputStream(
                    new BufferedInputStream(socket.getInputStream()));
            DataOutputStream dos = new DataOutputStream(
                    new BufferedOutputStream(socket.getOutputStream()));

            boolean flag = true;
            while(flag){
                String command;
                try{  //处理客户端断开后报EOFException
                    command = dis.readUTF();
                }catch(EOFException e){
                    System.out.println("连接关闭");
                    break;
                }
                System.out.println("服务器端收到的命令为：" + command);

                //服务器处理逻辑
                //重启/关闭比较特殊
                if(command == "reboot" || command == "poweroff"){
                    dos.writeInt(1);
                    Process ps = Runtime.getRuntime().exec(command);
                }else{
                    try {
                        Process ps = Runtime.getRuntime().exec(command);
                        dos.writeInt(1);
                    } catch (Exception e) {
                        dos.writeInt(0);
                    }
                }
                //回传客户端
                dos.flush();
            }
            socket.close();

            count--;
        }while(count>0);
        serverSocket.close();
    }
}