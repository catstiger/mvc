package com.github.catstiger.mvc.resovler;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.github.catstiger.mvc.annotation.Api;
import com.github.catstiger.mvc.annotation.Param;
import com.github.catstiger.mvc.converter.Corp;
import com.github.catstiger.mvc.converter.Department;
import com.github.catstiger.mvc.converter.Employee;

@Api @Service
public class SpringTestService {
  @Api @Transactional
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
  
  @Api @Transactional
  public String testSingleBean(Employee employee) {
    return "";
  }
  
  @Api @Transactional
  public String testAny(@Param("emp") Employee emp, @Param("dept") Department dept, @Param("corpId") Long corpId) {
    emp.setDept(dept);
    dept.setCorp(new Corp());
    dept.getCorp().setId(corpId);
    
    return "";
  }
  
  @Api @Transactional
  public String testSingleValue(@Param("data") Double value) {
    return String.valueOf(value);
  }
  
  @Api @Transactional
  public String testSinglePrimitiveArray(@Param("dbl") double [] dbl) {
    return JSON.toJSONString(dbl);
  }
  
  @Api @Transactional
  public String testSingleDateArray(@Param("dates") Date[] date) {
    return JSON.toJSONString(date, SerializerFeature.WriteDateUseDateFormat);
  }
  
  
  @Api @Transactional
  public void testHttpAndOther(@Param("emp") Employee emp, @Param("dept") Department dept, HttpServletRequest request) {
    if(emp == null) {
      throw new RuntimeException("Employee is null.");
    }
    
    if(request == null) {
      throw new RuntimeException("HttpServletRequest is null.");
    }
  }
}
