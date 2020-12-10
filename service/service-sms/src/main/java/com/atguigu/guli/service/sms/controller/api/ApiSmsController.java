package com.atguigu.guli.service.sms.controller.api;

import com.aliyuncs.exceptions.ClientException;
import com.atguigu.guli.common.util.FormUtils;
import com.atguigu.guli.common.util.RandomUtils;
import com.atguigu.guli.service.base.result.R;
import com.atguigu.guli.service.base.result.ResultCodeEnum;
import com.atguigu.guli.service.sms.service.SmsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/api/sms")
@Api(tags = "短信管理")
//@CrossOrigin //跨域
@Slf4j
public class ApiSmsController {

    @Autowired
    private SmsService smsService;

    @Autowired
    private RedisTemplate redisTemplate;

    @ApiOperation("获取验证码")
    @GetMapping("send/{mobile}")
    public R getCode(
            @ApiParam(value = "手机号", required = true)
            @PathVariable String mobile
    ) throws ClientException {
        //校验手机号是否合法
        if(StringUtils.isEmpty(mobile) || !FormUtils.isMobile(mobile)) {
            return R.setResult(ResultCodeEnum.LOGIN_PHONE_ERROR);
        }

        //生成验证码
        String checkCode = RandomUtils.getFourBitRandom();
        //发送验证码
        smsService.send(mobile, checkCode);
        //将验证码存入redis缓存
        String key = "checkCode::" + mobile;
        redisTemplate.opsForValue().set(key, checkCode, 5, TimeUnit.MINUTES);
        return R.ok().message("短信发送成功");
    }
}
