package com.klook.hotel.task.config;

public class Result<T> {

    private String msg;

    private Integer code;

    private T data;

    public Result(String msg, Integer code) {
        this.msg = msg;
        this.code = code;
    }

    public Result(String msg, Integer code, T data) {
        this.msg = msg;
        this.code = code;
        this.data = data;
    }

    public static <T> Result<T> success(T t) {
        return new Result<T>("操作成功",200, t);
    }

    public static <T> Result<T> error(T t) {
        return new Result<T>("操作失败",300, t);
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "Result{" +
                "msg='" + msg + '\'' +
                ", code=" + code +
                ", data=" + data +
                '}';
    }
}
