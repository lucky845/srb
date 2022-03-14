package com.atguigu.srb.core.service.impl;

import com.alibaba.fastjson.JSON;
import com.atguigu.common.exception.Assert;
import com.atguigu.common.result.ResponseEnum;
import com.atguigu.srb.base.util.LendNoUtils;
import com.atguigu.srb.core.enums.TransTypeEnum;
import com.atguigu.srb.core.hfb.FormHelper;
import com.atguigu.srb.core.hfb.HfbConst;
import com.atguigu.srb.core.hfb.RequestHelper;
import com.atguigu.srb.core.mapper.UserAccountMapper;
import com.atguigu.srb.core.mapper.UserInfoMapper;
import com.atguigu.srb.core.pojo.bo.TransFlowBO;
import com.atguigu.srb.core.pojo.entity.UserAccount;
import com.atguigu.srb.core.pojo.entity.UserInfo;
import com.atguigu.srb.core.service.TransFlowService;
import com.atguigu.srb.core.service.UserAccountService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.beans.Transient;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * 用户账户 服务实现类
 * </p>
 *
 * @author lucky845
 * @since 2022-03-03
 */
@Slf4j
@Service
public class UserAccountServiceImpl extends ServiceImpl<UserAccountMapper, UserAccount> implements UserAccountService {

    @Autowired
    private UserInfoMapper userInfoMapper;

    @Autowired
    private TransFlowService transFlowService;

    /**
     * 提交用户充值信息
     *
     * @param chargeAmt 充值金额
     * @param userId    用户id
     */
    @Override
    public String commitChange(BigDecimal chargeAmt, Long userId) {

        // 获取充值人绑定协议号
        UserInfo userInfo = userInfoMapper.selectById(userId);
        String bindCode = userInfo.getBindCode();
        // 判断用户绑定状态 USER_NO_BIND_ERROR(302, "用户未绑定")
        Assert.notEmpty(bindCode, ResponseEnum.USER_NO_BIND_ERROR);

        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("agentId", HfbConst.AGENT_ID);
        // 充值单号
        paramMap.put("agentBillNo", LendNoUtils.getChargeNo());
        // 用户绑定协议号
        paramMap.put("bindCode", bindCode);
        // 充值金额
        paramMap.put("chargeAmt", chargeAmt);
        // 手续费
        paramMap.put("feeAmt", new BigDecimal("0"));
        // 检查常量是否正确
        // 异步调用地址
        paramMap.put("notifyUrl", HfbConst.RECHARGE_NOTIFY_URL);
        // 返回地址
        paramMap.put("returnUrl", HfbConst.RECHARGE_RETURN_URL);
        // 时间戳
        paramMap.put("timestamp", RequestHelper.getTimestamp());
        // 签名
        String sign = RequestHelper.getSign(paramMap);
        paramMap.put("sign", sign);

        // 构建自动提交表单
        String formStr = FormHelper.buildForm(HfbConst.RECHARGE_URL, paramMap);
        return formStr;
    }

    /**
     * 用户充值异步回调
     *
     * @param paramMap 用户充值参数
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public String notify(Map<String, Object> paramMap) {

        log.info("充值成功: " + JSON.toJSONString(paramMap));

        // 判断交易流水是否存在
        String agentBillNo1 = (String) paramMap.get("agentBillNo");
        boolean isSave = transFlowService.isSaveTransFlow(agentBillNo1);
        if (isSave) {
            log.warn("幂等性返回");
            return "success";
        }

        // 充值人绑定协议号
        String bindCode = (String) paramMap.get("bindCode");
        // 充值金额
        String chargeAmt = (String) paramMap.get("chargeAmt");

        // 优化
        baseMapper.updateAccount(bindCode, new BigDecimal(chargeAmt), new BigDecimal(0));

        // 增加交易流水
        // 商户充值订单号
        String agentBillNo = (String) paramMap.get("agentBillNo");
        TransFlowBO transFlowBO = new TransFlowBO(
                agentBillNo,
                bindCode,
                new BigDecimal(chargeAmt),
                TransTypeEnum.RECHARGE,
                "充值"
        );
        transFlowService.saveTransFlow(transFlowBO);

        return "success";
    }

    /**
     * 根据用户id查询用户账户余额
     *
     * @param userId 用户id
     */
    @Override
    public BigDecimal getAccountByUserId(Long userId) {
        UserAccount userAccount = baseMapper.selectOne(new QueryWrapper<UserAccount>().eq("user_id", userId));
        return userAccount.getAmount();
    }
}
