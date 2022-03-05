package com.atguigu.common.result;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

/**
 * @author lucky845
 * @date 2022年03月03日 10:48
 */
@ToString
@Getter
@AllArgsConstructor
public enum ResponseEnum {

    /**
     * 请求成功
     */
    SUCCESS(0, "成功"),
    /**
     * 请求失败
     */
    ERROR(-1, "服务器内部错误"),
    /**
     * sql语法错误
     */
    BAD_SQL_GRAMMAR_ERROR(-101, "sql语法错误"),
    /**
     * servlet请求异常
     */
    SERVLET_ERROR(-102, "servlet请求异常"),
    /**
     * 文件上传错误
     */
    UPLOAD_ERROR(-103, "文件上传错误"),
    /**
     * 数据导出异常
     */
    EXPORT_DATA_ERROR(104, "数据导出失败"),
    /**
     * 借款额度不能为空
     */
    BORROW_AMOUNT_NULL_ERROR(-201, "借款额度不能为空"),
    /**
     * 手机号码不能为空
     */
    MOBILE_NULL_ERROR(-202, "手机号码不能为空"),
    /**
     * 手机号码不正确
     */
    MOBILE_ERROR(-203, "手机号码不正确"),
    /**
     * 密码不能为空
     */
    PASSWORD_NULL_ERROR(204, "密码不能为空"),
    /**
     * 验证码不能为空
     */
    CODE_NULL_ERROR(205, "验证码不能为空"),
    /**
     * 验证码错误
     */
    CODE_ERROR(206, "验证码错误"),
    /**
     * 手机号已被注册
     */
    MOBILE_EXIST_ERROR(207, "手机号已被注册"),
    /**
     * 用户不存在
     */
    LOGIN_MOBILE_ERROR(208, "用户不存在"),
    /**
     * 密码错误
     */
    LOGIN_PASSWORD_ERROR(209, "密码错误"),
    /**
     * 用户被锁定
     */
    LOGIN_LOKED_ERROR(210, "用户被锁定"),
    /**
     * 用户未登陆
     */
    LOGIN_AUTH_ERROR(-211, "未登录"),
    /**
     * 身份证号码已绑定
     */
    USER_BIND_IDCARD_EXIST_ERROR(-301, "身份证号码已绑定"),
    /**
     * 用户未绑定
     */
    USER_NO_BIND_ERROR(302, "用户未绑定"),
    /**
     * 用户信息未审核
     */
    USER_NO_AMOUNT_ERROR(303, "用户信息未审核"),
    /**
     * 您的借款额度不足
     */
    USER_AMOUNT_LESS_ERROR(304, "您的借款额度不足"),
    /**
     * 当前状态无法投标
     */
    LEND_INVEST_ERROR(305, "当前状态无法投标"),
    /**
     * 已满标,无法投注
     */
    LEND_FULL_SCALE_ERROR(306, "已满标，无法投标"),
    /**
     * 余额不足,请充值
     */
    NOT_SUFFICIENT_FUNDS_ERROR(307, "余额不足，请充值"),
    /**
     * 统一下单错误
     */
    PAY_UNIFIEDORDER_ERROR(401, "统一下单错误"),
    /**
     * 短信业务限流
     */
    ALIYUN_SMS_LIMIT_CONTROL_ERROR(-502, "短信发送过于频繁"),
    /**
     * 短信发送失败
     */
    ALIYUN_SMS_ERROR(-503, "短信发送失败"),
    /**
     * 回调参数不正确
     */
    WEIXIN_CALLBACK_PARAM_ERROR(-601, "回调参数不正确"),
    /**
     * 获取access_token失败
     */
    WEIXIN_FETCH_ACCESSTOKEN_ERROR(-602, "获取access_token失败"),
    /**
     * 获取用户信息失败
     */
    WEIXIN_FETCH_USERINFO_ERROR(-603, "获取用户信息失败"),

    /**
     * 阿里云响应失败
     */
    ALIYUN_RESPONSE_FAIL(-501, "阿里云响应失败");

    /**
     * 响应状态码
     */
    private Integer code;
    /**
     * 响应消息
     */
    private String message;


}
