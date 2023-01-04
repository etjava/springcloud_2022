package com.etjava.service;

import java.util.List;
import java.util.Map;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.etjava.entity.Teacher;

@FeignClient(value="provider-1001",fallbackFactory = FeignClientFallbackFactory.class)
public interface FeignClientService {

	/**
     * 添加或者修改老师信息
     * @param Teacher
     */
	@PostMapping(value="/tea/save")
    public void save(Teacher teacher);
     
    /**
     * 根据id查找老师信息
     * @param id
     * @return
     */
	@GetMapping(value="/tea/findById/{id}")
    public Teacher findById(@PathVariable("id") Integer id);
     
    /**
     * 查询老师信息
     * @return
     */
	@GetMapping(value="/tea/list")
    public List<Teacher> list();
     
    /**
     * 根据id删除老师信息
     * @param id
     */
	@GetMapping(value="/tea/delete/{id}")
    public void delete(@PathVariable("id") Integer id);
    
    /**
     * 获取信息
     * @return
     */
	@GetMapping(value="/tea/getInfo")
    public Map<String,Object> getInfo();
	@GetMapping(value="/tea/get/{id}")
    public Teacher get(Integer id) ;
	
	
	/**
     * 测试熔断机制
     * @return
     * @throws InterruptedException
     */
	@GetMapping(value="/tea/validHystrix")
    public Map<String,Object> validHystrix() throws InterruptedException;
}
