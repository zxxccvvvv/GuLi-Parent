package com.atguigu.guli.service.oss.controller.api;

import com.atguigu.guli.service.base.result.R;
import com.atguigu.guli.service.oss.service.FileService;
import com.atguigu.guli.service.oss.util.OssProperties;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "阿里云文件管理")
//@CrossOrigin
@RestController
@RequestMapping("/api/oss/file")
@Slf4j
public class ApiFileController {

    @Autowired
    private OssProperties ossProperties;

    @Autowired
    private FileService fileService;

    @ApiOperation("文件上传")
    @PostMapping("upload-from-url")
    public R uploadFromUrl(@RequestParam("url") String url,
                           @RequestParam("module") String module) {
        String avatar = fileService.upload(url, module);
        return R.ok().message("文件上传成功").data("url", avatar);
    }

}
