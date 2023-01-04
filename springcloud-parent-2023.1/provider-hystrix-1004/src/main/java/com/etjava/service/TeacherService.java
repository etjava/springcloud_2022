package com.etjava.service;

import java.util.List;
import java.util.Map;

import com.etjava.entity.Teacher;

public interface TeacherService {

	/**
     * 添加或者修改老师信息
     * @param Teacher
     */
    public void save(Teacher teacher);
     
    /**
     * 根据id查找老师信息
     * @param id
     * @return
     */
    public Teacher findById(Integer id);
     
    /**
     * 查询老师信息
     * @return
     */
    public List<Teacher> list();
     
    /**
     * 根据id删除老师信息
     * @param id
     */
    public void delete(Integer id);
    
    /**
     * 测试熔断机制
     * @return
     * @throws InterruptedException
     */
    public Map<String,Object> validHystrix() throws InterruptedException;
}
