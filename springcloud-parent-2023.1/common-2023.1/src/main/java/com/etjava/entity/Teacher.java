package com.etjava.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 教师信息实体
 * @author Administrator
 *
 */
@Entity
@Table(name="t_teacher")
public class Teacher implements Serializable{
 
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
 
    @Id
    @GeneratedValue
    private Integer id; // 编号
     
    @Column(length=50)
    private String name; // 姓名
     
    @Column(length=50)
    private String subject; // 学科
     
    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
}