package com.atguigu.srb.core.service;

import com.atguigu.srb.core.pojo.entity.UserInfo;
import com.atguigu.srb.core.pojo.query.UserInfoQuery;
import com.atguigu.srb.core.pojo.vo.LoginVO;
import com.atguigu.srb.core.pojo.vo.RegisterVO;
import com.atguigu.srb.core.pojo.vo.UserIndexVO;
import com.atguigu.srb.core.pojo.vo.UserInfoVO;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 用户基本信息 服务类
 * </p>
 *
 * @author lucky845
 * @since 2022-03-03
 */
public interface UserInfoService extends IService<UserInfo> {

    /**
     * 用户注册
     *
     * @param registerVO 用户注册信息
     */
    void register(RegisterVO registerVO);

    /**
     * 用户登陆
     *
     * @param loginVO 用户登陆信息
     * @param ip      用户访问ip
     */
    UserInfoVO login(LoginVO loginVO, String ip);

    /**
     * 获取会员分页列表
     *
     * @param pageParam     分页参数
     * @param userInfoQuery 查询对象
     */
    IPage<UserInfo> listPage(Page<UserInfo> pageParam, UserInfoQuery userInfoQuery);

    /**
     * 会员锁定
     *
     * @param id     会员id
     * @param status 锁定状态
     */
    void lock(Long id, Integer status);

    /**
     * 校验手机号是否注册
     *
     * @param mobile 手机号
     */
    boolean checkMobile(String mobile);

    /**
     * 根据用户id获取用户个人空间信息
     *
     * @param userId 用户id
     */
    UserIndexVO getIndexUserInfo(Long userId);
}
