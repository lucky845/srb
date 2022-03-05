package com.atguigu.srb.sms;

import com.atguigu.srb.sms.util.SmsProperties;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class UtilsTests {

    @Test
    public void testProperties(){
        System.out.println(SmsProperties.ACCOUNT_TOKEN);
        System.out.println(SmsProperties.ACCOUNTS_ID);
        System.out.println(SmsProperties.APPID);
    }
}