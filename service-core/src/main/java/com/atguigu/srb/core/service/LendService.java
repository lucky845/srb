package com.atguigu.srb.core.service;

import com.atguigu.srb.core.pojo.entity.BorrowInfo;
import com.atguigu.srb.core.pojo.entity.Lend;
import com.atguigu.srb.core.pojo.vo.BorrowInfoApprovalVO;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 标的准备表 服务类
 * </p>
 *
 * @author lucky845
 * @since 2022-03-03
 */
public interface LendService extends IService<Lend> {

    /**
     * 创建标的
     *
     * @param borrowInfoApprovalVO 借款审批信息
     * @param borrowInfo           借款信息
     */
    void createLend(BorrowInfoApprovalVO borrowInfoApprovalVO, BorrowInfo borrowInfo);

    /**
     * 查询标的列表
     */
    List<Lend> selectList();

    /**
     * 根据标的id获取标的信息
     *
     * @param id 标的id
     */
    Map<String, Object> getLendDetailById(Long id);
}
