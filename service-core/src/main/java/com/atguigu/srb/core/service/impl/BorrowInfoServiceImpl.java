package com.atguigu.srb.core.service.impl;

import com.atguigu.common.exception.Assert;
import com.atguigu.common.result.ResponseEnum;
import com.atguigu.srb.core.enums.BorrowInfoStatusEnum;
import com.atguigu.srb.core.enums.BorrowerStatusEnum;
import com.atguigu.srb.core.enums.UserBindEnum;
import com.atguigu.srb.core.mapper.BorrowInfoMapper;
import com.atguigu.srb.core.mapper.IntegralGradeMapper;
import com.atguigu.srb.core.mapper.UserInfoMapper;
import com.atguigu.srb.core.pojo.entity.BorrowInfo;
import com.atguigu.srb.core.pojo.entity.IntegralGrade;
import com.atguigu.srb.core.pojo.entity.UserInfo;
import com.atguigu.srb.core.service.BorrowInfoService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

/**
 * <p>
 * 借款信息表 服务实现类
 * </p>
 *
 * @author lucky845
 * @since 2022-03-03
 */
@Service
public class BorrowInfoServiceImpl extends ServiceImpl<BorrowInfoMapper, BorrowInfo> implements BorrowInfoService {

    @Autowired
    private UserInfoMapper userInfoMapper;

    @Autowired
    private IntegralGradeMapper integralGradeMapper;

    /**
     * 根据用户id获取借款额度
     *
     * @param userId 用户id
     */
    @Override
    public BigDecimal getBorrowAmount(Long userId) {

        // 获取用户积分
        UserInfo userInfo = userInfoMapper.selectById(userId);
        //  LOGIN_MOBILE_ERROR(208, "用户不存在")
        Assert.notNull(userInfo, ResponseEnum.LOGIN_MOBILE_ERROR);
        Integer integral = userInfo.getIntegral();

        // 根据积分查询借款额度
        QueryWrapper<IntegralGrade> integralGradeQueryWrapper = new QueryWrapper<>();
        integralGradeQueryWrapper.le("integral_start", integral);
        integralGradeQueryWrapper.ge("integral_end", integral);
        IntegralGrade integralGradeConfig = integralGradeMapper.selectOne(integralGradeQueryWrapper);
        if (integralGradeConfig == null) {
            return new BigDecimal("0");
        }
        return integralGradeConfig.getBorrowAmount();
    }

    /**
     * 根据用户id保存借款申请信息
     *
     * @param borrowInfo 借款申请信息
     * @param userId     用户id
     */
    @Override
    public void saveBorrowerInfo(BorrowInfo borrowInfo, Long userId) {

        // 获取userInfo的用户数据
        UserInfo userInfo = userInfoMapper.selectById(userId);

        // 获取用户绑定状态 USER_NO_BIND_ERROR(302, "用户未绑定")
        Assert.isTrue(
                userInfo.getBindStatus().intValue() == UserBindEnum.BIND_OK.getStatus(),
                ResponseEnum.USER_NO_BIND_ERROR
        );

        // 判断用户信息是否审批通过 USER_NO_AMOUNT_ERROR(303, "用户信息未审核")
        Assert.isTrue(
                userInfo.getBorrowAuthStatus().intValue() == BorrowerStatusEnum.AUTH_OK.getStatus(),
                ResponseEnum.USER_NO_AMOUNT_ERROR
        );

        // 判断借款额度是否足够 USER_AMOUNT_LESS_ERROR(304, "您的借款额度不足")
        BigDecimal borrowAmount = this.getBorrowAmount(userId);
        Assert.isTrue(
                borrowInfo.getAmount().doubleValue() <= borrowAmount.doubleValue(),
                ResponseEnum.USER_AMOUNT_LESS_ERROR
        );

        // 存储数据
        borrowInfo.setUserId(userId);
        // 百分比转换为小数
        borrowInfo.setBorrowYearRate(borrowInfo.getBorrowYearRate().divide(new BigDecimal(100)));
        borrowInfo.setStatus(BorrowInfoStatusEnum.CHECK_RUN.getStatus());
        baseMapper.insert(borrowInfo);
    }

    /**
     * 根据借款人id获取借款申请审批状态
     *
     * @param userId 借款人id
     */
    @Override
    public Integer getStatusByUserId(Long userId) {
        QueryWrapper<BorrowInfo> borrowInfoQueryWrapper = new QueryWrapper<>();
        borrowInfoQueryWrapper.select("status").eq("user_id", userId);
        List<Object> objects = baseMapper.selectObjs(borrowInfoQueryWrapper);

        if (objects.size() == 0) {
            // 借款人尚未提交信息
            return BorrowInfoStatusEnum.NO_AUTH.getStatus();
        }
        return (Integer) objects.get(0);
    }
}
