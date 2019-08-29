package com.LBModelProject.controller;

import com.LBModelProject.domain.ServerStatus;
import com.LBModelProject.result.CodeMsg;
import com.LBModelProject.result.Result;
import com.LBModelProject.service.ServerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

/**
 * @author 口是心非
 */

@Controller
@RequestMapping("/server")
public class ServerController {

    @Autowired
    ServerService serverService;

    /**
     * 版本1
     * 检测各个服务器状态
     * @param num
     * @return 最后回传data中是一个字典{ip:0, ip:1, ip:1, ……}
     */
//    @RequestMapping("/check")
//    @ResponseBody
//    public Result<Map<String, Integer>> check(@RequestParam("num") Integer num){
//        Map<String, Integer> status = new HashMap<>(16);
//        int first = 1, second = 2;
//        if (num<first || num>second){
//            return Result.error(CodeMsg.NUM_NOT_RIGHT);
//        }else if(num == first){
//            status.put("10.2.1.211", 53);status.put("10.2.1.214", 80);status.put("10.2.1.215", 80);
//            status.put("10.2.1.216", 80);status.put("10.2.1.217", 80);status.put("10.2.1.218", 80);
//            status.put("10.2.1.219", 80);status.put("10.2.1.220", 7000);status.put("10.2.1.221", 7000);
//        }else{
//            status.put("10.2.1.210", 53);status.put("10.2.1.212", 80);status.put("10.2.1.213", 80);
//            status.put("10.2.1.216", 80);status.put("10.2.1.217", 80);status.put("10.2.1.218", 80);
//            status.put("10.2.1.219", 80);status.put("10.2.1.233", 7000);status.put("10.2.1.234", 7000);
//        }
//
//        // 结果存储
//        Map<String, Integer> res = new HashMap<>(16);
//        // 遍历检测
//        for (String key : status.keySet()) {
//            int alive = serverService.checkServer(key, status.get(key));
//            res.put(key,alive);
//        }
//        return Result.success(res);
//    }

    /**
     * 版本2
     * 检测各个服务器状态
     */
    @RequestMapping("/check2")
    @ResponseBody
    public Result<Map<String, Integer>> check2(){
        Result<Map<String, Integer>> res;
        res = Result.success(ServerStatus.getServerStatus());
        return res;
    }
}
