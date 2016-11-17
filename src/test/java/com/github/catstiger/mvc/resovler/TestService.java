package com.github.catstiger.mvc.resovler;

import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.github.catstiger.mvc.annotation.API;
import com.github.catstiger.mvc.annotation.Domain;
import com.github.catstiger.mvc.annotation.Param;
import com.github.catstiger.mvc.converter.Corp;
import com.github.catstiger.mvc.converter.Department;
import com.github.catstiger.mvc.converter.Employee;
import com.github.catstiger.mvc.converter.TestValueConverter;

@Domain
public class TestService {
  @API
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
  
  @API
  public String testSingleBean(Employee employee) {
    return "";
  }
  
  @API
  public String testAny(@Param("emp") Employee emp, @Param("dept") Department dept, @Param("corpId") Long corpId) {
    emp.setDept(dept);
    if(dept != null) {
      dept.setCorp(new Corp());
      dept.getCorp().setId(corpId);
    }
    
    return "";
  }
  
  @API
  public String testSingleValue(@Param("data") Double value) {
    return String.valueOf(value);
  }
  
  @API
  public String testSinglePrimitiveArray(@Param("dbl") double [] dbl) {
    return JSON.toJSONString(dbl);
  }
  
  @API
  public String testSingleDateArray(@Param("dates") Date[] date) {
    return JSON.toJSONString(date, SerializerFeature.WriteDateUseDateFormat);
  }
  
  @API
  public void testEmptyArgs() {
    
  }
  
  @API
  public void testHttp(HttpServletRequest request, HttpServletResponse response) {
    if(request == null || response == null) {
      throw new RuntimeException("Request or response is null");
    }
    System.out.print("HTTP TEST OK");
  }
  
  @API
  public void testRequest(HttpServletRequest request) {
    if(request == null) {
      throw new RuntimeException("Request is null");
    }
    System.out.print("HTTP TEST OK");
  }
  
  @API
  public void testList(@Param("list")List<Long> list) {
    if(list == null || list.isEmpty()) {
      throw new RuntimeException("List is empty");
    }
  }
  
  @SuppressWarnings("rawtypes")
  @API
  public void testListNoParam(@Param("list")List list) {
    if(list == null || list.isEmpty()) {
      throw new RuntimeException("List is empty");
    }
  }
  
  @API
  public void testSet(@Param("dept") Department dept, @Param("set")Set<Long> set) {
    if(set == null || set.isEmpty()) {
      throw new RuntimeException("List is empty");
    }
    if(dept == null || dept.getList() == null || dept.getList().isEmpty()) {
      throw new RuntimeException("Dept is empty or it's list is empty.");
    }
  }
  
  @API
  public void testCustomerConverter(@Param(value = "param", converter = TestValueConverter.class) String name) {
    if(!"catstiger@gmail.com".equals(name)) {
      throw new RuntimeException(name);
    }
  }
}
