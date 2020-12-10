package com.atguigu.guli.service.oss.service;

import org.springframework.stereotype.Component;

import java.io.InputStream;

@Component
public interface FileService {

    String upload(InputStream inputStream, String module, String oFileName);

    void removeFile(String url);

    String upload(String url, String module);
}
