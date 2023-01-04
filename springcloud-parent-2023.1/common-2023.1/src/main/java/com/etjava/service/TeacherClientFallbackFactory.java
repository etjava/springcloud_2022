package com.etjava.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import feign.hystrix.FallbackFactory;

@Component
public class TeacherClientFallbackFactory implements FallbackFactory<FeignClientHystrixService>{

	@Override
	public FeignClientHystrixService create(Throwable cause) {
		// 接口中所有降级处理的方法 如果有多个方法 则只需要在这里进行统一处理即可
		return new FeignClientHystrixService() {
			@Override
			public Map<String, Object> validHystrix(){
				Map<String,Object> map = new HashMap<>();
				map.put("code", "500");
				map.put("msg", "访问超时");
				return map;
			}
		};
	}
}
