package com.keminapera.mybatis.mapper;

import com.keminapera.pojo.Student;

import java.util.List;

public interface StudentMapper {

  int insertStudent(Student student);

  List<Student> selectAll();

  Student selectOne(Integer id);
}
