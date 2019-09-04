package com.LBModelProject.controller;


import com.LBModelProject.domain.CommandQueue;
import com.LBModelProject.domain.ServerStatus;
import com.LBModelProject.result.CodeMsg;
import com.LBModelProject.result.Result;
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

    private static final String SHUT_DOWN= "/usr/local/javaWorkHouse/javaServer.sh";

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
        if(ServerStatus.isContainKeys(host)){
            if(ServerStatus.getElement(host)==0) {
                if (SHUT_DOWN.equals(command)) {
                    CommandQueue.putLast(str);
                    return Result.success(1);
                }
            }

            if(ServerStatus.getElement(host)==1){
                CommandQueue.putLast(str);
                return Result.success(1);
            }
        }
        return Result.error(CodeMsg.CONNECT_FAIL);
    }
}
