package com.etjava.service;

import java.util.List;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.etjava.entity.Student;

/**
 *	基于feign方式的客户端访问时需要的service接口
 *	@FeignClient(value="STUDENT-PROVIDER")	该接口需要指定访问的实例名称 即 application.name 不需要填写http:// 底层封装了
 *	底层实现由feign帮着实现了 类似MybatisPlus
 *  	
 * @author etjav
 *
 */
@FeignClient(value="STUDENT-PROVIDER")
public interface StudentServiceWithFeign {

	 /**
     * 	根据id查询学生信息
     * @param id
     * @return
     */
    @GetMapping(value="/student/get/{id}")
    public Student get(@PathVariable("id") Integer id);
     
    /**
     * 	查询学生信息
     * @return
     */
    @GetMapping(value="/student/list")
    public List<Student> list();
     
    /**
     * 	添加或者修改学生信息
     * @param student
     * @return
     */
    @PostMapping(value="/student/save")
    public boolean save(Student student);
     
    /**
     * 	根据id删除学生信息
     * @return
     */
    @GetMapping(value="/student/delete/{id}")
    public boolean delete(@PathVariable("id") Integer id);
	
}
