package com.keminapera.jdbc.dao;

import com.keminapera.pojo.Student;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

class StudentDaoTest {

  @Test
  public void testGetAllStudent() throws IOException {
    StudentDao studentDao = new StudentDao();
    List<Student> studentList = studentDao.getAgeGreaterThan(18);
    System.out.println(studentList.size());
  }

}
