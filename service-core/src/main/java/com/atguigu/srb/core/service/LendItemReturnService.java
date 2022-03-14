package com.atguigu.srb.core.service;

import com.atguigu.srb.core.pojo.entity.LendItemReturn;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 标的出借回款记录表 服务类
 * </p>
 *
 * @author lucky845
 * @since 2022-03-03
 */
public interface LendItemReturnService extends IService<LendItemReturn> {

    /**
     * 获取回款列表
     *
     * @param lendId 借款id
     * @param userId 用户id
     */
    List<LendItemReturn> selectByLendId(Long lendId, Long userId);

    /**
     * 添加还款详情
     *
     * @param lendReturnId 还款id
     */
    List<Map<String, Object>> addReturnDetail(Long lendReturnId);

    /**
     * 根据还款计划id获取对应的回款计划列表
     *
     * @param lendReturnId 还款计划id
     */
    List<LendItemReturn> selectLendItemReturnList(Long lendReturnId);

}
