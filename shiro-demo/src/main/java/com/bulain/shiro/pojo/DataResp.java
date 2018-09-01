package com.bulain.shiro.pojo;

/**
 * 数据返回值基类 
 * @author Bulain
 */
public class DataResp extends BaseResp {
    private static final long serialVersionUID = 1L;
    
    private Object data; //具体的数据

    public static <T> DataResp ok(T data) {
        DataResp res = new DataResp();
        res.setSuccess(true);
        res.setData(data);
        return res;
    }

    public static DataResp fail(String err, String msg) {
        DataResp res = new DataResp();
        res.setSuccess(false);
        res.setErr(err);
        res.setMsg(msg);
        return res;
    }

    public static DataResp fail(Exception e) {
        String msg = e.getMessage();
        if (msg == null) {
            msg = e.getClass().getName();
        }

        DataResp res = new DataResp();
        res.setSuccess(false);
        res.setErr("E40010");
        res.setMsg(msg);
        return res;
    }

    @SuppressWarnings("unchecked")
    public <T> T getData() {
        return (T) data;
    }
    public <T> void setData(T data) {
        this.data = data;
    }

}
