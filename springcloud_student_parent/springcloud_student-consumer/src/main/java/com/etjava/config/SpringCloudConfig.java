package com.etjava.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * Spring Cloud 相关配置
 * @author etjava
 *
 */
@Configuration
public class SpringCloudConfig {

	/**
     * 	调用服务模版
     * 	通过RestTemplate 调用远程服务
     * @return
     */
    @Bean
    public RestTemplate getRestTemplate(){
        return new RestTemplate();
    }
}
