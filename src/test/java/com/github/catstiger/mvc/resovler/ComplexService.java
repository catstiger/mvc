package com.github.catstiger.mvc.resovler;

import com.alibaba.fastjson.JSON;
import com.github.catstiger.mvc.annotation.API;
import com.github.catstiger.mvc.annotation.Domain;
import com.github.catstiger.mvc.model.Student;

@Domain
public class ComplexService {
  @API
  public Student save(Student student) {
    System.out.println(JSON.toJSON(student));
    return student;
  }
}
