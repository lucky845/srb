package com.atguigu.srb.core.service.impl;

import com.atguigu.common.exception.BusinessException;
import com.atguigu.common.result.ResponseEnum;
import com.atguigu.common.util.MD5;
import com.atguigu.srb.core.mapper.UserAccountMapper;
import com.atguigu.srb.core.mapper.UserInfoMapper;
import com.atguigu.srb.core.pojo.entity.UserAccount;
import com.atguigu.srb.core.pojo.entity.UserInfo;
import com.atguigu.srb.core.pojo.vo.RegisterVO;
import com.atguigu.srb.core.service.UserInfoService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
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
}
