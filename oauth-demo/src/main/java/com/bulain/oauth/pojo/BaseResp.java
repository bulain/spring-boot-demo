package com.bulain.oauth.pojo;

import java.io.Serializable;

/**
 * 返回值基类 
 * @author Bulain
 */
public class BaseResp implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private boolean success; //成功标志
    private String err;//错误代码
    private String msg;//错误文本

    public static BaseResp ok() {
        BaseResp res = new BaseResp();
        res.setSuccess(true);
        return res;
    }

    public static BaseResp fail(String err, String msg) {
        BaseResp res = new BaseResp();
        res.setSuccess(false);
        res.setErr(err);
        res.setMsg(msg);
        return res;
    }

    public static BaseResp fail(Exception e) {
        String msg = e.getMessage();
        if (msg == null) {
            msg = e.getClass().getName();
        }

        BaseResp res = new BaseResp();
        res.setSuccess(false);
        res.setErr("E40010");
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

}
