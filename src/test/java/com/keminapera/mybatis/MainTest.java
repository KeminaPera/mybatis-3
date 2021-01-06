package com.keminapera.mybatis;

import com.keminapera.pojo.Student;
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
    SqlSession sqlSession = sessionFactory.openSession(true);
    Student student = Student.builder()
      .id(3)
      .name("wangwu")
      .age(26)
      .build();
    int count = sqlSession.insert("com.keminapera.mybatis.mapper.StudentMapper.insertStudent", student);
    System.out.println("插入的学生数=" + count);
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
    Object one = sqlSession.selectOne("com.keminapera.mybatis.mapper.StudentMapper.selectOne", 2);
    System.out.println("从数据库中查询出来的数据=" + one);
    Object another = sqlSession.selectOne("com.keminapera.mybatis.mapper.StudentMapper.selectOne", 2);
    System.out.println(one == another);
  }
}
