package com.etjava.controller;

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
import com.etjava.service.FeignClientService;

@RestController
@RequestMapping("/tea")
public class CustomerConntroller {


	@Autowired
	private FeignClientService feignClientService;
	
	
	@GetMapping("/list")
	public List<Teacher> list(){
		return feignClientService.list();
	}
	
	@GetMapping("/getInfo")
	public Map<String,Object> getInfo(){
		return feignClientService.getInfo();
	}
	
	@GetMapping("/get/{id}")
	public Teacher get(@PathVariable("id") Integer id) {
		return feignClientService.get(id);
	}
	
	@PostMapping("/save")
	public boolean save(@RequestBody Teacher teacher) {
		try {
			feignClientService.save(teacher);
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	
	@GetMapping("/validHystrix")
	public Map<String,Object> validHystrix() throws InterruptedException{
	    return feignClientService.validHystrix();
	}
}
