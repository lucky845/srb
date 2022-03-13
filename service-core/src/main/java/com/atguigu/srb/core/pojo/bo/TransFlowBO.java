package com.atguigu.srb.core.pojo.bo;

import com.atguigu.srb.core.enums.TransTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransFlowBO {

    /**
     * 代理人账号单
     */
    private String agentBillNo;
    /**
     * 绑定编号
     */
    private String bindCode;
    /**
     * 充值金额
     */
    private BigDecimal amount;
    /**
     * 标的状态
     */
    private TransTypeEnum transTypeEnum;
    /**
     * 备注
     */
    private String memo;
}