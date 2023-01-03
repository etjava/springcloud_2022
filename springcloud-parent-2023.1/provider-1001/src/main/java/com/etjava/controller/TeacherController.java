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
		map.put("info",1001);
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
