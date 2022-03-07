package com.atguigu.srb.core.controller.api;


import com.atguigu.common.exception.Assert;
import com.atguigu.common.result.R;
import com.atguigu.common.result.ResponseEnum;
import com.atguigu.common.util.RegexValidateUtils;
import com.atguigu.srb.core.pojo.vo.RegisterVO;
import com.atguigu.srb.core.service.UserInfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 用户基本信息 前端控制器
 * </p>
 *
 * @author lucky845
 * @since 2022-03-03
 */
@Api(tags = "会员接口")
@Slf4j
@CrossOrigin
@RestController
@RequestMapping("/api/core/userInfo")
public class UserInfoController {

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private UserInfoService userInfoService;

    @ApiOperation(value = "会员注册")
    @PostMapping("/register")
    public R register(@RequestBody RegisterVO registerVO) {

        // 手机号
        String mobile = registerVO.getMobile();
        // 密码
        String password = registerVO.getPassword();
        // 验证码
        String code = registerVO.getCode();

        // 数据校验
        // MOBILE_NULL_ERROR(-202, "手机号码不能为空")
        Assert.notEmpty(mobile, ResponseEnum.MOBILE_NULL_ERROR);
        // MOBILE_ERROR(-203, "手机号码不正确")
        Assert.isTrue(RegexValidateUtils.checkCellphone(mobile), ResponseEnum.MOBILE_ERROR);
        // PASSWORD_NULL_ERROR(204, "密码不能为空")
        Assert.notEmpty(password, ResponseEnum.PASSWORD_NULL_ERROR);
        // CODE_NULL_ERROR(205, "验证码不能为空")
        Assert.notEmpty(code, ResponseEnum.CODE_NULL_ERROR);

        // 校验验证码是否正确
        String codeGen = (String) redisTemplate.opsForValue().get("srb:sms:code:" + mobile);
        // CODE_ERROR(206, "验证码错误")
        Assert.equals(code, codeGen, ResponseEnum.CODE_ERROR);

        // 注册
        userInfoService.register(registerVO);
        return R.ok().message("注册成功");
    }

}

