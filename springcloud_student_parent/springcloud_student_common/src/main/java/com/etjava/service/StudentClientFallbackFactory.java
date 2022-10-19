package com.etjava.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.etjava.entity.Student;

import feign.hystrix.FallbackFactory;

/**
 * 	解耦服务熔断服务降级 - 配置服务降级
 * 	当出现调用异常(超时，网络延迟等)会调用这里实现的方法
 * @author etjav
 *
 */
@Component
public class StudentClientFallbackFactory implements FallbackFactory<StudentServiceWithFeign>{

	
	// 当出现调用异常(超时，网络延迟等)会调用这里实现的方法
	@Override
	public StudentServiceWithFeign create(Throwable cause) {
		return new StudentServiceWithFeign() {

			@Override
			public Student get(Integer id) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public List<Student> list() {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public boolean save(Student student) {
				// TODO Auto-generated method stub
				return false;
			}

			@Override
			public boolean delete(Integer id) {
				// TODO Auto-generated method stub
				return false;
			}

			// 当调用getInfo出现异常时 会调用这里的降级方法
			@Override
			public Map<String, Object> getInfo() {
				Map<String,Object> map=new HashMap<String,Object>();
                map.put("code", 500);
                map.put("info", "系统出错，稍后重试");
                return map;
			}
			
		};
	}

}
