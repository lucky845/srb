package com.atguigu.srb.core.service;

import com.atguigu.srb.core.pojo.entity.BorrowInfo;
import com.baomidou.mybatisplus.extension.service.IService;

import java.math.BigDecimal;

/**
 * <p>
 * 借款信息表 服务类
 * </p>
 *
 * @author lucky845
 * @since 2022-03-03
 */
public interface BorrowInfoService extends IService<BorrowInfo> {

    /**
     * 根据用户id获取借款额度
     *
     * @param userId 用户id
     */
    BigDecimal getBorrowAmount(Long userId);

    /**
     * 根据用户id保存借款申请信息
     *
     * @param borrowInfo 借款申请信息
     * @param userId     用户id
     */
    void saveBorrowerInfo(BorrowInfo borrowInfo, Long userId);

    /**
     * 根据借款人id获取借款申请审批状态
     *
     * @param userId 借款人id
     */
    Integer getStatusByUserId(Long userId);
}
