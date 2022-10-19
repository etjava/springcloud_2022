package com.etjava.config;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import com.netflix.loadbalancer.ILoadBalancer;
import com.netflix.loadbalancer.IRule;
import com.netflix.loadbalancer.RetryRule;

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
    
    /**
     * 	自定义轮询算法 默认5次轮询 5次访问仍热失败则不在去访问失败的节点
     * @return
     */
    @Bean
    public IRule myRule(){
        return new RetryRule();
    }
}
