package com.etjava.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.etjava.entity.Teacher;
import com.etjava.service.TeacherService;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;

@RestController
@RequestMapping("/tea")
public class TeacherController {

	@Autowired
	private TeacherService teacherService;
	
	@GetMapping("/list")
	public List<Teacher> list(){
		return teacherService.list();
	}
	
	@GetMapping("/getInfo")
	public Map<String,Object> getInfo() {
		Map<String,Object> map = new HashMap<>();
		map.put("info",1005);
		return map;
	}
	
	 /**
	 * 测试验证带有熔断机制服务
	 * @HystrixCommand 标注当出现访问超时或方法报错时 调用fallbackMethod指定的方法进行返回
	 * @return
	 * @throws InterruptedException 
	 */
//	@GetMapping(value="/validHystrix")
//	public Map<String,Object> validHystrix() throws InterruptedException{
////	    Thread.sleep(100);
//	    return teacherService.validHystrix();
//	}
	
	@GetMapping(value="/validHystrix")
	@HystrixCommand(fallbackMethod="getInfoFallback")
	public Map<String,Object> validHystrix() throws InterruptedException{
	    Thread.sleep(200);
	    Map<String,Object> map=new HashMap<String,Object>();
	    map.put("code", 200);
	    map.put("info", "业务数据-----1005");
	    return map;
	}

	public Map<String,Object> getInfoFallback() throws InterruptedException{
	    Map<String,Object> map=new HashMap<String,Object>();
	    map.put("code", 500);
	    map.put("info", "系统出错，稍后重试");
	    return map;
	}
	
	@GetMapping("/get/{id}")
	public Teacher get(@PathVariable("id") Integer id) {
		return teacherService.findById(id);
	}
	
	@PostMapping("/save")
	public boolean save(@RequestBody Teacher teacher) {
		try {
			teacherService.save(teacher);
			return true;
		} catch (Exception e) {
			return false;
		}
	}
}
