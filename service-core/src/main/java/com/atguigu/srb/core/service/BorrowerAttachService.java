package com.atguigu.srb.core.service;

import com.atguigu.srb.core.pojo.entity.BorrowerAttach;
import com.atguigu.srb.core.pojo.vo.BorrowerAttachVO;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 借款人上传资源表 服务类
 * </p>
 *
 * @author lucky845
 * @since 2022-03-03
 */
public interface BorrowerAttachService extends IService<BorrowerAttach> {

    /**
     * 查询借款人信息列表
     *
     * @param borrowerId 借款人id
     */
    List<BorrowerAttachVO> selectBorrowerAttachVOList(Long borrowerId);

}
