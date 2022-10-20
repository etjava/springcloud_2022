package com.etjava.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ConfigClientController {
 
    @Value("${port}")
    private String port;
 
    // 模拟获取外部配置文件中的信息
    @GetMapping("/getPort")
    public String getPort() {
        return port;
    }
 
    public void setPort(String port) {
        this.port = port;
    }
}
