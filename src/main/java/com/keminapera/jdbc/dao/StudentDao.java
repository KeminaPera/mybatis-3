/**
 *    Copyright 2009-2021 the original author or authors.
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package com.keminapera.jdbc.dao;

import com.keminapera.jdbc.DbPropertiesManager;
import com.keminapera.pojo.Student;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class StudentDao {

  private final DbPropertiesManager.DbProperties dbProperties;

  public StudentDao() throws IOException {
    dbProperties = DbPropertiesManager.getDbProperties();
  }
  public List<Student> getAgeGreaterThan(int minAge) {
    Connection connection = null;
    PreparedStatement statement = null;
    ResultSet resultSet = null;

    List<Student> studentList = new ArrayList<>();
    try {
      //获取连接
      connection = DriverManager.getConnection(dbProperties.getUrl(), dbProperties.getUsername(), dbProperties.getPassword());
      //构造sql语句
      String sql = "SELECT * FROM student WHERE age > ?";
      //获取Statement对象
      statement = connection.prepareStatement(sql);
      //设置参数
      statement.setInt(1, minAge);
      //执行
      statement.execute();
      //获取结果集
      resultSet = statement.getResultSet();
      //处理结果集
      while (resultSet.next()) {
        int id = resultSet.getInt("id");
        String name = resultSet.getString("name");
        int age = resultSet.getInt("age");
        Student student = Student.builder()
          .id(id)
          .name(name)
          .age(age)
          .build();
        studentList.add(student);
      }
      //关闭资源
    }catch (SQLException e) {
      e.printStackTrace();
    }finally {
      if (resultSet != null) {
        try {
          resultSet.close();
        }catch (SQLException e) {
          e.printStackTrace();
        }
      }
      if (statement != null) {
        try {
          statement.close();
        } catch (SQLException e) {
          e.printStackTrace();
        }
      }
      if (connection != null) {
        try {
          connection.close();
        } catch (SQLException e) {
          e.printStackTrace();
        }
      }
    }
    return studentList;
  }
}
