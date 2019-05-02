package com.info.back.utils;

import lombok.Data;

/**
 * @author cqry_2016
 * @create 2018-08-23 18:49
 */
@Data
public class Result<T> {

    public static Result OK = new Result();

    public static String SUCCESS = "200";
    /**
     * 返回代码  默认成功
     */
    private String code = SUCCESS;

    /**
     * 提示信息 默认提示语
     */
    private String msg = "操作成功";

    private T data;

    public Result() {
    }
    public boolean isSuccessed() {
        return SUCCESS.equals(getCode());
    }

    public boolean isFail() {
        return !SUCCESS.equals(getCode());
    }

    public Result(T data) {
        this.data = data;
    }

    public Result(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public static Result success(String code, String msg) {
        return new Result(code, msg);
    }

    public static Result success(Object data) {
        return new Result(data);
    }

    public static Result error(String msg) {
        return new Result("fail", msg);
    }

}
