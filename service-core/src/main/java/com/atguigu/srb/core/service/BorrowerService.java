package com.atguigu.srb.core.service;

import com.atguigu.srb.core.pojo.entity.Borrower;
import com.atguigu.srb.core.pojo.vo.BorrowerApprovalVO;
import com.atguigu.srb.core.pojo.vo.BorrowerDetailVO;
import com.atguigu.srb.core.pojo.vo.BorrowerVO;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
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

    /**
     * 获取借款人分页列表
     *
     * @param pageParam 分页信息
     * @param keyword   查询关键字
     */
    IPage<Borrower> listPage(Page<Borrower> pageParam, String keyword);

    /**
     * 获取借款人信息
     *
     * @param id 借款人id
     */
    BorrowerDetailVO getBorrowerDetailVOById(Long id);

    /**
     * 借款额度审批
     *
     * @param borrowerApprovalVO 借款人审批信息
     */
    void approval(BorrowerApprovalVO borrowerApprovalVO);

}
