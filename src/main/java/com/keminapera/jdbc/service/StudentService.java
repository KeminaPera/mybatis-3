package com.keminapera.jdbc.service;

import com.keminapera.jdbc.dao.StudentDao;
import com.keminapera.pojo.Student;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class StudentService {
  private final StudentDao studentDao;

  public StudentService() throws IOException {
    studentDao = new StudentDao();
  }

  public List<Student> getAgeGreaterThan(int minAge) {
    List<Student> studentList = studentDao.getAgeGreaterThan(minAge);
    return Optional.ofNullable(studentList).orElse(Collections.emptyList());
  }
}
