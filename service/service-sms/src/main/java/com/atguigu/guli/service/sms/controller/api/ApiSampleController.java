package com.atguigu.guli.service.sms.controller.api;

import com.atguigu.guli.service.base.result.R;
import com.atguigu.guli.service.sms.util.SmsProperties;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RefreshScope
@Api(tags = "测试配置")
@RestController
@RequestMapping("/api/sms/sample")
public class ApiSampleController {

    @Value("${aliyun.sms.signName}")
    private String signName;

    @GetMapping("test1")
    public R test1(){
        return R.ok().data("signName", signName);
    }

    @Autowired
    private SmsProperties smsProperties;

    @GetMapping("test2")
    public R test2(){
        return R.ok().data("smsProperties", smsProperties);
    }
}
