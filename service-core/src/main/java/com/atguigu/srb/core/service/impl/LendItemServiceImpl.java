package com.atguigu.srb.core.service.impl;

import com.atguigu.common.exception.Assert;
import com.atguigu.common.result.ResponseEnum;
import com.atguigu.srb.base.util.LendNoUtils;
import com.atguigu.srb.core.enums.LendStatusEnum;
import com.atguigu.srb.core.enums.TransTypeEnum;
import com.atguigu.srb.core.hfb.FormHelper;
import com.atguigu.srb.core.hfb.HfbConst;
import com.atguigu.srb.core.hfb.RequestHelper;
import com.atguigu.srb.core.mapper.LendItemMapper;
import com.atguigu.srb.core.mapper.LendMapper;
import com.atguigu.srb.core.mapper.UserAccountMapper;
import com.atguigu.srb.core.pojo.bo.TransFlowBO;
import com.atguigu.srb.core.pojo.entity.Lend;
import com.atguigu.srb.core.pojo.entity.LendItem;
import com.atguigu.srb.core.pojo.vo.InvestVO;
import com.atguigu.srb.core.service.*;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 标的出借记录表 服务实现类
 * </p>
 *
 * @author lucky845
 * @since 2022-03-03
 */
@Slf4j
@Service
public class LendItemServiceImpl extends ServiceImpl<LendItemMapper, LendItem> implements LendItemService {

    @Autowired
    private LendMapper lendMapper;

    @Autowired
    private LendService lendService;

    @Autowired
    private UserAccountService userAccountService;

    @Autowired
    private UserBindService userBindService;

    @Autowired
    private TransFlowService transFlowService;

    @Autowired
    private UserAccountMapper userAccountMapper;

    /**
     * 会员投资提交数据
     *
     * @param investVO 会员投资信息
     */
    @Override
    public String commitInvest(InvestVO investVO) {

        // =============参数校验===========
        Long lendId = investVO.getLendId();
        // 获取标的信息
        Lend lend = lendMapper.selectById(lendId);

        // 标的状态必须为募资中
        Assert.isTrue(
                lend.getStatus().intValue() == LendStatusEnum.INVEST_RUN.getStatus().intValue(),
                ResponseEnum.LEND_INVEST_ERROR
        );

        // 标的不能超卖： （已投资金 + 本次投资金额）>= 标的金额 （超卖）
        BigDecimal sum = lend.getInvestAmount().add(new BigDecimal(investVO.getInvestAmount()));
        Assert.isTrue(
                sum.doubleValue() <= lend.getAmount().doubleValue(),
                ResponseEnum.LEND_FULL_SCALE_ERROR
        );

        // 账户可用余额充足： 当前用户的余额 >= 当前用户的投资金额 （可以投资）
        Long investUserId = investVO.getInvestUserId();
        // 获取当前投资者的账户余额
        BigDecimal amount = userAccountService.getAccountByUserId(investUserId);
        Assert.isTrue(
                amount.doubleValue() >= Double.parseDouble(investVO.getInvestAmount()),
                ResponseEnum.NOT_SUFFICIENT_FUNDS_ERROR
        );

        // ========在平台中生成投资信息
        // 标的下的投资信息
        LendItem lendItem = new LendItem();
        // 投资人id
        lendItem.setInvestUserId(investUserId);
        // 投资人名字
        lendItem.setInvestName(investVO.getInvestName());
        // 投资条目编号(一个lend对应一个或多个lendItem)
        String lendItemNo = LendNoUtils.getLendItemNo();
        lendItem.setLendItemNo(lendItemNo);
        // 对应的标的id
        lendItem.setLendId(investVO.getLendId());
        // 此笔投资金额
        lendItem.setInvestAmount(new BigDecimal(investVO.getInvestAmount()));
        // 年化收益
        lendItem.setLendYearRate(lend.getLendYearRate());
        // 投资时间
        lendItem.setInvestTime(LocalDateTime.now());
        // 开始时间
        lendItem.setLendStartDate(lend.getLendStartDate());
        // 结束时间
        lendItem.setLendEndDate(lend.getLendEndDate());
        // 预期收益
        BigDecimal expectAmount = lendService.getInterestCount(
                lendItem.getInvestAmount(),
                lendItem.getLendYearRate(),
                lend.getPeriod(),
                lend.getReturnMethod());
        lendItem.setExpectAmount(expectAmount);
        // 实际收益
        lendItem.setRealAmount(new BigDecimal(0));
        // 默认状态: 刚刚创建
        lendItem.setStatus(0);
        baseMapper.insert(lendItem);

        // =======组装投资相关的参数，提交到汇付宝资金托管平台=========
        // =======在托管平台同步用户的投资信息，修改用户的账户资金信息========

        // 获取投资人的绑定协议号
        String bindCode = userBindService.getBindCodeByUserId(investUserId);
        // 获取借款人的绑定协议号
        String benefitBindCode = userBindService.getBindCodeByUserId(lend.getUserId());

        //封装提交至汇付宝的参数
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("agentId", HfbConst.AGENT_ID);
        paramMap.put("voteBindCode", bindCode);
        paramMap.put("benefitBindCode",benefitBindCode);
        paramMap.put("agentProjectCode", lend.getLendNo());//项目标号
        paramMap.put("agentProjectName", lend.getTitle());
        // 在资金托管平台上的投资订单的唯一编号，要和lendItemNo保持一致。
        paramMap.put("agentBillNo", lendItemNo);//订单编号
        paramMap.put("voteAmt", investVO.getInvestAmount());
        paramMap.put("votePrizeAmt", "0");
        paramMap.put("voteFeeAmt", "0");
        paramMap.put("projectAmt", lend.getAmount()); //标的总金额
        paramMap.put("note", "");
        paramMap.put("notifyUrl", HfbConst.INVEST_NOTIFY_URL); //检查常量是否正确
        paramMap.put("returnUrl", HfbConst.INVEST_RETURN_URL);
        paramMap.put("timestamp", RequestHelper.getTimestamp());
        String sign = RequestHelper.getSign(paramMap);
        paramMap.put("sign", sign);

        // 构建充值自动提交表单
        String formStr = FormHelper.buildForm(HfbConst.INVEST_URL, paramMap);
        return formStr;
    }

