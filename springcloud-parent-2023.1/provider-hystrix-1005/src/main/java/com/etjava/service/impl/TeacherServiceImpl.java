package com.etjava.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.etjava.entity.Teacher;
import com.etjava.repository.TeacherRepository;
import com.etjava.service.TeacherService;

@Service
public class TeacherServiceImpl implements TeacherService{

    @Autowired
    private TeacherRepository teacherRepository;

    @Override
    public void save(Teacher teacher) {
        teacherRepository.save(teacher);
    }

    @Override
    public Teacher findById(Integer id) {
        return teacherRepository.findOne(id);
    }

    @Override
    public List<Teacher> list() {
        return teacherRepository.findAll();
    }

    @Override
    public void delete(Integer id) {
        teacherRepository.delete(id);
    }

	@Override
	public Map<String, Object> validHystrix() throws InterruptedException {
	    Map<String,Object> map=new HashMap<String,Object>();
	    map.put("code", 200);
	    map.put("info", "业务数据-----1005");
	    return map;
	}

}
