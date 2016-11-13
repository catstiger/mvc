package com.github.catstiger.mvc.resovler;

import java.util.Date;

import com.alibaba.fastjson.JSON;
import com.github.catstiger.mvc.annotation.Api;
import com.github.catstiger.mvc.annotation.Param;
import com.github.catstiger.mvc.converter.Department;
import com.github.catstiger.mvc.converter.Employee;

@Api
public class TestService {
  @Api
  public String testPrimitive(@Param("age") int age, @Param("score") double score, @Param("id") long id,
        @Param("birth") Date birth, @Param("isActive") boolean isActive) {
    Employee emp = new Employee();
    emp.setAge(age);
    emp.setScore(score);
    emp.setId(id);
    emp.setBirth(birth);
    emp.setIsActive(isActive);
    
    return JSON.toJSONString(emp, true);
  }
  
  @Api
  public String testSingleBean(Employee employee) {
    return JSON.toJSONString(employee, true);
  }
  
  @Api
  public String testAny(@Param("emp") Employee emp, @Param("dept") Department dept, @Param("corpId") Long corpId) {
    System.out.println(JSON.toJSONString(emp));
    System.out.println(JSON.toJSONString(dept));
    System.out.println(corpId);
    
    return null;
  }
}
