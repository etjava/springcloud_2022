package com.etjava.config;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
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
    @LoadBalanced  // 引入ribbon负载均衡
    public RestTemplate getRestTemplate(){
        return new RestTemplate();
    }
}
