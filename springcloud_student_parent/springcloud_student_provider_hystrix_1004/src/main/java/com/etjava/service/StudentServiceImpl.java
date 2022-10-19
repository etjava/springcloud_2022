package com.etjava.service;

import java.util.List;

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
}
