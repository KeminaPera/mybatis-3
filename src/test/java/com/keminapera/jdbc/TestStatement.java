package com.keminapera.jdbc;

import com.keminapera.pojo.Student;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.util.Collections;

public class TestStatement {
  @Test
  public void test() {
    try {
      Student student = Student.builder()
        .id(9)
        .name("zhangsan")
        .age(12)
        .school(Collections.emptyList())
        .build();
      String sql = "INSERT INTO student('n_id', 'c_name', 'n_age', 'c_school') VALUES('#{student.id}', '#{student.name}', '#{student.age}', '#{student.school}')";
      Connection connection = DriverManager.getConnection("jdbc:mysql://172.16.34.183:3306/student", "litemall", "123456");
      PreparedStatement ps = connection.prepareStatement(sql);
      ps.setInt(1, student.getId());
      ps.setString(2, student.getName());
      ps.setInt(3, student.getAge());
      ps.setString(4, student.getSchool().toString());

      ps.execute();
      int updateCount = ps.getUpdateCount();
      System.out.println(updateCount);
    }catch (Exception e) {
      e.printStackTrace();
    }
  }
}
