package com.atguigu.srb.sms.service;

import java.util.Map;

public interface SmsService {

    /**
     * 发送短信功能
     *
     * @param mobile 手机号
     * @param param 其他参数
     */
    void send(String mobile, Map<String, Object> param);

}