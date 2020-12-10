package com.atguigu.guli.service.ucenter.controller.api;


import com.atguigu.guli.common.util.FormUtils;
import com.atguigu.guli.service.base.dto.MemberDto;
import com.atguigu.guli.service.base.helper.JwtHelper;
import com.atguigu.guli.service.base.helper.JwtInfo;
import com.atguigu.guli.service.base.result.R;
import com.atguigu.guli.service.base.result.ResultCodeEnum;
import com.atguigu.guli.service.ucenter.entity.form.LoginForm;
import com.atguigu.guli.service.ucenter.entity.form.RegisterForm;
import com.atguigu.guli.service.ucenter.service.MemberService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 * 会员表 前端控制器
 * </p>
 *
 * @author Helen
 * @since 2020-10-12
 */
@Api(tags = "会员管理")
//@CrossOrigin
@RestController
@RequestMapping("/api/ucenter/member")
@Slf4j
public class ApiMemberController {

    @Autowired
    private MemberService memberService;

    @Autowired
    private RedisTemplate redisTemplate;

    @ApiOperation(value = "会员注册")
    @PostMapping("register")
    public R register(
            @ApiParam(value = "注册表单", required = true)
            @RequestBody RegisterForm registerForm
            ) {
        String nickname = registerForm.getNickname();
        String mobile = registerForm.getMobile();
        String password = registerForm.getPassword();
        String code = registerForm.getCode();

        //校验参数
        if (StringUtils.isEmpty(nickname)
        || StringUtils.isEmpty(password)
        || StringUtils.isEmpty(code)
        || StringUtils.isEmpty(mobile)
        || !FormUtils.isMobile(mobile)) {
            return R.setResult(ResultCodeEnum.PARAM_ERROR);
        }

        //检验验证码
        String key = "checkCode::" + mobile;
        String checkCode = (String) redisTemplate.opsForValue().get(key);
        if(!code.equals(checkCode)) {
           return R.setResult(ResultCodeEnum.CODE_ERROR);
        }

        memberService.register(registerForm);
        return R.ok().message("注册成功");
    }

    @ApiOperation(value = "会员登录")
    @PostMapping("login")
    public R login(
            @ApiParam(value = "登录表单")
            @RequestBody LoginForm loginForm
            ) {
        String mobile = loginForm.getMobile();
        String password = loginForm.getPassword();

        //校验参数
        if (StringUtils.isEmpty(mobile)
        || !FormUtils.isMobile(mobile)
        || StringUtils.isEmpty(password)) {
            return R.setResult(ResultCodeEnum.PARAM_ERROR);
        }

        String token = memberService.login(loginForm);
        return R.ok().data("token", token).message("登陆成功");
    }

    @ApiOperation(value = "根据token获取登录信息")
    @GetMapping("auth/get-login-info")
    public R getLoginInfo(HttpServletRequest request) {

        JwtInfo jwtInfo = JwtHelper.getJwtInfo(request);
        return R.ok().data("userInfo", jwtInfo);
    }

    @ApiOperation("根据会员id查询会员信息")
    @GetMapping("get-member-dto/{memberId}")
    public R getMemberDtoById(
            @ApiParam(value = "会员Id", required = true)
            @PathVariable String memberId
    ) {
        MemberDto memberDto = memberService.getMemberDtoById(memberId);
        return R.ok().data("memberDto", memberDto);
    }
}

