package com.atguigu.srb.core.service;

import com.atguigu.srb.core.pojo.entity.LendReturn;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

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

}
