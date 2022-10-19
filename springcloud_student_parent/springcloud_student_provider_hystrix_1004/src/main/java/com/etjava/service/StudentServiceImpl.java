package com.etjava.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.etjava.entity.Student;
import com.etjava.repository.StudentRepository;

@Service
public class StudentServiceImpl implements StudentService{

	@Resource
	private StudentRepository studentRepository;

	@Override
	public void save(Student student) {
		studentRepository.save(student);
	}

	@Override
	public Student findById(Integer id) {
		return studentRepository.findOne(id);
	}

	@Override
	public List<Student> list() {
		return studentRepository.findAll();
	}

	@Override
	public void delete(Integer id) {
		studentRepository.delete(id);
	}

	@Override
	public Map<String, Object> getInfo() {
		Map<String,Object> map=new HashMap<String,Object>();
	    map.put("code", 200);
	    map.put("info", "业务数据-----------1004");
	    return map;
	}
}
