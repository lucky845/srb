package com.atguigu.srb.core.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum TransTypeEnum {
    /**
     * 充值
     */
    RECHARGE(1,"充值"),
    /**\
     * 投标锁定
     */
    INVEST_LOCK(2,"投标锁定"),
    /**
     * 放款解锁
     */
    INVEST_UNLOCK(3,"放款解锁"),
    /**
     * 撤标
     */
    CANCEL_LEND(4,"撤标"),
    /**
     * 放款到账
     */
    BORROW_BACK(5,"放款到账"),
    /**
     * 还款扣减
     */
    RETURN_DOWN(6,"还款扣减"),
    /**
     * 出借回款
     */
    INVEST_BACK(7,"出借回款"),
    /**
     * 提现
     */
    WITHDRAW(8,"提现"),
    ;

    private Integer transType ;
    private String transTypeName;


    public static String getTransTypeName(int transType) {
        for (TransTypeEnum obj : TransTypeEnum.values()) {
            if (transType == obj.getTransType().intValue()) {
                return obj.getTransTypeName();
            }
        }
        return "";
    }

}
