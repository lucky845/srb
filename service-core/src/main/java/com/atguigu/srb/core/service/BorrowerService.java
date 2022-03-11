package com.atguigu.srb.core.service;

import com.atguigu.srb.core.pojo.entity.Borrower;
import com.atguigu.srb.core.pojo.vo.BorrowerVO;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 借款人 服务类
 * </p>
 *
 * @author lucky845
 * @since 2022-03-03
 */
public interface BorrowerService extends IService<Borrower> {

    /**
     * 保存借款人信息
     *
     * @param borrowerVO 借款人信息
     * @param userId     用户id
     */
    void saveBorrowerVOByUserId(BorrowerVO borrowerVO, Long userId);

    /**
     * 根据用户id获取用户认证状态
     *
     * @param userId 用户id
     */
    Integer getStatusByUserId(Long userId);
}
