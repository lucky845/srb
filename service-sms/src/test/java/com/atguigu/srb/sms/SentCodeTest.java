package com.atguigu.srb.sms;

import com.atguigu.common.util.RandomUtils;
import com.atguigu.srb.sms.service.SmsService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.HashMap;

/**
 * @author lucky845
 * @date 2022年03月05日 8:29
 */
@SpringBootTest
public class SentCodeTest {

    @Resource
    private SmsService smsService;

    @Test
    public void sent(){
        HashMap<String, Object> map = new HashMap<>();
        map.put("code", RandomUtils.getFourBitRandom());

        smsService.send("你的手机号", map);
    }

}
