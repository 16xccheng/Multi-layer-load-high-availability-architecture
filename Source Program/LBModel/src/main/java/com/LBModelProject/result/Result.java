package com.LBModelProject.result;

/**
 * @author 口是心非
 */

public class Result<T> {
    private int code;
    private String msg;
    private T data;

    /**
     * success
     * @param data
     */
    private Result(T data){
        this.code = 500200;
        this.msg = "success";
        this.data = data;
    }

    /**
     * error
     * @param cm
     */
    private Result(CodeMsg cm){
        if(cm == null){
            return ;
        }
        this.code = cm.getCode();
        this.msg = cm.getMsg();
    }

    public static <T> Result<T> success(T data){
        return new Result<T>(data);
    }

    public static <T> Result<T> error(CodeMsg cm){
        return new Result<T>(cm);
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

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
