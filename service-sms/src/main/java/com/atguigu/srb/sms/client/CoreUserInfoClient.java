package com.atguigu.srb.sms.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @author lucky845
 */
@FeignClient(value = "service-core")
public interface CoreUserInfoClient {

    /**
     * 远程调用core模块 校验手机号是否注册
     * @param mobile 手机号
     */
    @GetMapping("/api/core/userInfo/checkMobile/{mobile}")
    boolean checkMobile(@PathVariable String mobile);

}
