package com.atguigu.srb.core.service.impl;

import com.atguigu.srb.core.mapper.UserLoginRecordMapper;
import com.atguigu.srb.core.pojo.entity.UserLoginRecord;
import com.atguigu.srb.core.service.UserLoginRecordService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 用户登录记录表 服务实现类
 * </p>
 *
 * @author lucky845
 * @since 2022-03-03
 */
@Service
public class UserLoginRecordServiceImpl extends ServiceImpl<UserLoginRecordMapper, UserLoginRecord> implements UserLoginRecordService {


    /**
     * 获取会员登陆日志列表
     *
     * @param userId 用户id
     */
    @Override
    public List<UserLoginRecord> listTop50(Long userId) {

        QueryWrapper<UserLoginRecord> userLoginRecordQueryWrapper = new QueryWrapper<>();
        userLoginRecordQueryWrapper
                .eq("user_id",userId)
                .orderByDesc("id")
                .last("limit 50");
        return baseMapper.selectList(userLoginRecordQueryWrapper);
    }
}
