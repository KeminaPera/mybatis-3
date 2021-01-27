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
