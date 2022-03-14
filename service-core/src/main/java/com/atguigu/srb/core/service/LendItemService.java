package com.atguigu.srb.core.service;

import com.atguigu.srb.core.pojo.entity.LendItem;
import com.atguigu.srb.core.pojo.vo.InvestVO;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 标的出借记录表 服务类
 * </p>
 *
 * @author lucky845
 * @since 2022-03-03
 */
public interface LendItemService extends IService<LendItem> {

    /**
     * 会员投资提交数据
     *
     * @param investVO 会员投资信息
     */
    String commitInvest(InvestVO investVO);

    /**
     * 会员投资异步回调
     *
     * @param paramMap 用户投资回调参数
     */
    void notify(Map<String, Object> paramMap);

    /**
     * 根据lendId获取投资记录
     *
     * @param lendId 订单id
     * @param status 状态
     */
    List<LendItem> selectByLendId(Long lendId, Integer status);
}
