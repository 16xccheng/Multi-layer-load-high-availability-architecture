package com.LBModelProject.client;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;

/**
 * @author 97085
 */
public class NIOClient {
    /**
     * server socket ip
     */
    private static final String SERVER_HOST = "106.52.248.247";

    /**
     * server socket port
     */
    private static final int PORT = 7001;

    /**
     * localhost belong to which group
     */
    private static final String GROUP = "3";

    /**
     * localhost ip
     */
    private static final String LOCALHOST = "192.168.36.138";

    /**
     * heartbreak check port
     */
    private static final int CHECK_PORT = 80;

    /**
     * heartbreak check status
     */
    private static String status = "0";

    /**
     * check service timeout (ms)
     */
    private static final int TIME_OUT = 500;


    /**
     * localhost message
     */
    private Socket socket;
    private String ip;
    private String name;
    private int port;
    DataOutputStream dos;
    DataInputStream dis;

    /**
     * 构造方法
     * @param ip socket服务端ip
     * @param port socket对应端口
     */
    private NIOClient(String ip, int port, String name) {
        try {
            this.ip = ip;
            this.port = port;
            this.name = name;
            this.socket = new Socket(ip, port);
            dos = new DataOutputStream(socket.getOutputStream());
            dis = new DataInputStream(socket.getInputStream());
            System.out.println(ip + " connect to server……");
            new Thread(new CheckPortThread()).start();
            new Thread(new HeartThread()).start();
            new Thread(new MsgThread(),name).start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 给服务端发送信息
     * @param content
     */
    public void sendMsg(String content) {
        try {
            byte[] data = content.getBytes();
            dos.write(data);
            dos.flush();
        } catch (Exception e) {
            e.printStackTrace();
            closeSocket();
        }
    }

    /**
     * 异常处理，关闭流
     */
    public void closeSocket() {
        try {
            System.out.println("close the socket");
            socket.close();
            dos.close();
            dis.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 对象转字节流
     * @param obj
     * @return
     */
    public static byte[] objectToByte(Object obj) {
        byte[] bytes = null;
        try {
            // object---->byte[]
            ByteArrayOutputStream bo = new ByteArrayOutputStream();
            ObjectOutputStream oo = new ObjectOutputStream(bo);
            oo.writeObject(obj);
            bytes = bo.toByteArray();
            bo.close();
            oo.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bytes;
    }

    /**
     * 字节流转对象
     * @param bytes
     * @return
     */
    public static Object byteToObject(byte[] bytes) {
        Object obj = null;
        try {
            // byte array to object
            ByteArrayInputStream bi = new ByteArrayInputStream(bytes);
            ObjectInputStream oi = new ObjectInputStream(bi);
            obj = oi.readObject();
            bi.close();
            oi.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return obj;
    }

    /**
     * 检测对应端口的线程
     */
    class CheckPortThread implements Runnable{
        @Override
        public void run(){
            //适用于nginx、haproxy，web，dns
            while(true){
                try {
                    Thread.sleep(500);
                    // 简单socket检测
                    Socket socket = new Socket();
                    SocketAddress add = new InetSocketAddress("127.0.0.1",CHECK_PORT);
                    // 超时时间为0.5s
                    socket.connect(add,TIME_OUT);
                    socket.close();
                    status = "1";
                } catch (Exception e) {
                    status = "0";
                }
            }
            // 使用于lvs监听keepalived
//            while(true){
//                try {
//                    // 通过java执行linux命令
//                    String cmd = "ps -ef | grep \"keepalived\" | grep -v \"grep\" | awk '{print $2}'";
//                    String[] cmdA = {"/bin/sh", "-c", cmd};
//                    Process process = Runtime.getRuntime().exec(cmdA);
//                    LineNumberReader br = new LineNumberReader(new InputStreamReader(process.getInputStream()));
//                    StringBuffer sb = new StringBuffer();
//                    String line;
//                    while ((line = br.readLine()) != null) {
//                        sb.append(line).append("\n");
//                    }
//                    if(sb.length()>0){
//                        status = "1";
//                    }else{
//                        status = "0";
//                    }
//                } catch (Exception e) {
//                    status = "0";
//                    e.printStackTrace();
//                }
//            }
        }
    }

    /**
     * 心跳检测线程
     */
    class HeartThread implements Runnable {
        @Override
        public void run() {
            while(true) {
                try {
                    sendMsg("ip="+LOCALHOST+"&group="+GROUP+"&status="+status);
                    Thread.sleep(1000);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 接受信息的线程
     */
    class MsgThread implements Runnable {
        @Override
        public void run() {
            while(true) {
                try {
                    if(socket.getInputStream().available() > 0) {
                        byte[] bytes = new byte[1024];
                        // if语句中去掉了第一个字符
                        if(dis.read()!='\0'){
                            dis.read(bytes);
                            String Str = new String(bytes);
                            System.out.println(name+Str);
                            // 服务端发送的字符串（去掉首个字符）格式是"ip:command"
                            String[] str = Str.split(":");
                            if(str.length>1){
                                String ip = str[0];
                                String command = str[1];
                                // 当ip为本机ip时，可执行对应命令
                                if(LOCALHOST.equals(ip)){
                                    try{
                                        Process ps = Runtime.getRuntime().exec(command);
                                    }catch (Exception e){
                                        e.printStackTrace();
                                    }
                                }
                            }
                        }
                    }
                } catch (Exception e) {
                    closeSocket();
                }
            }
        }
    }

    public static void main(String[] args) {
        NIOClient client1 = new NIOClient(SERVER_HOST, PORT,"Thread1");
    }
}
