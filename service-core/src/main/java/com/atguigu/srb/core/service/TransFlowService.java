package com.atguigu.srb.core.service;

import com.atguigu.srb.core.pojo.bo.TransFlowBO;
import com.atguigu.srb.core.pojo.entity.TransFlow;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 交易流水表 服务类
 * </p>
 *
 * @author lucky845
 * @since 2022-03-03
 */
public interface TransFlowService extends IService<TransFlow> {

    /**
     * 保存交易流水信息
     *
     * @param transFlowBO 交易流水信息
     */
    void saveTransFlow(TransFlowBO transFlowBO);

    /**
     * 判断流水是否存在
     *
     * @param agentBillNo 流水单号
     */
    boolean isSaveTransFlow(String agentBillNo);

    /**
     * 根据用户id获取流水信息集合
     *
     * @param userId 用户id
     */
    List<TransFlow> selectByUserId(Long userId);
}
