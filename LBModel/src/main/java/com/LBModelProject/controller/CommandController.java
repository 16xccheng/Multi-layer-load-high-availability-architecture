package com.LBModelProject.controller;


import com.LBModelProject.domain.CommandQueue;
import com.LBModelProject.domain.ServerStatus;
import com.LBModelProject.result.CodeMsg;
import com.LBModelProject.result.Result;
import com.LBModelProject.service.CommandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;

/**
 * @author 口是心非
 */

@Controller
@RequestMapping("/command")
public class CommandController {

    //private final static int PORT = 7000;

    @Autowired
    CommandService commandService;

    /**
     * 页面跳转
     * @return 页面
     */
    @RequestMapping("/index1")
    public String index1(){
        return "index1";
    }

    @RequestMapping("/index2")
    public String index2(){
        return "index2";
    }


    /**
     * 版本1
     * 执行命令
     * @param host
     * @param command
     * @return 1：执行成功  0：执行失败  -1：连接失败
     * @throws IOException
     */
//    @RequestMapping("/do_command")
//    @ResponseBody
//    public Result<Integer> doCommand(@RequestParam("host")String host,
//                                     @RequestParam("command")String command) throws IOException {
//        if (host == null) {
//            return Result.error(CodeMsg.HOST_NOT_EXIST);
//        }
//        if (command == null) {
//            return Result.error(CodeMsg.COMMAND_NOT_EXIST);
//        }
//
//        System.out.println("the server's ip ：" + host);
//        System.out.println("the command ：" + command);
//        //交由服务端执行
//        int res = commandService.doSocket(command, host, PORT);
//        System.out.println("return result ：" + res);
//        System.out.println();
//        if (res == 0){
//            return Result.error(CodeMsg.DO_COMMAND_FAIL);
//        }
//        if (res == -1){
//            return Result.error(CodeMsg.CONNECT_FAIL);
//        }
//
//        return Result.success(res);
//    }

    /**
     * 版本2
     * 执行命令
     * @param host
     * @param command
     * @return
     * @throws IOException
     */
    @RequestMapping("/do_command2")
    @ResponseBody
    public Result<Integer> doCommand2(@RequestParam("host")String host,
                                      @RequestParam("command")String command){
        String str = " "+host+":"+command;
        System.out.println("send message "+str);
        if("/usr/local/javaWorkHouse/javaServer.sh".equals(command)){
            CommandQueue.putLast(str);
            return Result.success(1);
        }
        if(ServerStatus.isContainKeys(host)){
            if(ServerStatus.getElement(host)==1){
                CommandQueue.putLast(str);
                return Result.success(1);
            }
        }
        return Result.error(CodeMsg.CONNECT_FAIL);
    }
}
