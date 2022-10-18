package com.etjava.controller;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.etjava.entity.Student;

/**
 * 	服务消费者 controller
 * @author etjav
 *
 */
@RestController
@RequestMapping("/student")
public class StudentConsumerController {

	
	/**
	 * restTemplate 调用远程服务端
	 */
	@Resource
    private RestTemplate restTemplate;
	
	@Value("${provider_host}")
	private String HOST;
	
	/**
     * 	添加或者修改学生信息
     * @param student
     * @return
     */
    @PostMapping(value="/save")
    public boolean save(Student student){
    	return restTemplate.postForObject(HOST+"/student/save", student, Boolean.class);
    }
     
    /**
     * 	查询学生信息
     * @return
     */
    @GetMapping(value="/list")
    public List<Student> list(){
        return restTemplate.getForObject("http://STUDENT-PROVIDER/student/list", List.class);
    }
     
    /**
     * 	根据id查询学生信息
     * @return
     */
    @GetMapping(value="/get/{id}")
    public Student get(@PathVariable("id") Integer id){
        return restTemplate.getForObject(HOST+"/student/get/"+id, Student.class);
    }
     
    /**
     * 	根据id删除学生信息
     * @return
     */
    @GetMapping(value="/delete/{id}")
    public boolean delete(@PathVariable("id") Integer id){
    	return restTemplate.getForObject(HOST+"/student/delete/"+id,Boolean.class);
    }
}
