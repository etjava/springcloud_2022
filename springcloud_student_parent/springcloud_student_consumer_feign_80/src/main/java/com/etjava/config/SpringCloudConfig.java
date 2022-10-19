package com.etjava.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

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
     * 	自定义轮询算法 默认5次轮询 5次访问仍热失败则不在去访问失败的节点
     * @return
     */
    @Bean
    public IRule myRule(){
        return new RetryRule();
    }
}
