package com.etjava.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.etjava.entity.Student;
import com.etjava.service.StudentServiceWithFeign;

/**
 * 	服务消费者 controller
 * @author etjav
 *
 */
@RestController
@RequestMapping("/student")
public class StudentConsumerController {

	
	
	@Autowired
	private StudentServiceWithFeign studentServiceWithFeign;
	
	@Value("${provider_host}")
	private String HOST;
	
	/**
     * 	添加或者修改学生信息
     * @param student
     * @return
     */
    @PostMapping(value="/save")
    public boolean save(Student student){
    	return studentServiceWithFeign.save(student);
    }
     
    /**
     * 	查询学生信息
     * @return
     */
    @GetMapping(value="/list")
    public List<Student> list(){
        return studentServiceWithFeign.list();
    }
     
    /**
     * 	根据id查询学生信息
     * @return
     */
    @GetMapping(value="/get/{id}")
    public Student get(@PathVariable("id") Integer id){
        return studentServiceWithFeign.get(id);
    }
     
    /**
     * 	根据id删除学生信息
     * @return
     */
    @GetMapping(value="/delete/{id}")
    public boolean delete(@PathVariable("id") Integer id){
    	return studentServiceWithFeign.delete(id);
    }
    
    /**
     * 	模拟Hystrix熔断和降级处理
     * @return
     */
    @GetMapping(value="/getInfo")
    @ResponseBody
    public Map<String,Object> getInfo(){
        return studentServiceWithFeign.getInfo();
    }
}
