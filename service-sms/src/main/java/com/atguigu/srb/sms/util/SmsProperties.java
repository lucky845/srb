package com.atguigu.srb.sms.util;

import lombok.Data;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author lucky845
 * @date 2022年03月04日 23:27
 */
@Data
@Component
// 注意prefix要写到最后一个 "." 符号之前
// 调用setter为成员赋值
@ConfigurationProperties(prefix = "rckj.sms")
public class SmsProperties implements InitializingBean {

    private String accountSid;
    private String accountToken;
    private String appId;
    private String serverIp;
    private String ServerPort;

    public static String ACCOUNTS_ID;
    public static String ACCOUNT_TOKEN;
    public static String APPID;
    public static String SERVER_IP;
    public static String SERVER_PORT;

    //当私有成员被赋值后，此方法自动被调用，从而初始化常量
    @Override
    public void afterPropertiesSet() throws Exception {
        ACCOUNTS_ID = accountSid;
        ACCOUNT_TOKEN = accountToken;
        APPID = appId;
        SERVER_IP = serverIp;
        SERVER_PORT = ServerPort;
    }
}
