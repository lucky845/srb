package com.atguigu.srb.core.service;

import com.atguigu.srb.core.pojo.entity.UserLoginRecord;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 用户登录记录表 服务类
 * </p>
 *
 * @author lucky845
 * @since 2022-03-03
 */
public interface UserLoginRecordService extends IService<UserLoginRecord> {

    /**
     * 获取会员登陆日志列表
     * @param userId 用户id
     */
    List<UserLoginRecord> listTop50(Long userId);
}
