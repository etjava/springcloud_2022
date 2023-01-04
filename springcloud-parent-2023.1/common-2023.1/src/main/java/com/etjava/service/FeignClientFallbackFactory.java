package com.etjava.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.etjava.entity.Teacher;

import feign.hystrix.FallbackFactory;

@Component
public class FeignClientFallbackFactory implements FallbackFactory<FeignClientService>{

	@Override
	public FeignClientService create(Throwable cause) {
		return new FeignClientService() {
			
			@Override
			public Map<String, Object> validHystrix() throws InterruptedException {
				Map<String,Object> map=new HashMap<String,Object>();
			    map.put("code", 500);
			    map.put("info", "测试服务降级处理");
			    return map;
			}
			
			@Override
			public void save(Teacher teacher) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public List<Teacher> list() {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public Map<String, Object> getInfo() {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public Teacher get(Integer id) {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public Teacher findById(Integer id) {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public void delete(Integer id) {
				// TODO Auto-generated method stub
				
			}
		};
	}

}
