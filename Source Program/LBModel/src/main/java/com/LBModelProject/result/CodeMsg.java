package com.LBModelProject.result;


/**
 * @author 口是心非
 */

public class CodeMsg {
    private int code;
    private String msg;

    /**
     * 成功场景
      */
    public static CodeMsg SUCCESS = new CodeMsg(500200, "连接成功");

    /**
     * 失败场景
     */
    public static CodeMsg CONNECT_FAIL = new CodeMsg(500301, "连接失败");


    public CodeMsg(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public CodeMsg fillArgs(Object... args){
        int code = this.code;
        String message = String.format(this.msg,args);
        return new CodeMsg(code,message);
    }

    @Override
    public String toString() {
        return "CodeMsg {code= " + code + ", msg="+ msg + "}";
    }
}
