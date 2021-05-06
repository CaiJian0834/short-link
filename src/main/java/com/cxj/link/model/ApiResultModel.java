package com.cxj.link.model;

import java.io.Serializable;

/**
 * 返回结果包装对象
 *
 * @author cxj
 * @emall 735374036@qq.com
 */
public class ApiResultModel<T> {
    private static final long serialVersionUID = 5576237395711742681L;
    public static final Integer SUCCESS_CODE = 0;
    public static final String MSG_SUCCESS_DESC = "操作成功！";
    public static final Integer ERROR_CODE = -1;
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
        this.code = SUCCESS_CODE;
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

    public int getStamp() {
        return this.stamp;
    }

    public void setStamp(int stamp) {
        this.stamp = stamp;
    }

    public static <T> ApiResultModel<T> SUCCESS() {
        return new ApiResultModel<>();
    }

    public static <T> ApiResultModel<T> SUCCESS(T object) {
        return new ApiResultModel<>(0, MSG_SUCCESS_DESC, object);
    }

    public static <T> ApiResultModel<T> ERROR(String msg) {
        return new ApiResultModel<>(ERROR_CODE, msg, null);
    }

    public static <T> ApiResultModel<T> ERROR(int code, String msg) {
        return new ApiResultModel<>(code, msg, null);
    }

    public boolean isSuccess() {
        return ApiResultModel.SUCCESS_CODE.equals(this.getCode());
    }

    @Override
    public String toString() {
        return "Result [code=" + this.code + ", msg=" + this.msg + "]";
    }
}

