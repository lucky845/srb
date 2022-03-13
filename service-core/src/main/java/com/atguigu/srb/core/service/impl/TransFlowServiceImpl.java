package com.atguigu.srb.core.service.impl;

import com.atguigu.srb.core.mapper.UserInfoMapper;
import com.atguigu.srb.core.pojo.bo.TransFlowBO;
import com.atguigu.srb.core.pojo.entity.TransFlow;
import com.atguigu.srb.core.mapper.TransFlowMapper;
import com.atguigu.srb.core.pojo.entity.UserInfo;
import com.atguigu.srb.core.service.TransFlowService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 交易流水表 服务实现类
 * </p>
 *
 * @author lucky845
 * @since 2022-03-03
 */
@Service
public class TransFlowServiceImpl extends ServiceImpl<TransFlowMapper, TransFlow> implements TransFlowService {

    @Autowired
    private UserInfoMapper userInfoMapper;

    /**
     * 保存交易流水信息
     *
     * @param transFlowBO 交易流水信息
     */
    @Override
    public void saveTransFlow(TransFlowBO transFlowBO) {

        // 获取用户基本信息,user_id
        QueryWrapper<UserInfo> userInfoQueryWrapper = new QueryWrapper<>();
        userInfoQueryWrapper.eq("bind_code", transFlowBO.getBindCode());
        UserInfo userInfo = userInfoMapper.selectOne(userInfoQueryWrapper);

        // 存储交易流水信息
        TransFlow transFlow = new TransFlow();
        transFlow.setUserId(userInfo.getId());
        transFlow.setUserName(userInfo.getName());
        transFlow.setTransNo(transFlowBO.getAgentBillNo());
        transFlow.setTransType(transFlowBO.getTransTypeEnum().getTransType());
        transFlow.setTransTypeName(transFlowBO.getTransTypeEnum().getTransTypeName());
        transFlow.setTransAmount(transFlowBO.getAmount());
        transFlow.setMemo(transFlowBO.getMemo());
        baseMapper.insert(transFlow);
    }

    /**
     * 判断流水是否存在
     *
     * @param agentBillNo 流水单号
     */
    @Override
    public boolean isSaveTransFlow(String agentBillNo) {
        QueryWrapper<TransFlow> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("trans_no", agentBillNo);
        Integer count = baseMapper.selectCount(queryWrapper);
        if (count > 0) {
            return true;
        }
        return false;
    }
}
