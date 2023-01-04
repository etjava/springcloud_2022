package com.etjava.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
@RestController
@RequestMapping("/tea")
public class CustomerConntroller {

	@Autowired
	private RestTemplate restTemplate;
	
	private static String PRE_URL="http://provider-1001";
	
	@SuppressWarnings("unchecked")
	@GetMapping(value="/validHystrix")
	public Map<String,Object> validHystrix() throws InterruptedException{
	   return restTemplate.getForObject(PRE_URL+"/tea/validHystrix", Map.class);
	}
}