    /**
     * 会员投资异步回调
     *
     * @param paramMap 用户投资回调参数
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void notify(Map<String, Object> paramMap) {
        log.info("投标成功");

        //获取投资编号
        String agentBillNo = (String) paramMap.get("agentBillNo");

        boolean result = transFlowService.isSaveTransFlow(agentBillNo);
        if (result) {
            log.warn("幂等性返回");
            return;
        }

        // 获取用户的绑定协议号
        String bindCode = (String) paramMap.get("voteBindCode");
        String voteAmt = (String) paramMap.get("voteAmt");

        // 修改商户系统中的用户账户金额：余额、冻结金额
        userAccountMapper.updateAccount(bindCode, new BigDecimal("-" + voteAmt), new BigDecimal(voteAmt));

        // 修改投资记录的投资状态为已支付
        LendItem lendItem = this.getByLendItemNo(agentBillNo);
        // 已支付
        lendItem.setStatus(1);
        baseMapper.updateById(lendItem);

        // 修改标的信息： 投资人数、已投金额
        Long lendId = lendItem.getLendId();
        Lend lend = lendMapper.selectById(lendId);
        lend.setInvestNum(lend.getInvestNum() + 1);
        lend.setInvestAmount(lend.getInvestAmount().add(lendItem.getInvestAmount()));
        lendMapper.updateById(lend);

        // 新增交易流水
        TransFlowBO transFlowBO = new TransFlowBO(
                agentBillNo,
                bindCode,
                new BigDecimal(voteAmt),
                TransTypeEnum.INVEST_LOCK,
                "投资项目编号：" + lend.getLendNo() + "，项目名称：" + lend.getTitle());
        transFlowService.saveTransFlow(transFlowBO);
    }

    /**
     * 根据标的编号获取标的信息
     *
     * @param lendItemNo 标的编号
     */
    private LendItem getByLendItemNo(String lendItemNo) {
        QueryWrapper<LendItem> queryWrapper = new QueryWrapper();
        queryWrapper.eq("lend_item_no", lendItemNo);
        return baseMapper.selectOne(queryWrapper);
    }

    /**
     * 根据lendId获取投资记录
     *
     * @param lendId 订单id
     * @param status 状态
     */
    @Override
    public List<LendItem> selectByLendId(Long lendId, Integer status) {
        QueryWrapper<LendItem> lendItemQueryWrapper = new QueryWrapper<>();
        lendItemQueryWrapper.eq("lend_id", lendId);
        lendItemQueryWrapper.eq("status", status);
        List<LendItem> lendItemList = baseMapper.selectList(lendItemQueryWrapper);
        return lendItemList;
    }

    /**
     * 获取投资列表
     *
     * @param lendId 借款id
     */
    @Override
    public List<LendItem> selectByLendId(Long lendId) {
        QueryWrapper<LendItem> lendItemQueryWrapper = new QueryWrapper<>();
        lendItemQueryWrapper.eq("lend_id", lendId);
        List<LendItem> lendItemList = baseMapper.selectList(lendItemQueryWrapper);
        return lendItemList;
    }
}
