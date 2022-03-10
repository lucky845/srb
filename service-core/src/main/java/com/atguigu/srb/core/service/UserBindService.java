package com.atguigu.srb.core.service;

import com.atguigu.srb.core.pojo.entity.UserBind;
import com.atguigu.srb.core.pojo.vo.UserBindVO;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

/**
 * <p>
 * 用户绑定表 服务类
 * </p>
 *
 * @author lucky845
 * @since 2022-03-03
 */
public interface UserBindService extends IService<UserBind> {

    /**
     * 账户绑定提交到托管平台的数据
     *
     * @param userBindVO 用户绑定信息
     * @param userId 用户id
     */
    String commitBindUser(UserBindVO userBindVO, Long userId);

    /**
     * 用户账户绑定异步回调
     * @param paramMap 用户绑定信息
     */
    void notify(Map<String, Object> paramMap);
}
