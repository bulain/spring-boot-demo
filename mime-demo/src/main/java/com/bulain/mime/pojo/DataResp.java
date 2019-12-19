package com.bulain.mime.pojo;

import java.io.Serializable;

/**
 * 基础设施
 */
public class DataResp<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    private boolean success;//成功标志
    private String err;//错误代码
    private String msg;//错误文本
    private T data;//业务数据

    public static <T> DataResp<T> ok() {
        DataResp<T> res = new DataResp<T>();
        res.setSuccess(true);
        return res;
    }

    public static <T> DataResp<T> ok(T data) {
        DataResp<T> res = new DataResp<T>();
        res.setSuccess(true);
        res.setData(data);
        return res;
    }

    public static <T> DataResp<T> fail(String err, String msg) {
        DataResp<T> res = new DataResp<T>();
        res.setSuccess(false);
        res.setErr(err);
        res.setMsg(msg);
        return res;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getErr() {
        return err;
    }

    public void setErr(String err) {
        this.err = err;
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
