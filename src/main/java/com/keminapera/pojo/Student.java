package com.keminapera.pojo;


import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
public class Student {
  private Integer id;
  private String name;
  private Integer age;
}
