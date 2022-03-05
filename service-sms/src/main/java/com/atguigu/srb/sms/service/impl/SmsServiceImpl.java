package com.atguigu.srb.sms.service.impl;

import com.atguigu.srb.sms.service.SmsService;
import com.atguigu.srb.sms.util.SmsProperties;
import com.cloopen.rest.sdk.BodyType;
import com.cloopen.rest.sdk.CCPRestSmsSDK;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author lucky845
 * @date 2022年03月04日 23:44
 */
@Slf4j
@Service
public class SmsServiceImpl implements SmsService {
    /**
     * 发送短信功能
     *
     * @param mobile 手机号
     * @param param  其他参数
     */
    @Override
    public void send(String mobile, Map<String, Object> param) {
        CCPRestSmsSDK ccpRestSmsSDK = new CCPRestSmsSDK();
        ccpRestSmsSDK.setAccount(SmsProperties.ACCOUNTS_ID, SmsProperties.ACCOUNT_TOKEN);
        ccpRestSmsSDK.setAppId(SmsProperties.APPID);
        ccpRestSmsSDK.setBodyType(BodyType.Type_JSON);
        ccpRestSmsSDK.init(SmsProperties.SERVER_IP, SmsProperties.SERVER_PORT);
        String[] datas = {(String) param.get("code"), "5"};
        HashMap<String, Object> result = ccpRestSmsSDK.sendTemplateSMS(mobile, "1", datas);
        log.info("SDKTestGetSubAccounts  result = " + result);
        if ("000000".equals(result.get("statusCode"))){
            // 正常返回输出data包体信息(map)
            HashMap<String, Object> data = (HashMap<String, Object>) result.get("data");
            Set<String> keySet = data.keySet();
            for (String key : keySet) {
                Object obj = data.get(key);
                log.info(key + " = " + obj);
            }
        }else{
            // 异常返回输出错误码和错误信息
            log.error("错误码= " + result.get("statusCode") + "错误信息= " + result.get("statusMsg"));
        }
    }
}
