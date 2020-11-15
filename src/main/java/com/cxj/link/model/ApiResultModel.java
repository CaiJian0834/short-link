package com.cxj.link.model;


import java.io.Serializable;

/**
 * 返回结果包装对象
 * @author cxj
 * @emall 735374036@qq.com
 */
public class ApiResultModel<T> implements Serializable {
    private static final long serialVersionUID = 5576237395711742681L;
    public static final Integer SUCCESS = 0;
    public static final String MSG_SUCCESS_DESC = "操作成功！";
    public static final Integer ERROR = -1;
    public static final String MSG_ERROR_DESC = "系统忙！";
    public static final Integer ILLEGAL = -2;
    public static final String MSG_ILLEGAL_DESC = "参数不正确！";


    private int code = 0;
    private String msg = MSG_SUCCESS_DESC;
    private T data;
    private int stamp = 0;

    public ApiResultModel() {
    }

    public ApiResultModel(int code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public ApiResultModel(T data) {
        this.code = SUCCESS;
        this.msg = MSG_SUCCESS_DESC;
        this.data = data;
    }

    public int getCode() {
        return this.code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return this.msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return this.data == null ? null : this.data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getCurrentTimeStamp() {
        return String.valueOf(System.currentTimeMillis());
    }

    @Override
    public String toString() {
        return "Result [code=" + this.code + ", msg=" + this.msg + "]";
    }

    public int getStamp() {
        return this.stamp;
    }

    public void setStamp(int stamp) {
        this.stamp = stamp;
    }

    public static <T> ApiResultModel<T> success() {
        return new ApiResultModel();
    }

    public static <T> ApiResultModel<T> success(T object) {
        return new ApiResultModel(0, MSG_SUCCESS_DESC, object);
    }

    public static <T> ApiResultModel<T> success1(T object) {
        return new ApiResultModel(1, MSG_SUCCESS_DESC, object);
    }

    public static <T> ApiResultModel<T> successMsg(String msg) {
        return new ApiResultModel(1, msg, null);
    }

    public static <T> ApiResultModel<T> error(String msg) {
        return new ApiResultModel(ERROR, msg, null);
    }

    public static <T> ApiResultModel<T> error(int code, String msg) {
        return new ApiResultModel(code, msg, null);
    }

    public boolean isSuccess() {
        return this.getCode() == ApiResultModel.SUCCESS;
    }
}

