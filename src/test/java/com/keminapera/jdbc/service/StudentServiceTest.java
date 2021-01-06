package com.keminapera.jdbc.service;

import com.keminapera.pojo.Student;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

class StudentServiceTest {

  @Test
  public void testGetAgeGreaterThan() throws IOException {
    StudentService service = new StudentService();
    List<Student> studentList = service.getAgeGreaterThan(18);
    Assertions.assertNotNull(studentList);
  }
}
