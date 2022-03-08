package com.atguigu.srb.core.controller.api;


import com.atguigu.common.exception.Assert;
import com.atguigu.common.result.R;
import com.atguigu.common.result.ResponseEnum;
import com.atguigu.common.util.RegexValidateUtils;
import com.atguigu.srb.base.util.JwtUtils;
import com.atguigu.srb.core.pojo.vo.LoginVO;
import com.atguigu.srb.core.pojo.vo.RegisterVO;
import com.atguigu.srb.core.pojo.vo.UserInfoVO;
import com.atguigu.srb.core.service.UserInfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

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

    /**
     * 会员注册
     *
     * @param registerVO 会员注册信息
     */
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

    /**
     * 会员登陆
     *
     * @param loginVO 会员登陆信息
     */
    @ApiOperation(value = "会员登陆")
    @PostMapping("/login")
    public R login(@RequestBody LoginVO loginVO, HttpServletRequest request) {

        String mobile = loginVO.getMobile();
        String password = loginVO.getPassword();

        // MOBILE_NULL_ERROR(-202, "手机号码不能为空")
        Assert.notEmpty(mobile, ResponseEnum.MOBILE_NULL_ERROR);
        // PASSWORD_NULL_ERROR(204, "密码不能为空")
        Assert.notEmpty(password, ResponseEnum.PASSWORD_NULL_ERROR);

        String ip = request.getRemoteAddr();
        UserInfoVO userInfoVO = userInfoService.login(loginVO, ip);

        return R.ok().data("userInfo", userInfoVO);
    }

    /**
     * 校验令牌
     */
    @ApiOperation(value = "校验令牌")
    @GetMapping("/checkToken")
    public R checkToken(HttpServletRequest request) {

        String token = request.getHeader("token");
        boolean result = JwtUtils.checkToken(token);

        if (result) {
            return R.ok();
        } else {
            // LOGIN_AUTH_ERROR(-211, "未登录")
            return R.setResult(ResponseEnum.LOGIN_AUTH_ERROR);
        }
    }

    /**
     * 校验手机号是否注册
     * @param mobile 手机号
     */
    @ApiOperation("校验手机号是否注册")
    @GetMapping("/checkMobile/{mobile}")
    public boolean checkMobile(@PathVariable String mobile){
        return userInfoService.checkMobile(mobile);
    }

}

