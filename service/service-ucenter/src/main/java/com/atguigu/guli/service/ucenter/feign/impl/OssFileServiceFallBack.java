package com.atguigu.guli.service.ucenter.feign.impl;

import com.atguigu.guli.service.base.result.R;
import com.atguigu.guli.service.ucenter.feign.OssFileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class OssFileServiceFallBack implements OssFileService {
    @Override
    public R uploadFromUrl(String url, String module) {
        log.warn("远程调用失败，服务熔断");
        return R.error();
    }
}
