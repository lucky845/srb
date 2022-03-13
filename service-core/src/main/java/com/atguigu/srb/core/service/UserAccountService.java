package com.atguigu.srb.core.service;

import com.atguigu.srb.core.pojo.entity.UserAccount;
import com.baomidou.mybatisplus.extension.service.IService;

import java.math.BigDecimal;
import java.util.Map;

/**
 * <p>
 * 用户账户 服务类
 * </p>
 *
 * @author lucky845
 * @since 2022-03-03
 */
public interface UserAccountService extends IService<UserAccount> {

    /**
     * 提交用户充值信息
     *
     * @param chargeAmt 充值金额
     * @param userId    用户id
     */
    String commitChange(BigDecimal chargeAmt, Long userId);

    /**
     * 用户充值异步回调
     *
     * @param paramMap 用户充值参数
     */
    String notify(Map<String, Object> paramMap);
}
