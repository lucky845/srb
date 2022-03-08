package com.atguigu.srb.core.service.impl;

import com.atguigu.common.exception.Assert;
import com.atguigu.common.exception.BusinessException;
import com.atguigu.common.result.ResponseEnum;
import com.atguigu.common.util.MD5;
import com.atguigu.srb.base.util.JwtUtils;
import com.atguigu.srb.core.mapper.UserAccountMapper;
import com.atguigu.srb.core.mapper.UserInfoMapper;
import com.atguigu.srb.core.mapper.UserLoginRecordMapper;
import com.atguigu.srb.core.pojo.entity.UserAccount;
import com.atguigu.srb.core.pojo.entity.UserInfo;
import com.atguigu.srb.core.pojo.entity.UserLoginRecord;
import com.atguigu.srb.core.pojo.query.UserInfoQuery;
import com.atguigu.srb.core.pojo.vo.LoginVO;
import com.atguigu.srb.core.pojo.vo.RegisterVO;
import com.atguigu.srb.core.pojo.vo.UserInfoVO;
import com.atguigu.srb.core.service.UserInfoService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 * 用户基本信息 服务实现类
 * </p>
 *
 * @author lucky845
 * @since 2022-03-03
 */
@Service
public class UserInfoServiceImpl extends ServiceImpl<UserInfoMapper, UserInfo> implements UserInfoService {

    @Autowired
    private UserAccountMapper userAccountMapper;

    @Autowired
    private UserLoginRecordMapper userLoginRecordMapper;

    /**
     * 用户注册
     *
     * @param registerVO 用户注册信息
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void register(RegisterVO registerVO) {

        String code = registerVO.getCode();
        String mobile = registerVO.getMobile();
        String password = registerVO.getPassword();
        Integer userType = registerVO.getUserType();

        // 判断用户是否已经被注册
        QueryWrapper<UserInfo> userInfoQueryWrapper = new QueryWrapper<>();
        userInfoQueryWrapper.eq("mobile", mobile);
        Integer count = baseMapper.selectCount(userInfoQueryWrapper);
        if (count != 0) {
            // MOBILE_EXIST_ERROR(207, "手机号已被注册")
            throw new BusinessException(ResponseEnum.MOBILE_EXIST_ERROR);
        }

        // 插入用户信息记录: user_info
        UserInfo userInfo = new UserInfo();
        userInfo.setUserType(userType);
        userInfo.setNickName(mobile);
        userInfo.setName(mobile);
        userInfo.setMobile(mobile);
        // 密码加密
        userInfo.setPassword(MD5.encrypt(password));
        userInfo.setStatus(UserInfo.STATUS_NORMAL);
        // 使用base64压缩的头像
        userInfo.setHeadImg(UserInfo.USER_AVATAR);
        // 插入用户信息
        baseMapper.insert(userInfo);

        // 插入用户账户记录: user_account
        UserAccount userAccount = new UserAccount();
        userAccount.setUserId(userInfo.getId());
        userAccountMapper.insert(userAccount);
    }

    /**
     * 用户登陆
     *
     * @param loginVO 用户登陆信息
     * @param ip      用户访问ip
     */
    @Override
    public UserInfoVO login(LoginVO loginVO, String ip) {
        // 获取用户登陆信息
        String mobile = loginVO.getMobile();
        String password = loginVO.getPassword();
        Integer userType = loginVO.getUserType();

        // 用户是否存在
        QueryWrapper<UserInfo> userInfoQueryWrapper = new QueryWrapper<>();
        userInfoQueryWrapper
                .eq("mobile", mobile)
                .eq("user_type", userType);
        UserInfo userInfo = baseMapper.selectOne(userInfoQueryWrapper);
        // LOGIN_MOBILE_ERROR(208, "用户不存在")
        Assert.notNull(userInfo, ResponseEnum.LOGIN_MOBILE_ERROR);

        // 密码是否正确
        // LOGIN_PASSWORD_ERROR(209, "密码错误")
        Assert.equals(MD5.encrypt(password), userInfo.getPassword(), ResponseEnum.LOGIN_PASSWORD_ERROR);

        // 用户是否被禁用
        // LOGIN_LOKED_ERROR(210, "用户被锁定")
        Assert.equals(userInfo.getStatus(), UserInfo.STATUS_NORMAL, ResponseEnum.LOGIN_LOKED_ERROR);

        // 记录登陆日志
        UserLoginRecord userLoginRecord = new UserLoginRecord();
        userLoginRecord.setUserId(userInfo.getId());
        userLoginRecord.setIp(ip);
        userLoginRecordMapper.insert(userLoginRecord);

        // 生成token
        String token = JwtUtils.createToken(userInfo.getId(), userInfo.getName());

        // 组装userInfoVO
        UserInfoVO userInfoVO = new UserInfoVO();
        userInfoVO.setToken(token);
        userInfoVO.setName(userInfo.getName());
        userInfoVO.setNickName(userInfo.getNickName());
        userInfoVO.setHeadImg(userInfo.getHeadImg());
        userInfoVO.setMobile(userInfo.getMobile());
        userInfoVO.setUserType(userType);

        return userInfoVO;
    }

    /**
     * 获取会员分页列表
     *
     * @param pageParam     分页参数
     * @param userInfoQuery 查询对象
     */
    @Override
    public IPage<UserInfo> listPage(Page<UserInfo> pageParam, UserInfoQuery userInfoQuery) {
        // 判断是否有查询条件
        if (userInfoQuery == null) {
            return baseMapper.selectPage(pageParam, null);
        } else {
            String mobile = userInfoQuery.getMobile();
            Integer status = userInfoQuery.getStatus();
            Integer userType = userInfoQuery.getUserType();

            QueryWrapper<UserInfo> userInfoQueryWrapper = new QueryWrapper<>();
            userInfoQueryWrapper
                    .eq(StringUtils.isNotBlank(mobile), "mobile", mobile)
                    .eq(null != status, "status", status)
                    .eq(null != userType, "user_type", userType);

            return baseMapper.selectPage(pageParam, userInfoQueryWrapper);
        }
    }

    /**
     * 会员锁定
     *
     * @param id     会员id
     * @param status 锁定状态
     */
    @Override
    public void lock(Long id, Integer status) {
        UserInfo userInfo = new UserInfo();
        userInfo.setId(id);
        userInfo.setStatus(status);
        baseMapper.updateById(userInfo);
    }

    /**
     * 校验手机号是否注册
     *
     * @param mobile 手机号
     */
    @Override
    public boolean checkMobile(String mobile) {
        QueryWrapper<UserInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("mobile", mobile);
        Integer count = baseMapper.selectCount(queryWrapper);
        return count > 0;
    }
}
