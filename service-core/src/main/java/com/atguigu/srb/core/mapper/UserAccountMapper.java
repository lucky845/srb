package com.atguigu.srb.core.mapper;

import com.atguigu.srb.core.pojo.entity.UserAccount;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;

/**
 * <p>
 * 用户账户 Mapper 接口
 * </p>
 *
 * @author lucky845
 * @since 2022-03-03
 */
public interface UserAccountMapper extends BaseMapper<UserAccount> {

    /**
     * 修改账户
     *
     * @param bindCode     绑定编号
     * @param changeAmt    充值金额
     * @param freezeAmount 冻结金额
     */
    void updateAccount(
            @Param("bindCode") String bindCode,
            @Param("amount") BigDecimal changeAmt,
            @Param("freezeAmount") BigDecimal freezeAmount);

}
