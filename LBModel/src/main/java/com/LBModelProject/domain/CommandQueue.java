package com.LBModelProject.domain;

import java.util.LinkedList;
import java.util.Queue;

/**
 * @author 97085
 */
public class CommandQueue{
    private static Queue<String> commandQueue = new LinkedList<>();

    /**
     * 向尾部添加一个元素
     * @param element
     */
    public static void putLast(String element){
        commandQueue.offer(element);
    }

    /**
     * 获取第一个元素并删除
     * @return
     */
    public static String getFirst(){
        return commandQueue.poll();
    }

    /**
     * 返回一个长度
     * @return
     */
    public static int getLength(){
        return commandQueue.size();
    }
}
