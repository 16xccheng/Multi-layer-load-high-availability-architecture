package com.LBModelProject.init;

import com.LBModelProject.domain.CommandQueue;
import com.LBModelProject.domain.ServerStatus;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 自启动的方法
 * @author 97085
 */
@Component
public class InitServer implements ApplicationRunner {
    /**
     * 缓冲区大小
     */
    private static final int BUFFER_SIZE = 100;
    /**
     * 监听端口
     */
    private static final int PORT = 7001;
    /**
     * 延迟时间
     */
    private static final int TIME_OUT = 3000;

    /**
     * 初始化服务器状态表---后续可通过添加字段增加服务器
     */
    private void init(){
        for(String s: ServerStatus.STATUS1){
            ServerStatus.putElement(s,0);
        }
    }

    /**
     * 匹配对应字符串中的ip字段
     * @param str
     * @return
     */
    private Matcher foundIP(String str){
        // 匹配模式
        String pattern = "((2(5[0-5]|[0-4]\\d))|[0-1]?\\d{1,2})(\\.((2(5[0-5]|[0-4]\\d))|[0-1]?\\d{1,2})){3}";
        Pattern r = Pattern.compile(pattern);
        Matcher matcher = r.matcher(str);
        return matcher;
    }

    /**
     * 对应格式字符串转hashmap
     * 例A=a&B=b&C=c
     * @param s
     * @return
     */
    private Map<String, String> stringToHashmap(String s){
        System.out.println(s);
        String[] strs = s.split("&");
        Map<String, String> m = new HashMap<String, String>(16);
        for(String str : strs){
            String[] ms = str.split("=");
            m.put(ms[0], ms[1]);
        }
        return m;
    }

    /**
     * 自启动主程序
     * 方法太长，需要分解优化
     * @param args
     * @throws IOException
     */
    @Override
    public void run(ApplicationArguments args) throws IOException {
        init();
        Selector selector = null;
        try {
            // 1.创建 Selector 实例
            selector = Selector.open();

            // 2.创建 ServerSocketChannel 实例，配置为非阻塞模式，绑定本地端口
            ServerSocketChannel ssc = ServerSocketChannel.open();
            ssc.configureBlocking(false);
            ssc.bind(new InetSocketAddress(PORT));

            // 3.把 ServerSocketChannel实例 注册到 Selector 实例中
            ssc.register(selector, SelectionKey.OP_ACCEPT);

            while (true) {
                // 4.选择一些准备好 I/O 操作的信道，这里设置了3秒超时时间，也就是阻塞3秒
                if (selector.select(TIME_OUT) == 0) {
                    continue;
                }
                // 5.获取选中的 SelectionKey 的集合
                Set<SelectionKey> selectionKeys = selector.selectedKeys();
                Iterator<SelectionKey> iterator = selectionKeys.iterator();

                // 获取对应的命令
                String temp = CommandQueue.getFirst();
                // 6.处理 SelectionKey 的感兴趣的操作
                while (iterator.hasNext()) {
                    SelectionKey key = iterator.next();

                    // 获得连接事件
                    if (key.isAcceptable()) {
                        ServerSocketChannel serverSocketChannel = (ServerSocketChannel) key.channel();

                        // 获得和客户端连接的通道
                        SocketChannel clientChannel = serverSocketChannel.accept();

                        // 设置非阻塞
                        clientChannel.configureBlocking(false);

                        // 在和客户端连接成功之后，为了可以接收到客户端的信息，需要给通道设置读的权限
                        clientChannel.register(key.selector(), SelectionKey.OP_READ|SelectionKey.OP_WRITE);
                    }

                    // 获得可读事件
                    else if (key.isReadable()) {
                        // 服务器可读取消息:得到事件发生的Socket通道
                        SocketChannel clientChannel = (SocketChannel) key.channel();

                        // 创建读取的缓冲区
                        ByteBuffer readBuffer = ByteBuffer.allocate(BUFFER_SIZE);
                        int readBytes = clientChannel.read(readBuffer);
                        // 客户端中断后执行
                        if (readBytes == -1) {
                            // 过滤ip
                            Matcher matcher = foundIP(clientChannel.getRemoteAddress().toString());
                            String clientIP = "";
                            if(matcher.find()) {
                                clientIP = (String)matcher.group(0);
                                if(ServerStatus.isContainKeys(clientIP)){
                                    ServerStatus.putElement(clientIP,0);
                                }
                            }
                            System.out.println(clientIP+" disconnect……");
                            clientChannel.close();
                        }
                        // 客户端存活执行
                        else if (readBytes > 0) {
                            // 对应格式字符串转hashmap----也可以转json
                            String s = new String(readBuffer.array());
                            Map<String, String> m = stringToHashmap(s);
                            String ip = m.get("ip");
                            String statusStr = m.get("status");
                            Integer status = Integer.valueOf(statusStr);

                            // 根据组别写入对应hashmap
                            ServerStatus.putElement(ip,status);

                            // 切换到写
                            //key.interestOps(SelectionKey.OP_WRITE);
                        }

                    }
                    if(temp!=null) {
                        // 广播命令
                        if (key.isWritable()) {
                            SocketChannel clientChannel = (SocketChannel) key.channel();
                            // 内容写入缓冲区
                            System.out.println("write to buffer : " + temp);
                            clientChannel.write(ByteBuffer.wrap(temp.getBytes()));

                            // 切换回读
                            //key.interestOps(SelectionKey.OP_READ);
                        }
                    }
                    // 删除已选的key,以防重复处理
                    iterator.remove();
                }
            }
        } catch (IOException e) {
            // 客户端强制关此处抛异常
            e.printStackTrace();
        } finally {
            // 最后关闭selector
            System.out.println("server error");
            if (selector != null) {
                try {
                    selector.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
