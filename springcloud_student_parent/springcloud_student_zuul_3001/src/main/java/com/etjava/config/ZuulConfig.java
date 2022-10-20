package com.etjava.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.etjava.filter.AccessFilter;

/**
 *  zuul 配置
 * @author etjav
 *
 */
@Configuration
public class ZuulConfig {

	/**
	 *  将AccessFilter配置成Bean 就可以在每次访问时执行了
	 * @return
	 */
	@Bean
	public AccessFilter accessFilter() {
		return new AccessFilter();
	}
}
