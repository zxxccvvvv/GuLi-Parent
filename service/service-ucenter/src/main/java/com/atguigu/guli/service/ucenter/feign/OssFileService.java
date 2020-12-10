package com.atguigu.guli.service.ucenter.feign;

import com.atguigu.guli.service.base.result.R;
import com.atguigu.guli.service.ucenter.feign.impl.OssFileServiceFallBack;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Service
@FeignClient(value = "service-oss", fallback = OssFileServiceFallBack.class)
public interface OssFileService {

    @PostMapping("/api/oss/file/upload-from-url")
    R uploadFromUrl(
            @RequestParam("url") String url,
            @RequestParam("module") String module);
}
