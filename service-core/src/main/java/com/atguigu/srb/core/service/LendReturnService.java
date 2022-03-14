package com.atguigu.srb.core.service;

import com.atguigu.srb.core.pojo.entity.LendReturn;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 还款记录表 服务类
 * </p>
 *
 * @author lucky845
 * @since 2022-03-03
 */
public interface LendReturnService extends IService<LendReturn> {

    /**
     * 获取还款列表
     *
     * @param lendId 借款id
     */
    List<LendReturn> selectByLendId(Long lendId);

    /**
     * 用户还款
     *
     * @param lendReturnId 还款id
     * @param userId       用户id
     */
    String commitReturn(Long lendReturnId, Long userId);

    /**
     * 用户还款异步回调
     *
     * @param paramMap 回调参数
     */
    void notify(Map<String, Object> paramMap);

}
