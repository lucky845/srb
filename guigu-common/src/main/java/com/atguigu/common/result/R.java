package com.atguigu.common.result;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

/**
 * @author lucky845
 * @date 2022年03月03日 10:50
 */
@Data
public class R {

    /**
     * 业务响应码
     */
    private Integer code;
    /**
     * 响应消息
     */
    private String message;
    /**
     * 返回的数据
     */
    private Map<String, Object> data = new HashMap<>();

    /**
     * 私有构造器
     */
    private R() {

    }

    /**
     * 返回成功
     */
    public static R ok() {
        R r = new R();
        r.setCode(ResponseEnum.SUCCESS.getCode());
        r.setMessage(ResponseEnum.SUCCESS.getMessage());
        return r;
    }

    public static R error() {
        R r = new R();
        r.setCode(ResponseEnum.ERROR.getCode());
        r.setMessage(ResponseEnum.ERROR.getMessage());
        return r;
    }

    /**
     * 设置特定结果
     *
     * @param responseEnum 响应枚举类对象
     */
    public static R setResult(ResponseEnum responseEnum) {
        R r = new R();
        r.setCode(responseEnum.getCode());
        r.setMessage(responseEnum.getMessage());
        return r;
    }

    public R message(String message) {
        this.setMessage(message);
        return this;
    }

    public R code(Integer code) {
        this.setCode(code);
        return this;
    }

    public R data(String key, Object value) {
        this.data.put(key, value);
        return this;
    }

    public R data(Map<String, Object> map) {
        this.setData(map);
        return this;
    }

}
