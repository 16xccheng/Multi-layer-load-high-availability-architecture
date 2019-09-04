package com.LBModelProject.controller;

import com.LBModelProject.domain.ServerStatus;
import com.LBModelProject.result.Result;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

/**
 * @author 口是心非
 */

@Controller
@RequestMapping("/server")
public class ServerController {

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
