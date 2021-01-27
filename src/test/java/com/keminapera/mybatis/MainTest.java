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
package com.keminapera.mybatis;

import com.keminapera.mybatis.mapper.StudentMapper;
import com.keminapera.pojo.Student;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Slf4j
public class MainTest {
  private static SqlSessionFactory sessionFactory;

  @BeforeAll
  static void before() {
    SqlSessionFactoryBuilder builder = new SqlSessionFactoryBuilder();
    InputStream is = null;
    try {
      is = Resources.getResourceAsStream("mybatis-config.xml");
    } catch (IOException e) {
      e.printStackTrace();
    }
    sessionFactory = builder.build(is);
  }

  @Test
  public void testMain() {
    try(SqlSession sqlSession = sessionFactory.openSession(true)){
      int count = sqlSession.insert("com.keminapera.mybatis.mapper.StudentMapper.insertStudent", getStudent());
      System.out.println("插入的学生数=" + count);
    }
  }

  private Student getStudent() {
    Student student = Student.builder()
      .id(3)
      .name("wangwu")
      .age(26)
      .build();
    return student;
  }

  @Test
  public void testSelect() {
    SqlSession sqlSession = sessionFactory.openSession(true);
    RowBounds rowBounds = new RowBounds(0,3);
    List<Object> objects = sqlSession.selectList("com.keminapera.mybatis.mapper.StudentMapper.selectAll", null, rowBounds);
    objects.forEach(System.out::println);
  }

  @Test
  public void testSelectOne() {
    SqlSession sqlSession = sessionFactory.openSession(true);
    Object one = sqlSession.selectOne("com.keminapera.mybatis.mapper.StudentMapper.selectOne", 10);
    System.out.println("从数据库中查询出来的数据=" + one);
    Object another = sqlSession.selectOne("com.keminapera.mybatis.mapper.StudentMapper.selectOne", 10);
    System.out.println(one == another);
  }

  @Test
  public void testTypeHandler() {
    try(SqlSession sqlSession = sessionFactory.openSession(true)){
      StudentMapper studentMapper = sqlSession.getMapper(StudentMapper.class);
      studentMapper.insertStudent(getStudent());
    }
  }

}
