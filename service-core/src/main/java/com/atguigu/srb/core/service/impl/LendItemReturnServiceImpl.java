package com.atguigu.srb.core.service.impl;

import com.atguigu.srb.core.mapper.LendItemMapper;
import com.atguigu.srb.core.mapper.LendItemReturnMapper;
import com.atguigu.srb.core.mapper.LendMapper;
import com.atguigu.srb.core.mapper.LendReturnMapper;
import com.atguigu.srb.core.pojo.entity.Lend;
import com.atguigu.srb.core.pojo.entity.LendItem;
import com.atguigu.srb.core.pojo.entity.LendItemReturn;
import com.atguigu.srb.core.pojo.entity.LendReturn;
import com.atguigu.srb.core.service.LendItemReturnService;
import com.atguigu.srb.core.service.UserBindService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 标的出借回款记录表 服务实现类
 * </p>
 *
 * @author lucky845
 * @since 2022-03-03
 */
@Service
public class LendItemReturnServiceImpl extends ServiceImpl<LendItemReturnMapper, LendItemReturn> implements LendItemReturnService {

    @Autowired
    private UserBindService userBindService;

    @Autowired
    private LendItemMapper lendItemMapper;

    @Autowired
    private LendMapper lendMapper;

    @Autowired
    private LendReturnMapper lendReturnMapper;


    /**
     * 获取回款列表
     *
     * @param lendId 借款id
     * @param userId 用户id
     */
    @Override
    public List<LendItemReturn> selectByLendId(Long lendId, Long userId) {
        QueryWrapper<LendItemReturn> queryWrapper = new QueryWrapper<>();
        queryWrapper
                .eq("lend_id", lendId)
                .eq("invest_user_id", userId)
                .orderByAsc("current_period");
        return baseMapper.selectList(queryWrapper);
    }

    /**
     * 添加还款详情
     *
     * @param lendReturnId 还款id
     */
    @Override
    public List<Map<String, Object>> addReturnDetail(Long lendReturnId) {

        // 获取还款记录
        LendReturn lendReturn = lendReturnMapper.selectById(lendReturnId);
        // 获取标的信息
        Lend lend = lendMapper.selectById(lendReturn.getLendId());

        // 根据还款id获取回款列表
        List<LendItemReturn> lendItemReturnList = this.selectLendItemReturnList(lendReturnId);
        ArrayList<Map<String, Object>> lendItemReturnDetailList = new ArrayList<>();
        for (LendItemReturn lendItemReturn : lendItemReturnList) {
            LendItem lendItem = lendItemMapper.selectById(lendItemReturn.getLendItemId());
            String bindCode = userBindService.getBindCodeByUserId(lendItem.getInvestUserId());

            HashMap<String, Object> map = new HashMap<>();
            // 项目编号
            //项目编号
            map.put("agentProjectCode", lend.getLendNo());
            //出借编号
            map.put("voteBillNo", lendItem.getLendItemNo());
            //收款人（出借人）
            map.put("toBindCode", bindCode);
            //还款金额
            map.put("transitAmt", lendItemReturn.getTotal());
            //还款本金
            map.put("baseAmt", lendItemReturn.getPrincipal());
            //还款利息
            map.put("benifitAmt", lendItemReturn.getInterest());
            //商户手续费
            map.put("feeAmt", new BigDecimal("0"));

            lendItemReturnDetailList.add(map);
        }
        return lendItemReturnDetailList;
    }

    /**
     * 根据还款计划id获取对应的回款计划列表
     *
     * @param lendReturnId 还款计划id
     */
    @Override
    public List<LendItemReturn> selectLendItemReturnList(Long lendReturnId) {
        QueryWrapper<LendItemReturn> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("lend_return_id", lendReturnId);
        List<LendItemReturn> lendItemReturnList = baseMapper.selectList(queryWrapper);
        return lendItemReturnList;
    }
}
