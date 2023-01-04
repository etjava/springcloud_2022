package com.etjava.service;

import java.util.Map;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Feign整合解耦
 * @FeignClient(value="provider-1004") 带有hystrix的服务名称是1004 非1001
 * @author etjav
 *
 */
@FeignClient(value="provider-1004",fallbackFactory=TeacherClientFallbackFactory.class)
public interface FeignClientHystrixService {

	@GetMapping(value="/validHystrix")
	public Map<String,Object> validHystrix();
}
