package com.etjava.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.etjava.entity.Teacher;

@RestController
@RequestMapping("/tea")
public class CustomerConntroller {

	@Autowired
	private RestTemplate restTemplate;
	
	private static String PRE_URL="http://provider-1001";
	
	@SuppressWarnings("unchecked")
	@GetMapping("/list")
	public List<Teacher> list(){
		return restTemplate.getForObject(PRE_URL+"/tea/list", List.class);
	}
	
	@GetMapping("/get/{id}")
	public Teacher get(@PathVariable("id") Integer id) {
		return restTemplate.getForObject(PRE_URL+"/tea/get/"+id, Teacher.class);
	}
	
	@PostMapping("/save")
	public boolean save(@RequestBody Teacher teacher) {
		try {
			restTemplate.postForObject(PRE_URL+"/tea/save", teacher, Boolean.class);
			return true;
		} catch (Exception e) {
			return false;
		}
	}
}
